package com.mtjsoft.www.kotlinmvputils.imp

import android.app.Application

/**
 * 利用反射初始化 Module Application
 *
 * 不能混淆
 */
interface IComponentApplication {
    fun init(application: Application)
}