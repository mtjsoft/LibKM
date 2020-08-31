package com.mtjsoft.www.kotlinmvputils.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
 * BaseFragment
 * @author mtj
 */

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseTopViewFragment() {

    lateinit var viewModel: VM
    lateinit var binding: V

    // 通过DataBinding添加布局
    override fun initaddView(): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, layoutResId(), baseCenterFrameLayout, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initBaseView() {
        initVM()
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        initView()
        initData()
    }

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    protected abstract fun initVariableId(): Int

    protected abstract fun layoutResId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun providerVMClass(): Class<VM>


    override fun onDestroy() {
        super.onDestroy()
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(viewModel)
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
                    val clz = it[BaseViewModel.ParameterField.CLASS] as Class<*>
                    val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle
                    val intent = Intent(context,clz)
                    activity?.startActivity(intent, bundle)
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
