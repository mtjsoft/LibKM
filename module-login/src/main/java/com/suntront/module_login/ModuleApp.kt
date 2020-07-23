package com.suntront.module_login

import android.app.Application
import com.mtjsoft.www.kotlinmvputils.imp.IComponentApplication
import com.mtjsoft.www.kotlinmvputils.utils.KLog

/**
 * 作为合并Module时，初始化自身的一些库
 */
class ModuleApp : IComponentApplication {

    companion object {
    }

    override fun init(application: Application) {
        KLog.e("mtj", "com.suntront.module_login.ModuleApp")
    }
}