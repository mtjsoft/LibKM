package com.mtjsoft.www.kotlinmvputils.imp

/**
 * 权限申请接口
 * Created by Administrator on 2017/8/23.
 */

interface PermissionsResultListener {
    fun onPermissionGranted()

    fun onPermissionDenied()
}
