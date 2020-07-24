package com.suntront.module_login.login

import android.text.TextUtils
import androidx.lifecycle.rxLifeScope
import com.mtjsoft.www.kotlinmvputils.base.BaseViewModel
import com.mtjsoft.www.kotlinmvputils.event.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * @author mtj
 * @Package com.suntront.module_login.login
 * @date 2020-05-25 15:40:04
 */

class LoginViewModel : BaseViewModel() {

    private val nameSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private val pswSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()


    fun setName(name: String) {
        nameSingleLiveEvent.value = name
    }

    fun setPsw(psw: String) {
        pswSingleLiveEvent.value = psw
    }

    fun login() {
        if (TextUtils.isEmpty(nameSingleLiveEvent.value)) {
            showToastUI("请输入账号")
            return
        }
        if (TextUtils.isEmpty(pswSingleLiveEvent.value)) {
            showToastUI("请输入密码")
            return
        }
        // 开启协程，模拟登录请求
        rxLifeScope.launch {
            showLoadingUI("正在登录...", false)
            async {
                // 延迟3秒
                delay(3000)
            }.await()
            hideLoadingUI()
            showToastUI("登录成功")
        }
    }
}