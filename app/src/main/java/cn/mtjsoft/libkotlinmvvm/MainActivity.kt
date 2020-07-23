package cn.mtjsoft.libkotlinmvvm

import android.os.Message
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.mtjsoft.www.kotlinmvputils.base.BaseNotActivity
import com.mtjsoft.www.kotlinmvputils.constants.RouterConstants

class MainActivity : BaseNotActivity() {
    override fun layoutResId(): Int = R.layout.activity_main

    override fun initView() {
        isShowBackView(false)
        setPageTitle(getString(R.string.app_name))
    }

    override fun initData() {
    }

    override fun onClick(p0: View) {
        super.onClick(p0)
        when (p0.id) {
            R.id.btn_login -> {
                ARouter.getInstance().build(RouterConstants.LOGIN_ACTIVITY).navigation()
            }
        }
    }

    override fun processHandlerMsg(msg: Message) {
    }

}