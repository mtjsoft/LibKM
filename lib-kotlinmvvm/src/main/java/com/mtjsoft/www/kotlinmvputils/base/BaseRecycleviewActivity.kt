package com.mtjsoft.www.kotlinmvputils.base

import android.graphics.Rect
import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.imp.AdapterItemClickListener
import com.mtjsoft.www.kotlinmvputils.imp.LoadViewImp
import com.mtjsoft.www.kotlinmvputils.manager.AndLoadViewManager
import com.mtjsoft.www.kotlinmvputils.model.LoadState
import com.mtjsoft.www.kotlinmvputils.utils.AndDensityUtils
import kotlinx.android.synthetic.main.hh_activity_recycleview.*

abstract class BaseRecycleviewActivity<T, VM : BaseRecycleViewModel<T>> : BaseTopViewActivity(),
    LoadViewImp, AdapterItemClickListener {

    lateinit var viewModel: VM
    lateinit var loadViewManager: AndLoadViewManager

    override fun initaddView(): View = inflateView(R.layout.hh_activity_recycleview)

    override fun initBaseView() {
        initVM()
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //加载状态
        loadViewManager = AndLoadViewManager(this, getBaseCenterLayout(), this)
        changeLoadState(LoadState.LOADING)
        // adapter
        val adapter = instanceAdapter(viewModel.list.value!!)
        recycler.adapter = adapter
        viewModel.adapter.value = adapter
        loadActivityInfo()
        initValues()
        initListeners()
    }

    override fun onPageLoad() {
        viewModel.getListData()
    }

    override fun changeLoadState(state: LoadState) {
        viewModel.loadState.postValue(state)
    }

    /**
     * 初始化VM
     */
    private fun initVM() {
        providerVMClass().let {
            viewModel = ViewModelProvider(this)[it]
        }
    }


    protected abstract fun providerVMClass(): Class<VM>

    /**
     * 加载Activity的数据之前，主要用于设置Activity的一些信息。如：标题是否显示、是否需要下拉功能等
     */
    protected abstract fun loadActivityInfo()

    /**
     * 设置item装饰间距
     *
     * @return
     */
    protected abstract fun setItemDecoration(): Int

    /**
     * 获取当前页每页获取的数据的大小
     *
     * @return
     */
    protected abstract fun setPageSize(): Int

    /**
     * 设置LayoutManager类型，默认2 【0：LinearLayoutManager ，1：GridLayoutManager，
     * 2：StaggeredGridLayoutManager 】设置1、2时，需用setCount（）方法，设置列数，默认2
     *
     * @return
     */
    protected abstract fun setLayoutManagerType(): BaseRecycleViewModel.LayoutManager

    /**
     * 设置每行列数，默认2
     */
    protected abstract fun setCount(): Int

    /**
     * 实例化一个Adapter
     *
     * @param list listView显示的数据的集合
     * @return
     */
    protected abstract fun instanceAdapter(list: MutableList<T>): BaseQuickAdapter<T, BaseViewHolder>

    private fun initValues() {
        // TODO Auto-generated method stub
        if (setCount() > 0) {
            viewModel.count.value = setCount()
        }
        viewModel.mark.value = setLayoutManagerType()
        viewModel.pager_size.value = setPageSize()
        when (setLayoutManagerType()) {
            BaseRecycleViewModel.LayoutManager.LinearLayoutManager -> recycler.layoutManager =
                LinearLayoutManager(this)
            BaseRecycleViewModel.LayoutManager.GridLayoutManager -> recycler.layoutManager =
                GridLayoutManager(this, setCount())
            BaseRecycleViewModel.LayoutManager.StaggeredGridLayoutManager -> recycler.layoutManager =
                StaggeredGridLayoutManager(
                    setCount(),
                    StaggeredGridLayoutManager.VERTICAL
                )
        }
        if (setItemDecoration() > 0) {
            val itemPad = AndDensityUtils.dip2px(getContext(), setItemDecoration())
            // 设置间隔
            recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    var posi = parent.getChildAdapterPosition(view)
                    if (viewModel.adapter.value!!.headerLayoutCount > 0 && posi < viewModel.adapter.value!!.headerLayoutCount) {
                        outRect.set(0, 0, 0, 0)
                    } else if (viewModel.adapter.value!!.footerLayoutCount > 0 && posi >= (viewModel.list.value!!.size + viewModel.adapter.value!!.headerLayoutCount)) {
                        outRect.set(0, 0, 0, 0)
                    } else {
                        posi -= viewModel.adapter.value!!.headerLayoutCount
                        if (viewModel.mark.value == BaseRecycleViewModel.LayoutManager.LinearLayoutManager) {
                            if (viewModel.list.value!!.size > 0 && posi == viewModel.list.value!!.size - 1) {
                                outRect.set(itemPad, itemPad, itemPad, itemPad)
                            } else {
                                outRect.set(itemPad, itemPad, itemPad, 0)
                            }
                        } else {
                            val isLastRaw =
                                isLastRaw(parent, posi, setCount(), viewModel.list.value!!.size)
                            if (isFirstColum(
                                    parent,
                                    posi,
                                    setCount(),
                                    viewModel.list.value!!.size
                                )
                            ) {
                                // 第一列
                                if (isLastRaw) {
                                    // 最后一行
                                    outRect.set(itemPad, itemPad, itemPad / 2, itemPad)
                                } else {
                                    outRect.set(itemPad, itemPad, itemPad / 2, 0)
                                }
                            } else if (isLastColum(
                                    parent,
                                    posi,
                                    setCount(),
                                    viewModel.list.value!!.size
                                )
                            ) {
                                // 最后一列
                                if (isLastRaw) {
                                    // 最后一行
                                    outRect.set(itemPad / 2, itemPad, itemPad, itemPad)
                                } else {
                                    outRect.set(itemPad / 2, itemPad, itemPad, 0)
                                }
                            } else {
                                // 中间列
                                if (isLastRaw) {
                                    outRect.set(itemPad / 2, itemPad, itemPad / 2, itemPad)
                                } else {
                                    outRect.set(itemPad / 2, itemPad, itemPad / 2, 0)
                                }
                            }
                        }
                    }
                }
            })
        }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // 屏幕停止滚动，加载图片
                        if (!isDestroyed) {
                            try {
                                Glide.with(getContext()).resumeRequests()
                            } catch (e: Exception) {
                            }
                        }
                    }
                    else -> {
                        // 停止加载图片
                        if (!isDestroyed) {
                            try {
                                Glide.with(getContext()).pauseRequests()
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            }
        })
    }

    /**
     * 滚动监听
     */
    private fun initListeners() {
        // TODO Auto-generated method stub
        if (viewModel.refresh.value!!) {
            //启用下拉刷新
            swipe_refresh.setEnableRefresh(true)
            swipe_refresh.setOnRefreshListener {
                viewModel.isSwipeRefresh.value = true
                viewModel.pager.value = 1
                viewModel.getListData()
            }
        } else {
            swipe_refresh.setEnableRefresh(false)
        }
        if (viewModel.load_more.value!!) {
            //启用上拉加载更多
            swipe_refresh.setEnableLoadMore(true)
            //设置是否在没有更多数据之后 Footer 跟随内容
            swipe_refresh.setEnableFooterFollowWhenNoMoreData(false)
            swipe_refresh.setOnLoadMoreListener {
                if (viewModel.adapter.value != null && viewModel.temp.value != null) {
                    if (viewModel.pager_size.value!! <= viewModel.temp.value!!.size) {
                        viewModel.isLoadMoreing.value = true
                        viewModel.pager.value = viewModel.pager.value!! + 1
                        viewModel.getListData()
                    } else {
                        swipe_refresh.finishLoadMore()
                        toast("抱歉，暂无更多数据")
                    }
                }
            }
        } else {
            //不启用上拉加载更多
            swipe_refresh.setEnableLoadMore(false)
        }
        // 监听适配器
        val helper = ItemTouchHelper(viewModel.itemTouchCallback)
        helper.attachToRecyclerView(recycler)
        // 监听获取数据的变化
        viewModel.temp.observe(this, Observer {
            if (viewModel.isSwipeRefresh.value!!) {
                swipe_refresh.finishRefresh()
                viewModel.isSwipeRefresh.value = false
            }
            if (viewModel.isLoadMoreing.value!!) {
                swipe_refresh.finishLoadMore()
                viewModel.isLoadMoreing.value = false
            }
            if (viewModel.temp.value == null) {
                return@Observer
            }
            if (viewModel.temp.value!!.size == 0) {
                if (viewModel.pager.value!! == 1) {
                    changeLoadState(LoadState.NODATA)
                    viewModel.adapter.value!!.notifyDataSetChanged()
                } else {
                    toast(getString(R.string.hh_no_data_more))
                }
            } else {
                changeLoadState(LoadState.SUCCESS)
                viewModel.adapter.value!!.notifyDataSetChanged()
            }
        })
        // 界面状态
        viewModel.loadState.observe(this, Observer {
            loadViewManager.changeLoadState(it)
        })
    }

    /**
     * 处理连续点击
     */
    override fun onItemClick(position: Int, view: View) {
        view.isEnabled = false
        val msg = getNewHandlerMessage()
        msg.what = ONE_CLICK
        msg.obj = view
        getHandler().sendMessageDelayed(msg, FILTER_TIMEM)
    }

    /**
     * 取出最大值
     *
     * @param positions
     * @return
     */
    private fun findMax(positions: IntArray): Int {
        var max = positions[0]
        for (value in positions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    override fun processHandlerMsg(msg: Message) {
    }

    /**
     * 是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isLastColum(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        var isLastColum = false
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)
            // 如果是最后一列，
            {
                isLastColum = true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)
                // 如果是最后一列，
                {
                    isLastColum = true
                }
            } else {
                childCount -= childCount % spanCount
                if (pos >= childCount) {
                    isLastColum = true
                }// 如果是最后一列
            }
        }
        return isLastColum
    }

    /**
     * 是否是第一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isFirstColum(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var isFirstColum = false
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 1) {// 如果是第一列，
                isFirstColum = true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 1) {// 如果是第一列，
                    isFirstColum = true
                }
            }
        }
        return isFirstColum
    }

    /**
     * 是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isLastRaw(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        val ys = childCount % spanCount
        var isLastRaw = false
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if (ys > 0) {
                isLastRaw = (pos >= childCount - ys)
            } else {
                isLastRaw = (pos >= childCount - spanCount)
            }
            return isLastRaw
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (ys > 0) {
                    isLastRaw = (pos >= childCount - ys)
                } else {
                    isLastRaw = (pos >= childCount - spanCount)
                }
                return isLastRaw
            }
        }
        return isLastRaw
    }

    /**
     * 列数
     *
     * @param parent
     * @return
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                .spanCount
        }
        return spanCount
    }


    /**
     * 返回recyclerView
     *
     * @return
     */
    fun getRecyclerView(): RecyclerView {
        return recycler
    }

    /**
     * 下载刷新的时候执行的代码
     */
    fun onRefresh() {
        viewModel.isSwipeRefresh.value = true
        viewModel.pager.value = 1
        viewModel.getListData()
    }

    override fun onDestroy() {
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    /**
     * =====================================================================
     */
    //注册ViewModel与View的契约UI回调事件
    private fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowLoadingEvent().observe(this, Observer {
            showLoadingUI(
                it[BaseViewModel.ParameterField.MSG].toString(),
                it[BaseViewModel.ParameterField.IS_CANCLE] as Boolean
            )
        })
        //加载对话框消失
        viewModel.getUC().getHideLoadingEvent().observe(this, Observer {
            hideLoadingUI()
        })
        //Toast显示
        viewModel.getUC().getShowToastEvent().observe(this, Observer {
            toast(it)
        })
        //跳入新页面
        viewModel.getUC().getStartActivityEvent()
            .observe(this, Observer {
                fun onChanged(@Nullable params: Map<String, Any>) {
                    val clz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                    val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle
                    startActivity(clz, bundle)
                }
            })
        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, Observer {
            finish()
        })
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, Observer {
            onBackPressed()
        })
    }
}