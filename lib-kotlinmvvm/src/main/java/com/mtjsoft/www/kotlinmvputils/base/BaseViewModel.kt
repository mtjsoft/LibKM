package com.mtjsoft.www.kotlinmvputils.base

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.mtjsoft.www.kotlinmvputils.event.SingleLiveEvent
import com.trello.rxlifecycle2.LifecycleProvider
import java.lang.ref.WeakReference

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private var uc: UIChangeLiveData? = null

    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    fun getLifecycleProvider(): LifecycleProvider<*>? {
        return lifecycle?.get()
    }


    fun getUC(): UIChangeLiveData {
        if (uc == null) {
            uc = UIChangeLiveData()
        }
        return uc!!
    }

    /**
     * 显示loading
     */
    fun showLoadingUI(msg: String = "", isCan: Boolean = false) {
        val params = HashMap<String, Any>()
        params[ParameterField.MSG] = msg
        params[ParameterField.IS_CANCLE] = isCan
        getUC().getShowLoadingEvent().postValue(params)
    }

    /**
     * 隐藏loading
     */
    fun hideLoadingUI(){
        getUC().getHideLoadingEvent().call()
    }

    /**
     * 显示TOAST
     */
    fun showToastUI(msg: String = ""){
        getUC().getShowToastEvent().postValue(msg)
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(clz, null)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        getUC().getStartActivityEvent().postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finishUI(){
        getUC().getFinishEvent().call()
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        getUC().getOnBackPressedEvent().call()
    }

    /**
     * ViewModel与View的契约UI回调事件
     */
    class UIChangeLiveData {
        private var showLoadingEvent: SingleLiveEvent<Map<String, Any>>? = null
        private var hideLoadingEvent: SingleLiveEvent<Void>? = null
        private var showToastEvent: SingleLiveEvent<String>? = null
        private var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
        private var finishEvent: SingleLiveEvent<Void>? = null
        private var onBackPressedEvent: SingleLiveEvent<Void>? = null

        fun getShowLoadingEvent(): SingleLiveEvent<Map<String, Any>> {
            if (showLoadingEvent == null) {
                showLoadingEvent = SingleLiveEvent()
            }
            return showLoadingEvent!!
        }

        fun getHideLoadingEvent(): SingleLiveEvent<Void> {
            if (hideLoadingEvent == null) {
                hideLoadingEvent = SingleLiveEvent()
            }
            return hideLoadingEvent!!
        }

        fun getShowToastEvent(): SingleLiveEvent<String> {
            if (showToastEvent == null) {
                showToastEvent = SingleLiveEvent()
            }
            return showToastEvent!!
        }

        fun getStartActivityEvent(): SingleLiveEvent<Map<String, Any>> {
            if (startActivityEvent == null) {
                startActivityEvent = SingleLiveEvent()
            }
            return startActivityEvent!!
        }

        fun getFinishEvent(): SingleLiveEvent<Void> {
            if (finishEvent == null) {
                finishEvent = SingleLiveEvent()
            }
            return finishEvent!!
        }

        fun getOnBackPressedEvent(): SingleLiveEvent<Void> {
            if (onBackPressedEvent == null) {
                onBackPressedEvent = SingleLiveEvent()
            }
            return onBackPressedEvent!!
        }
    }

    /**
     * 判断空
     */
    fun isEmpty(list: Any): Boolean {
        return CommonUtils.isEmpty(list)
    }

    object ParameterField {
        var CLASS = "CLASS"
        var CANONICAL_NAME = "CANONICAL_NAME"
        var BUNDLE = "BUNDLE"
        var MSG = "MSG"
        var IS_CANCLE = "IS_CANCLE"
    }
}