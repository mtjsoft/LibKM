package cn.mtjsoft.libkotlinmvvm.test

import androidx.lifecycle.MutableLiveData
import com.mtjsoft.www.kotlinmvputils.base.BaseViewModel

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.test
 */

class TestDataViewModel : BaseViewModel() {

    val liveData = MutableLiveData<String>()
}