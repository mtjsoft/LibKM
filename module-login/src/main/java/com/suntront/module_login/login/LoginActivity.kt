package com.suntront.module_login.login

import android.Manifest
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.mtjsoft.www.kotlinmvputils.base.BaseActivity
import com.mtjsoft.www.kotlinmvputils.constants.RouterConstants
import com.mtjsoft.www.kotlinmvputils.imp.PermissionsResultListener
import com.suntront.module_login.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页
 * @author mtj
 * @Package com.suntront.module_login.login
 * @date 2020-05-25 15:40:04
 */

@Route(path = RouterConstants.LOGIN_ACTIVITY)
class LoginActivity : BaseActivity<LoginViewModel>() {

    override fun layoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        // 沉浸式状态栏
        ImmersionBar.with(this).init()
        removeAllBaseTopLayout()
        login_name_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.setName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        login_password_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.setPsw(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun initData() {
        requestPermission(
            arrayListOf(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE
            ),
            true,
            object : PermissionsResultListener {
                override fun onPermissionGranted() {
                }

                override fun onPermissionDenied() {
                }
            })
    }

    override fun onClick(p0: View) {
        super.onClick(p0)
        when (p0.id) {
            R.id.login_submit_button -> {
                // 登录
                viewModel.login()
            }
        }
    }

    override fun providerVMClass(): Class<LoginViewModel>? {
        return LoginViewModel::class.java
    }

    override fun processHandlerMsg(msg: Message) {
    }
}