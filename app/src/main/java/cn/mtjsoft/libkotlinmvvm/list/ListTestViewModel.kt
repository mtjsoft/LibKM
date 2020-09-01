package cn.mtjsoft.libkotlinmvvm.list

import androidx.lifecycle.rxLifeScope
import com.mtjsoft.www.kotlinmvputils.base.BaseRecycleViewModel
import com.mtjsoft.www.kotlinmvputils.imp.AndCallBackListImp
import cn.mtjsoft.libkotlinmvvm.model.ListTestModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.ArrayList

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.list
 * @date 2020-09-01 10:10:12
 */

class ListTestViewModel : BaseRecycleViewModel<ListTestModel>() {
    /**
     * 分页获取列表数据
     */
    override fun getListDataInThread(
        pager: Int,
        callBackListImp: AndCallBackListImp<ListTestModel>
    ) {
        // 开启协程，模拟请求
        rxLifeScope.launch {
            val list: MutableList<ListTestModel> = ArrayList()
            async {
                for (i in 0 until 20) {
                    val model = ListTestModel()
                    model.numSrt = i.toString()
                    list.add(model)
                }
                // 延迟2秒
                delay(2000)
            }.await()
            showToastUI("请求成功")
            callBackListImp.onSuccess(list)
        }
    }
}