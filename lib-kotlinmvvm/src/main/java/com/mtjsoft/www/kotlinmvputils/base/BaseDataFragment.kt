package com.mtjsoft.www.kotlinmvputils.base

import android.view.View
import androidx.databinding.ViewDataBinding
import com.mtjsoft.www.kotlinmvputils.imp.LoadViewImp
import com.mtjsoft.www.kotlinmvputils.manager.AndLoadViewManager
import com.mtjsoft.www.kotlinmvputils.model.LoadState

/**
 *  BaseDataFragment
 * @author mtj
 */

abstract class BaseDataFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseFragment<V, VM>(),
    View.OnClickListener, LoadViewImp {

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
