package cn.mtjsoft.libkotlinmvvm.data

import android.os.Message
import cn.mtjsoft.libkotlinmvvm.BR
import cn.mtjsoft.libkotlinmvvm.R
import cn.mtjsoft.libkotlinmvvm.databinding.ActivityLodingBinding
import com.mtjsoft.www.kotlinmvputils.base.BaseDataActivity
import com.suntront.module_login.databinding.ActivityLoginBinding

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.data
 * @date 2020-08-31 16:58:27
 */

class LoadingTextActivity : BaseDataActivity<ActivityLodingBinding, LoadingTextViewModel>() {

    override fun layoutResId(): Int = R.layout.activity_loding

    override fun providerVMClass(): Class<LoadingTextViewModel> = LoadingTextViewModel::class.java

    override fun initVariableId(): Int = 0

    override fun onPageLoad() {
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun processHandlerMsg(msg: Message) {
    }
}