package com.mtjsoft.www.kotlinmvputils.imp

import com.mtjsoft.www.kotlinmvputils.model.LoadState


interface LoadViewImp {
    /**
     * 调用页面加载的方法
     */
    fun onPageLoad()

    /**
     * 改变加载状态
     */
    fun changeLoadState(state: LoadState)
}
