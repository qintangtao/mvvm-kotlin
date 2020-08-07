package com.qin.wan.ui.settings

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.*
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.bus.Bus
import com.qin.mvvm.util.getFormateSize
import com.qin.mvvm.util.isNightMode
import com.qin.mvvm.util.setNightMode
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.model.store.SettingsStore
import com.qin.wan.ui.common.UserRepository
import com.qin.wan.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.view_change_text_zoom.view.*

class SettingsViewModel : BaseViewModel() {

    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    private val _isLogin = MutableLiveData<Boolean>()
    private val _isNight = MutableLiveData<Boolean>()
    private val _cacheSize = MutableLiveData<String>()
    private val _fontSize = MutableLiveData<String>()

    val isLogin : LiveData<Boolean> = _isLogin
    val isNight : LiveData<Boolean> = _isNight
    val cacheSize : LiveData<String> = _cacheSize
    val fontSize : LiveData<String> = _fontSize

    fun initData() {
        _isLogin.value = repository.isLogin()
        _isNight.value = isNightMode(ActivityUtils.getTopActivity())
        _cacheSize.value = getFormateSize(CacheDiskStaticUtils.getCacheSize())
        _fontSize.value = "${SettingsStore.getWebTextZoom()}%"
    }

    fun logout() {
        repository.clearLoginState()
        Bus.post(UserRepository.USER_LOGIN_STATE_CHANGED, false)
    }

    fun click(view: View) {
        when(view.id) {
            R.id.llClearCache -> {
                AlertDialog.Builder(view.context)
                    .setMessage(R.string.confirm_clear_cache)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        CacheDiskStaticUtils.clear()
                        _cacheSize.value = getFormateSize(CacheDiskStaticUtils.getCacheSize())
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
            R.id.tvLogout -> {
                AlertDialog.Builder(view.context)
                    .setMessage(R.string.confirm_logout)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        logout()
                        _isLogin.value = repository.isLogin()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
            R.id.llCheckVersion -> {
                ToastUtils.showLong(getString(R.string.stay_tuned))
            }
            R.id.llAboutUs -> {
                view.context.startActivity(Intent().apply {
                    setClass(view.context, DetailActivity::class.java)
                    putExtra(
                        DetailActivity.PARAM_ARTICLE, Article(
                            title = getString(R.string.abount_us),
                            link = "https://github.com/qintangtao/mvvm-kotlin"
                        )
                    )
                })
            }
            R.id.llFontSize -> {
                setFontSize(view.context)
            }
        }
    }

    fun setFontSize(context: Context) {
        val textZoom = SettingsStore.getWebTextZoom()
        var tempTextZoom = textZoom
        AlertDialog.Builder(context)
            .setTitle(R.string.font_size)
            .setView(LayoutInflater.from(context).inflate(R.layout.view_change_text_zoom, null).apply {
                seekBar.progress = textZoom - 50
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        tempTextZoom = 50 + progress
                        _fontSize.value = "$tempTextZoom%"
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }
                })
            })
            .setNegativeButton(R.string.cancel) { _, _ ->
                _fontSize.value = "$textZoom%"
            }
            .setPositiveButton(R.string.confirm) { _, _ ->
                SettingsStore.setWebTextZoom(tempTextZoom)
            }
            .show()
    }

    val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, checked ->
        if (_isNight.value != checked) {
            _isNight.value = checked
            setNightMode(checked)
            SettingsStore.setNightMode(checked)
        }
    }
}