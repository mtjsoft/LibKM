package com.suntront.module_login.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.mtjsoft.www.kotlinmvputils.base.BaseViewModel
import com.mtjsoft.www.kotlinmvputils.event.SingleLiveEvent
import com.mtjsoft.www.kotlinmvputils.utils.KLog
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * @author mtj
 * @Package com.suntront.module_login.login
 * @date 2020-05-25 15:40:04
 */

class LoginViewModel : BaseViewModel() {

    val nameSingleLiveEvent = MutableLiveData<String>()

    val pswSingleLiveEvent = MutableLiveData<String>()

    fun login() {
        KLog.e(nameSingleLiveEvent.value + "," + pswSingleLiveEvent.value)
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