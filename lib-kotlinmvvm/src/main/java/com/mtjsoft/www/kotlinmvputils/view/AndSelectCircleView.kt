package com.mtjsoft.www.kotlinmvputils.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.ViewPager
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.utils.AndDensityUtils
import com.mtjsoft.www.kotlinmvputils.utils.AndPagerTask
import java.security.InvalidParameterException




/**
 * 显示小圆点，表示当前选择的位置
 *
 * @author mtj
 */
@SuppressLint("Recycle")
class AndSelectCircleView : RadioGroup {
    //子控件显示的宽度
    private var mChildWidth = 0
    //子控件显示的高度
    private var mChildHeight = 0
    //两个子控件之间的间距
    private var mChildMargin = 0
    //正常情况下显示的颜色
    private var mNormalColor = DEFAULT_NORMAL_COLOR
    //选中的时候现实的颜色
    private var mSelectColor = DEFAULT_SELECT_COLOR
    //正常情况下显示的drawable
    private var mNormalDrawable: Drawable? = null
    //选中情况下显示的drawable
    private var mSelectDrawable: Drawable? = null
    //是否显示圆点
    private var mIsCircle = true
    //是否是自定义
    private var mIsCustom = false
    //自定义选中情况下显示的drawable
    private var mSelectCustomDrawable: Int? = 0
    //是否自动切换viewpager，默认false
    private var viewPager: ViewPager? = null
    private var pagerTask: AndPagerTask? = null
    //
    private var pointCheckedChangeListener: PointCheckedChangeListener? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        orientation = LinearLayout.HORIZONTAL
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AndSelectCircleView)
        mChildWidth = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_width, AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE))
        mChildHeight = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_height, AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE))
        mChildMargin = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_margin, AndDensityUtils.dip2px(context, DEFAULT_CHILD_MARGIN))
        mIsCircle = attributes.getBoolean(R.styleable.AndSelectCircleView_is_circle, true)
        mIsCustom = attributes.getBoolean(R.styleable.AndSelectCircleView_is_custom, false)
        mNormalColor = attributes.getColor(R.styleable.AndSelectCircleView_normal_color, DEFAULT_NORMAL_COLOR)
        mSelectColor = attributes.getColor(R.styleable.AndSelectCircleView_select_color, DEFAULT_SELECT_COLOR)
        mSelectCustomDrawable = attributes.getInt(R.styleable.AndSelectCircleView_custom_drawable, R.drawable.hh_selector_custom)
        attributes.recycle()
        mNormalDrawable = getSpecialDrawable(true)
        mSelectDrawable = getSpecialDrawable(false)
    }

    private fun getSpecialDrawable(isNormal: Boolean): Drawable {
        if (mIsCircle) {
            val width = Math.min(mChildHeight, mChildWidth)
            val create = RoundedBitmapDrawableFactory.create(resources, getCircleDrawableBitmap(isNormal, width))
            create.isCircular = true
            return create
        } else {
            val drawable = ColorDrawable(if (isNormal) mNormalColor else mSelectColor)
            drawable.setBounds(0, 0, mChildWidth, mChildHeight)
            return drawable
        }
    }

    private fun getCircleDrawableBitmap(isNormal: Boolean, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, width, Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(if (isNormal) mNormalColor else mSelectColor)
        return bitmap
    }

    constructor(context: Context) : super(context) {
        orientation = LinearLayout.HORIZONTAL
        initView()
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        mChildHeight = AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE)
        mChildWidth = AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE)
        mChildMargin = AndDensityUtils.dip2px(context, DEFAULT_CHILD_MARGIN)
        mNormalDrawable = getSpecialDrawable(true)
        mSelectDrawable = getSpecialDrawable(false)
    }

    /**
     * 设置是否自定义
     */
    fun setIsCustom(b: Boolean) {
        mIsCustom = b
    }

    /**
     * 添加子View
     *
     * @param count 添加的数量
     */
    fun addChild(count: Int) {
        clear()
        if (count < 1) {
            throw InvalidParameterException("count must be biger than 0")
        }
        mNormalDrawable = getSpecialDrawable(true)
        mSelectDrawable = getSpecialDrawable(false)
        for (index in 0 until count) {
            val button = RadioButton(context)
            button.id = index
            val drawable = StateListDrawable()
            drawable.addState(intArrayOf(android.R.attr.state_checked), mSelectDrawable)
            drawable.addState(intArrayOf(), mNormalDrawable)
            button.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
            button.setBackgroundDrawable(drawable)
            val params = RadioGroup.LayoutParams(mChildWidth, mChildHeight)
            if (index == 0) {
                button.isChecked = true
                params.leftMargin = 0
            } else {
                button.isChecked = false
                params.leftMargin = mChildMargin
            }
            addView(button, index, params)
        }
        setOnCheckedChangeListener { group, checkedId ->
            if (pointCheckedChangeListener != null) {
                pointCheckedChangeListener!!.checkedChange(checkedId)
            }
        }
    }

    /**
     * 设置选中的位置
     *
     * @param position
     */
    fun setSelectPosition(position: Int) {
        if (childCount > 0 && position < childCount) {
            val button = getChildAt(position) as RadioButton
            button.isChecked = true
        }
    }

    interface PointCheckedChangeListener {
        fun checkedChange(position: Int)
    }

    /**
     * 设置自动切换
     *
     * @param automaticTime 自动切换时间
     */
    fun setAutomatic(automaticTime: Int) {
        if (viewPager != null && viewPager!!.adapter?.count!! > 1) {
            pagerTask = AndPagerTask(automaticTime, viewPager!!.adapter!!.count, viewPager)
            pagerTask!!.startChange()
        }
    }

    /**
     * 清除所有
     */
    fun clear() {
        removeAllViews()
    }

    fun setPointCheckedChangeListener(pointCheckedChangeListener: PointCheckedChangeListener): AndSelectCircleView {
        this.pointCheckedChangeListener = pointCheckedChangeListener
        return this
    }

    companion object {
        private val tag = AndSelectCircleView::class.java.simpleName
        //默认的子控件的大小，单位是dp
        private val DEAFULT_CHILD_SIZE = 8
        //默认情况下子控件之间的间距，单位是dp
        private val DEFAULT_CHILD_MARGIN = 8
        //默认显示的颜色值
        private val DEFAULT_NORMAL_COLOR = Color.WHITE
        //默认选中显示的颜色值
        private val DEFAULT_SELECT_COLOR = Color.GRAY
    }
}
