package cn.mtjsoft.libkotlinmvvm.test

import android.os.Message
import android.view.View
import cn.mtjsoft.libkotlinmvvm.BR
import cn.mtjsoft.libkotlinmvvm.R
import cn.mtjsoft.libkotlinmvvm.databinding.ActivityMainBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.mtjsoft.www.kotlinmvputils.base.BaseActivity

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.test
 * @date 2020-07-24 10:30:48
 */

class TestDataActivity : BaseActivity<ActivityMainBinding, TestDataViewModel>() {

    override fun initVariableId(): Int = BR.testViewModel

    override fun layoutResId(): Int = R.layout.activity_main

    override fun providerVMClass(): Class<TestDataViewModel> = TestDataViewModel::class.java

    override fun initView() {
        isShowBackView(false)
        setPageTitle("测试LibKM")
    }

    override fun initData() {
    }

    override fun onClick(p0: View) {
        super.onClick(p0)
        ARouter.getInstance().build("/login/loginActivity").navigation()
    }

    override fun processHandlerMsg(msg: Message) {
    }

}