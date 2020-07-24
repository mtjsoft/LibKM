package com.mtjsoft.www.kotlinmvputils.manager

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.imp.LoadViewImp
import com.mtjsoft.www.kotlinmvputils.model.LoadState
import com.mtjsoft.www.kotlinmvputils.model.LoadViewInfo
import com.wang.avi.AVLoadingIndicatorView
import java.util.*

/**
 * 控制页面加载状态
 *
 * @author mtj
 */
class AndLoadViewManager(
    lifecycleOwner: LifecycleOwner,
    var baseView: FrameLayout,
    var mLoadViewImp: LoadViewImp
) {
    private var mLoadingLayout: RelativeLayout
    private var isAdded: Boolean = false
    private var animationDrawable: AnimationDrawable? = null

    // 正在加载，加载失败和没有数据的View
    private var mLoaddingView: View

    // mLoaddingView中显示图片的View
    private var mLoaddingImageView: ImageView
    private var avi: AVLoadingIndicatorView

    // mLoaddingView中显示文字的View
    private var mLoadContentTextView: TextView

    //控制各个状态下显示的图片和文字
    private val mLoadViewInfoMap = HashMap<LoadState, LoadViewInfo>()

    private var animationOut: Animation

    init {
        isAdded = false
        //实例化加载的页面
        mLoaddingView = View.inflate(baseView.context, R.layout.include_loading_layout, null)
        mLoaddingImageView = mLoaddingView.findViewById(R.id.iv_loading_status) as ImageView
        avi = mLoaddingView.findViewById(R.id.avi) as AVLoadingIndicatorView
        mLoadContentTextView = mLoaddingView.findViewById(R.id.tv_loading_msg) as TextView
        mLoadingLayout = mLoaddingView.findViewById(R.id.ll_loading_layout) as RelativeLayout
        animationOut = AnimationUtils.loadAnimation(baseView.context, R.anim.anim_alpha_out)

        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event
            ) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    animationOut.cancel()
                    if (animationDrawable != null && animationDrawable!!.isRunning) {
                        animationDrawable!!.stop()
                        animationDrawable = null
                    }
                }
            }
        })
    }

    /**
     * 改变当前的加载状态<br></br>
     * 如果不需要修改显示的图片资源则drawableID传0，如果不需要修改显示状态的文本，stateMsg传null
     *
     * @param state 当前的加载状态
     */
    fun changeLoadState(state: LoadState) {
        //还原用户原来设置的点击事件
        bindListener(state)
        when (state) {
            LoadState.LOADING -> {
                avi.visibility = View.VISIBLE
                avi.show()
                changeTipViewInfo(state)
                mLoadingLayout.visibility = View.VISIBLE
//                if (animationDrawable == null) {
//                    animationDrawable = mLoaddingImageView.background as AnimationDrawable
//                    animationDrawable!!.start()
//                }
                mLoadViewImp.onPageLoad()
            }
            LoadState.FAILED, LoadState.NODATA -> {
                if (animationDrawable != null && animationDrawable!!.isRunning) {
                    animationDrawable!!.stop()
                    animationDrawable = null
                }
                avi.hide()
                avi.visibility = View.GONE
                changeTipViewInfo(state)
            }
            LoadState.SUCCESS -> {
                //从页面中移除掉
                if (isAdded) {
                    animationOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            if (animationDrawable != null && animationDrawable!!.isRunning) {
                                animationDrawable!!.stop()
                                animationDrawable = null
                            }
                            avi.visibility = View.GONE
                            avi.hide()
                            val count = baseView.childCount
                            if (count > 0) {
                                baseView.removeViewAt(count - 1)
                                isAdded = false
                            }
                        }

                        override fun onAnimationStart(animation: Animation?) {
                        }
                    })
                    mLoadingLayout.startAnimation(animationOut)
                }
            }
        }
    }

    /**
     * 设置加载的View和提示的View显示的提示的图片和文本
     *
     * @param state      加载的状态
     * @param drawableID 显示的图片
     * @param stateMsg   显示的文本
     */
    fun setLoaddingViewInfo(state: LoadState, drawableID: Int, stateMsg: String) {
        //在Map查找对应状态保存的加载视图的信息，如果查找到的信息为null，则需要重新创建一个对象，并且把该对象
        //保存到Map集合中，如果该对象存在的话，则需要重新设置该对象的信息
        var loadViewInfo: LoadViewInfo? = mLoadViewInfoMap[state]
        if (loadViewInfo == null) {
            loadViewInfo = LoadViewInfo(stateMsg, drawableID)
            mLoadViewInfoMap[state] = loadViewInfo
        } else {
            loadViewInfo.drawableID = drawableID
            loadViewInfo.msgInfo = stateMsg
        }
    }

    /**
     * 绑定监听器
     *
     * @param state 加载的状态
     */
    private fun bindListener(state: LoadState) {
        when (state) {
            //当当前的状态是长在加载或者加载成功的时候，取消所有控件的点击事件
            LoadState.LOADING, LoadState.SUCCESS -> {
                mLoaddingView.setOnClickListener(null)
                mLoaddingImageView.setOnClickListener(null)
            }
            LoadState.FAILED -> {
                //当获取数据失败的时候，点击重新加载数据
                val listener = LoadClickListener()
                mLoaddingView.setOnClickListener(listener)
                mLoaddingImageView.setOnClickListener(listener)
            }
            LoadState.NODATA -> {
                //暂无数据时，点击重新获取
                val listener = LoadClickListener()
                mLoaddingView.setOnClickListener(listener)
                mLoaddingImageView.setOnClickListener(listener)
            }
        }
    }

    /**
     * 设置提示的视图显示的内容
     */
    private fun changeTipViewInfo(state: LoadState) {
        //首先需要停止当前动画效果
        val hhLoadViewInfo = mLoadViewInfoMap[state]
        //定义变量，保存显示的图片资源和显示的文本信息
        var drawableID = -1
        var msg = ""
        //用户没有为单独的页面设置显示的图片和现实的文本
        if (hhLoadViewInfo == null) {
            //设置默认的图片和文本
            when (state) {
                LoadState.FAILED -> {
                    mLoaddingImageView.visibility = View.VISIBLE
                    drawableID = R.drawable.loadding_error
                    msg = getString(R.string.hh_load_failed_click)
                }
                LoadState.NODATA -> {
                    mLoaddingImageView.visibility = View.VISIBLE
                    drawableID = R.drawable.loadding_no_data
                    msg = getString(R.string.hh_no_data)
                }
                LoadState.LOADING -> {
                    mLoaddingImageView.visibility = View.GONE
//                    drawableID = R.drawable.hh_loadding_anim
                    msg = getString(R.string.hh_loading)
                }
                LoadState.SUCCESS -> {
                }
            }
            //设置控件显示的图片和文本
            if (drawableID != -1) {
                mLoaddingImageView.setImageResource(drawableID)
            }
            mLoadContentTextView.text = msg
        } else {
            //用户为单独的页面设置了显示的文本和显示的图片
            avi.hide()
            avi.visibility = View.GONE
            mLoaddingImageView.visibility = View.VISIBLE
            mLoaddingImageView.setImageResource(hhLoadViewInfo.drawableID)
            mLoadContentTextView.text = hhLoadViewInfo.msgInfo
        }
        //加载的视图添加到页面中
        if (!isAdded) {
            baseView.addView(mLoadingLayout, baseView.childCount)
            isAdded = true
        }
    }

    /**
     * 根据ID获取字符串
     *
     * @param resID 资源的ID
     * @return
     */
    private fun getString(resID: Int): String {
        return baseView.context.getString(resID)
    }

    /**
     * 加载失败的时候点击执行的默认的监听器
     *
     * @author mtj
     */
    private inner class LoadClickListener : OnClickListener {
        override fun onClick(v: View) {
            changeLoadState(LoadState.LOADING)
        }
    }

}
