package com.mtjsoft.www.kotlinmvputils.imp

/**
 * Created by matengjiao on 2017/8/13.
 */

interface AndCallBackImp<T> {
    fun onSuccess(data: T)

    fun onError(data: T)
}
