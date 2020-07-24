package com.mtjsoft.www.kotlinmvputils.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mtjsoft.www.kotlinmvputils.imp.LoadViewImp
import com.mtjsoft.www.kotlinmvputils.manager.AndLoadViewManager
import com.mtjsoft.www.kotlinmvputils.model.LoadState

/**
 *  BaseDataFragment
 * @author mtj
 */

abstract class BaseDataFragment<VM : BaseViewModel>: BaseTopViewFragment(), View.OnClickListener, LoadViewImp {

    lateinit var viewModel: VM

    override fun initBaseView() {
        initVM()
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        val boolean = initView()
        if (boolean) {
            changeLoadState(LoadState.LOADING)
        }
    }

    protected abstract fun initView(): Boolean

    protected abstract fun providerVMClass(): Class<VM>?


    private var loadViewManager: AndLoadViewManager? = null
    override fun changeLoadState(state: LoadState) {
        if (loadViewManager == null) {
            loadViewManager = AndLoadViewManager(this, getBaseCenterLayout(), this)
        }
        loadViewManager!!.changeLoadState(state)
    }

    override fun onDestroyView() {
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(viewModel)
        super.onDestroyView()
    }

    /**
     * 初始化VM
     */
    private fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this)[it]
        }
    }

    /**
     * =====================================================================
     */
    //注册ViewModel与View的契约UI回调事件
    private fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowLoadingEvent().observe(this, Observer {
            showLoadingUI(it[BaseViewModel.ParameterField.MSG].toString(),
                it[BaseViewModel.ParameterField.IS_CANCLE] as Boolean
            )
        })
        //加载对话框消失
        viewModel.getUC().getHideLoadingEvent().observe(this, Observer {
            hideLoadingUI()
        })
        //Toast显示
        viewModel.getUC().getShowToastEvent().observe(this, Observer {
            toast(it)
        })
        //跳入新页面
        viewModel.getUC().getStartActivityEvent()
            .observe(this, Observer {
                fun onChanged(@Nullable params: Map<String, Any>) {
                    val clz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                    val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle
                    val intent = Intent(context,clz)
                    activity?.startActivity(intent, bundle)
                }
            })
        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, Observer {
            activity?.finish()
        })
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, Observer {
            activity?.onBackPressed()
        })
    }

}
