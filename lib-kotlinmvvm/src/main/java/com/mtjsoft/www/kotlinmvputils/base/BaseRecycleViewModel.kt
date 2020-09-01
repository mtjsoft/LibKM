package com.mtjsoft.www.kotlinmvputils.base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mtjsoft.www.kotlinmvputils.event.SingleLiveEvent
import com.mtjsoft.www.kotlinmvputils.imp.AndCallBackListImp
import com.mtjsoft.www.kotlinmvputils.imp.ItemTouchCallback
import com.mtjsoft.www.kotlinmvputils.imp.ItemTouchHelpAdapter
import com.mtjsoft.www.kotlinmvputils.model.LoadState
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseRecycleViewModel<T> : BaseViewModel(), ItemTouchHelpAdapter {

    // 适配器
    val adapter: SingleLiveEvent<BaseQuickAdapter<T, BaseViewHolder>> = SingleLiveEvent()

    // 布局类型
    enum class LayoutManager {
        LinearLayoutManager,
        GridLayoutManager,
        StaggeredGridLayoutManager;
    }

    // 当选择 网格布局 和 流式布局，设置每行数据有几列
    val count: SingleLiveEvent<Int> = SingleLiveEvent()

    // 选择的布局类型
    val mark: SingleLiveEvent<LayoutManager> = SingleLiveEvent()

    // 分页加载时，页码
    val pager: SingleLiveEvent<Int> = SingleLiveEvent()

    // 分页加载时，每页数据大小
    val pager_size: SingleLiveEvent<Int> = SingleLiveEvent()

    // 是否是下拉刷新
    val isSwipeRefresh: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 是否是上拉加载
    val isLoadMoreing: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 总数据
    val list: SingleLiveEvent<MutableList<T>> = SingleLiveEvent()

    // 获取的一页数据
    val temp: SingleLiveEvent<MutableList<T>> = SingleLiveEvent()

    // 是否开启上拉加载
    val load_more: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 是否开启下拉刷新
    val refresh: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 是否拖拽排序
    val isDrag: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 是否滑动删除
    val isSwipe: SingleLiveEvent<Boolean> = SingleLiveEvent()

    // 拖拽排序、滑动删除 监听
    val itemTouchCallback = ItemTouchCallback()

    val loadState: SingleLiveEvent<LoadState> = SingleLiveEvent()

    // 初始化默认参数
    init {
        count.value = 2
        mark.value = LayoutManager.LinearLayoutManager
        pager.value = 1
        pager_size.value = 30
        isSwipeRefresh.value = false
        isLoadMoreing.value = false
        load_more.value = true
        refresh.value = true
        isDrag.value = false
        isSwipe.value = false
        list.value = ArrayList()
    }

    /**
     * 刷新
     */
    fun onRefresh() {
        isSwipeRefresh.value = true
        pager.value = 1
        getListData()
    }

    fun notifyDataSetChanged() {
        if (adapter.value != null) {
            adapter.value!!.notifyDataSetChanged()
        }
    }

    override fun itemMove(fromPosition: Int, toPosition: Int) {
        if (adapter.value != null && list.value != null) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(list.value!!, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(list.value!!, i, i - 1)
                }
            }
            adapter.value!!.notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun itemDelete(position: Int) {
        if (adapter.value != null && list.value != null && list.value!!.size > position) {
            list.value!!.removeAt(position)
            adapter.value!!.notifyItemRemoved(position)
            adapter.value!!.notifyItemRangeChanged(position, list.value!!.size - position)
        }
    }

    fun getListData() {
        getListDataInThread(pager.value!!, object : AndCallBackListImp<T> {
            override fun onSuccess(data: List<T>) {
                if (pager.value == 1) {
                    list.value!!.clear()
                }
                temp.value = data.toMutableList()
                list.value!!.addAll(temp.value!!)
                //
                itemTouchCallback.setData(
                    isDrag.value!!,
                    isSwipe.value!!,
                    this@BaseRecycleViewModel
                )
            }

            override fun onError(data: String) {
                if (pager.value == 1) {
                    loadState.postValue(LoadState.FAILED)
                } else {
                    showToastUI(data)
                }
                temp.call()
            }
        })
    }

    protected abstract fun getListDataInThread(pager: Int, callBackListImp: AndCallBackListImp<T>)
}