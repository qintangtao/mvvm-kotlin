package com.qin.mvvm.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.Utils
import com.qin.mvvm.event.Message
import com.qin.mvvm.event.SingleLiveEvent
import com.qin.mvvm.network.ExceptionHandle
import com.qin.mvvm.network.ResponseThrowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*

open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }

    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }

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
    fun <T> launchOnlyresult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: () -> Unit = {
            defUI.complete.call()
        },
        isNotify: Boolean = true
    ) {
        if (isNotify) defUI.start.call()
        launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { res ->
                    executeResponse(res) { success(it) }
                },
                { error(it) },
                { complete() }
            )
        }
    }
	
	fun <T> launchFlowrersult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit,
		block2: suspend CoroutineScope.() -> IBaseResponse<T>,
        success2: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: () -> Unit = {},
        isNotify: Boolean = true
    ) {
		launchUI {
            launchFlow { block() }
                .flatMapConcat {
                    return@flatMapConcat executeResponseFlow(it) {
                        success(it)
                        launchFlow { block2() } }
                }
                .onStart { if (isNotify) defUI.start.call() }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.complete.call()
                    complete() }
                .catch {
					error(ExceptionHandle.handleException(it))
                }
                .collect {
					executeResponse(it) { success2(it) }
                }
        }
    }
	
	fun <T> launchFlowrersult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit,
		block2: suspend CoroutineScope.() -> IBaseResponse<T>,
        success2: (T) -> Unit,
		block3: suspend CoroutineScope.() -> IBaseResponse<T>,
        success3: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.error.postValue(Message(it.code, it.msg))
        },
        complete: () -> Unit = {},
        isNotify: Boolean = true
    ) {
		launchUI {
            launchFlow { block() }
                .flatMapConcat {
                    return@flatMapConcat executeResponseFlow(it) {
                        success(it)
                        launchFlow { block2() } }
                }
                .flatMapConcat {
                    return@flatMapConcat executeResponseFlow(it) {
                        success2(it)
                        launchFlow { block3() } }
                }
                .onStart { if (isNotify) defUI.start.call() }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.complete.call()
                    complete() }
                .catch {
					error(ExceptionHandle.handleException(it))
                }
                .collect {
					executeResponse(it) { success3(it) }
                }
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
            else throw ResponseThrowable(response.code(), response.msg())
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
            else throw ResponseThrowable(response.code(), response.msg())
        }
    }

    /**
     * 异常统一处理
     */
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
        val complete by lazy { SingleLiveEvent<Void>() }
        val error by lazy { SingleLiveEvent<Message>() }
    }
}