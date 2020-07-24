package cn.mtjsoft.libkotlinmvvm.test

import android.os.Message
import android.view.View
import androidx.lifecycle.Observer
import cn.mtjsoft.libkotlinmvvm.R
import com.alibaba.android.arouter.launcher.ARouter
import com.mtjsoft.www.kotlinmvputils.base.BaseDataActivity
import com.mtjsoft.www.kotlinmvputils.model.LoadState

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.test
 * @date 2020-07-24 10:30:48
 */

class TestDataActivity : BaseDataActivity<TestDataViewModel>() {

    override fun layoutResId(): Int {
        return R.layout.activity_test
    }

    override fun providerVMClass(): Class<TestDataViewModel>? {
        return TestDataViewModel::class.java
    }

    override fun onPageLoad() {
        viewModel.loadData()
    }

    override fun initView(): Boolean {
        setPageTitle("主页")
        isShowBackView(false)

        // 监听数据变化
        viewModel.getNumberLiveData().observe(this, Observer {
            changeLoadState(LoadState.SUCCESS)
            toast("$it")
        })

        return true
    }

    override fun onClick(p0: View) {
        super.onClick(p0)
        ARouter.getInstance().build("/login/loginActivity").navigation()
    }

    override fun processHandlerMsg(msg: Message) {
    }
}