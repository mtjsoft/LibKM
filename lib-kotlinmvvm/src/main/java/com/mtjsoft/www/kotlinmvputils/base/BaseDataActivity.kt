package com.mtjsoft.www.kotlinmvputils.base

import androidx.databinding.ViewDataBinding
import com.mtjsoft.www.kotlinmvputils.imp.LoadViewImp
import com.mtjsoft.www.kotlinmvputils.manager.AndLoadViewManager
import com.mtjsoft.www.kotlinmvputils.model.LoadState


/**
 * BaseDataActivity
 * @author mtj
 */

abstract class BaseDataActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseActivity<V, VM>(),
    LoadViewImp {

    private lateinit var loadViewManager: AndLoadViewManager

    override fun initBaseView() {
        loadViewManager = AndLoadViewManager(this, getBaseCenterLayout(), this)
        changeLoadState(LoadState.LOADING)
        super.initBaseView()
    }

    override fun changeLoadState(state: LoadState) {
        loadViewManager.changeLoadState(state)
    }

    fun getLoadViewManager(): AndLoadViewManager = loadViewManager
}
