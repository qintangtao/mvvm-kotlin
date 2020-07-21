package com.qin.mvvm.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.Utils
import com.qin.mvvm.event.Message
import com.qin.mvvm.event.SingleLiveEvent
import com.qin.mvvm.network.ExceptionHandle
import com.qin.mvvm.network.RESULT
import com.qin.mvvm.network.ResponseThrowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*

open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }

    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun <T> launchFlow(block: suspend () -> T): Flow<T> = flow { emit(block()) }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     */
    fun launchGo(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: suspend CoroutineScope.() -> Unit = {
            defUI.complete.call()
        },
        isNotify: Boolean = true
    ) {
        if (isNotify) defUI.start.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { error(it) },
                { complete() }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     */
    fun <T> launchOnlyResult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Int,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: suspend CoroutineScope.() -> Unit = {
            defUI.complete.call()
        },
        isNotify: Boolean = true
    ) {
        if (isNotify) defUI.start.call()
        launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { executeResponse(it) { defUI.result.postValue(success(it)) } },
                { error(it) },
                { complete() }
            )
        }
    }

    fun <T, R> launchSerialResult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Int,
        block2: suspend CoroutineScope.() -> IBaseResponse<R>,
        success2: suspend CoroutineScope.(R) -> Int,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: suspend CoroutineScope.() -> Unit = {
            defUI.complete.call()
        },
        isNotify: Boolean = true
    ) {
        if (isNotify) defUI.start.call()
        launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { executeResponseResult(it) {
                    val code = success(it)
                    if (code != RESULT.SUCCESS.code)
                        defUI.result.postValue(code)
                    code == RESULT.SUCCESS.code
                } },
                { withContext(Dispatchers.IO) { block2() }},
                { executeResponse(it) { defUI.result.postValue(success2(it)) } },
                { error(it) },
                { complete() }
            )
        }
    }

    fun <T, R, Z> launchFlowzipResult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        block2: suspend CoroutineScope.() -> IBaseResponse<R>,
        compose: suspend CoroutineScope.(T, R) -> Z,
        success: suspend CoroutineScope.(Z) -> Int,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: () -> Unit = {},
        isNotify: Boolean = true
    ) {
        launchUI {
            launchFlow { block() }
                .zip(launchFlow { block2() }) { l, r ->
                    executeResponseFlow(l, r) { ld, rd -> compose(ld, rd) } }
                .onStart { if (isNotify) defUI.start.call() }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.complete.call(); complete() }
                .catch { error(ExceptionHandle.handleException(it)) }
                .collect { defUI.result.postValue(success(it)) }
        }
    }

    fun <T, R> launchFlowResult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Int,
        block2: suspend CoroutineScope.() -> IBaseResponse<R>,
        success2: suspend CoroutineScope.(R) -> Int,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: suspend CoroutineScope.() -> Unit = {},
        isNotify: Boolean = true
    ) {
        launchUI {
            launchFlow { block() }
                .flatMapConcat {
                    return@flatMapConcat executeResponseFlow(it) {
                        val code = success(it)
                        if (code != RESULT.SUCCESS.code) {
                            defUI.result.postValue(code)
                            flow{}
                        } else launchFlow { block2() } }
                }
                .onStart { if (isNotify) defUI.start.call() }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.complete.call(); complete() }
                .catch { error(ExceptionHandle.handleException(it)) }
                .collect { executeResponse(it) { defUI.result.postValue(success2(it)) } }
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T> executeResponse(
        response: IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.isSuccess()) success(response.data())
            else throw ResponseThrowable(response)
        }
    }

    private suspend fun <T> executeResponseResult(
        response: IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Boolean
    ): Boolean {
        return coroutineScope {
            if (response.isSuccess()) success(response.data())
            else throw ResponseThrowable(response)
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T, R, C> executeResponseFlow(
        response: IBaseResponse<T>,
        response2: IBaseResponse<R>,
        success: suspend CoroutineScope.(T, R) -> C
    ): C {
        return coroutineScope {
            if (response.isSuccess()) {
                if (response2.isSuccess()) {
                    success(response.data(), response2.data())
                } else throw ResponseThrowable(response2)
            } else throw ResponseThrowable(response)
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T, R> executeResponseFlow(
        response: IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Flow<R>
    ): Flow<R> {
        return coroutineScope {
            if (response.isSuccess()) success(response.data())
            else throw ResponseThrowable(response)
        }
    }

    private suspend fun <T, R> handleException(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(IBaseResponse<T>) -> Boolean,
        block2: suspend CoroutineScope.() -> IBaseResponse<R>,
        success2: suspend CoroutineScope.(IBaseResponse<R>) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                if (success(block()))
                    success2(block2())
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }

    private suspend fun <T> handleException(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(IBaseResponse<T>) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }

    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }

    inner class UIChange {
        val start by lazy { SingleLiveEvent<Void>() }
        val error by lazy { SingleLiveEvent<Message>() }
        val result by lazy { SingleLiveEvent<Int>() }
        val complete by lazy { SingleLiveEvent<Void>() }
    }

    inline fun callStart() {
        defUI.start.call()
    }

    inline fun callError(msg: Message) {
        defUI.error.postValue(msg)
    }

    inline fun callResult(code: Int) {
        defUI.result.postValue(code)
    }

    inline fun callComplete() {
        defUI.complete.call()
    }

    //other
    fun getString(resId: Int): String {
        return getApplication<Application>().getString(resId)
    }
}