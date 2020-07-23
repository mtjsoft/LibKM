package com.mtjsoft.www.kotlinmvputils.base

import android.view.View


/**
 * BaseNotActivity 不需要接口的简单页面
 * @author mtj
 */

abstract class BaseNotActivity: BaseTopViewActivity(), View.OnClickListener {

    override fun initaddView(): View  = inflateView(layoutResId())

    override fun initBaseView() {
        initView()
        initData()
    }

    protected abstract fun layoutResId(): Int

    protected abstract fun initView()

    protected abstract fun initData()
}
