package cn.mtjsoft.libkotlinmvvm.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mtjsoft.www.kotlinmvputils.base.BaseRecycleviewActivity
import com.mtjsoft.www.kotlinmvputils.base.BaseRecycleViewModel

import cn.mtjsoft.libkotlinmvvm.model.ListTestModel
import cn.mtjsoft.libkotlinmvvm.adapter.ListTestAdapter

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.list
 * @date 2020-09-01 10:10:12
 */

 class ListTestRecycleViewActivity : BaseRecycleviewActivity<ListTestModel, ListTestViewModel>(){

         override fun providerVMClass(): Class<ListTestViewModel> {
             return ListTestViewModel::class.java
         }

         override fun loadActivityInfo() {
             setPageTitle("RecycleView列表")
         }

         override fun setItemDecoration(): Int {
             return 10
         }

         override fun setPageSize(): Int {
             return 20
         }

         override fun setLayoutManagerType(): BaseRecycleViewModel.LayoutManager {
             return BaseRecycleViewModel.LayoutManager.LinearLayoutManager
         }

         override fun setCount(): Int {
             return 2
         }

         override fun instanceAdapter(list: MutableList<ListTestModel>): BaseQuickAdapter<ListTestModel, BaseViewHolder>{
             return ListTestAdapter(getContext(),list)
         }

 }