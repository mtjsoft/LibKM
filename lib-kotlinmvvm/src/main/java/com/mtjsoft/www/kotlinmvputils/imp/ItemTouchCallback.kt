package com.mtjsoft.www.kotlinmvputils.imp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * recyclerview 的拖拽回调
 */
class ItemTouchCallback : ItemTouchHelper.Callback() {

    private var isDrag: Boolean = false
    private var isSwipe: Boolean = false
    private var itemTouchHelpAdapter: ItemTouchHelpAdapter? = null

    fun setData(
        isDrag: Boolean,
        isSwipe: Boolean,
        itemTouchHelpAdapter: ItemTouchHelpAdapter?
    ) {
        this.isDrag = isDrag
        this.isSwipe = isSwipe
        this.itemTouchHelpAdapter = itemTouchHelpAdapter
    }

    /**
     * 判断动作方向
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val dragFlags =
            if (recyclerView.layoutManager is GridLayoutManager || recyclerView.layoutManager is StaggeredGridLayoutManager) {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        return makeMovementFlags(if (isDrag) dragFlags else 0, if (isSwipe) swipeFlags else 0)
    }

    /**
     * 针对drag状态，当前target对应的item是否允许移动
     * 我们一般用drag来做一些换位置的操作，就是当前对应的target对应的Item可以移动
     */
    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return isDrag
    }

    /**
     * 拖拽回调
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.adapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.adapterPosition
        if (itemTouchHelpAdapter != null) {
            itemTouchHelpAdapter!!.itemMove(fromPosition, toPosition)
        }
        return true
    }

    /**
     * 针对swipe状态，是否允许swipe(滑动)操作
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipe
    }

    /**
     * 侧滑回调
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //侧滑删除可以使用；
        //得到当拖拽的viewHolder的Position
        val position = viewHolder.adapterPosition
        if (itemTouchHelpAdapter != null) {
            itemTouchHelpAdapter!!.itemDelete(position)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * 长按选中Item的时候开始调用
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null && actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (isDrag || isSwipe) {
                val animatorSet = AnimatorSet()
                val animatorScaleX =
                    ObjectAnimator.ofFloat(viewHolder.itemView, "ScaleX", 1.0f, 0.85f)
                val animatorScaleY =
                    ObjectAnimator.ofFloat(viewHolder.itemView, "ScaleY", 1.0f, 0.85f)
                animatorSet.playTogether(animatorScaleX, animatorScaleY)
                animatorSet.duration = 200L
                animatorSet.start()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 手指松开的时候
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (isDrag || isSwipe) {
            val animatorSet = AnimatorSet()
            val animatorScaleX =
                ObjectAnimator.ofFloat(viewHolder.itemView, "ScaleX", 0.85f, 1.0f)
            val animatorScaleY =
                ObjectAnimator.ofFloat(viewHolder.itemView, "ScaleY", 0.85f, 1.0f)
            animatorSet.playTogether(animatorScaleX, animatorScaleY)
            animatorSet.duration = 100L
            animatorSet.start()
        }
        super.clearView(recyclerView, viewHolder)
    }
}