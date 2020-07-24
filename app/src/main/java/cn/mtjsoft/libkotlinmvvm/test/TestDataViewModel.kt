package cn.mtjsoft.libkotlinmvvm.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.mtjsoft.www.kotlinmvputils.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.test
 * @date 2020-07-24 10:30:48
 */

class TestDataViewModel : BaseViewModel() {
    private val numberLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    private fun getNumber(): Int {
        return numberLiveData.value ?: 0
    }

    fun getNumberLiveData(): MutableLiveData<Int> = numberLiveData

    // 模拟请求
    fun loadData() {
        rxLifeScope.launch {
            async {
                delay(3000)
            }.await()
            numberLiveData.value = getNumber() + 1
        }
    }
}