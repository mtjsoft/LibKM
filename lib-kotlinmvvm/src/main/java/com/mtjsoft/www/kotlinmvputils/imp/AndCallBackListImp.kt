package com.mtjsoft.www.kotlinmvputils.imp

/**
 * Created by matengjiao on 2017/8/13.
 */

interface AndCallBackListImp<T> {
    fun onSuccess(data: List<T>)

    fun onError(data: String)
}
