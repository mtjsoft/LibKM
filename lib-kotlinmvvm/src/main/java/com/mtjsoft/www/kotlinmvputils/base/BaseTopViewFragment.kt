package com.mtjsoft.www.kotlinmvputils.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.manager.AndBaseTopViewInfo
import com.mtjsoft.www.kotlinmvputils.model.EventMessage
import com.mtjsoft.www.kotlinmvputils.utils.AndEventBusUtils
import com.mtjsoft.www.kotlinmvputils.utils.HHTipUtils
import com.mtjsoft.www.kotlinmvputils.utils.HHWeakHandler
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.base_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

/**
 *  BaseTopViewFragment
 *  Fragment顶层基类
 * @author mtj
 */

abstract class BaseTopViewFragment : RxFragment(), View.OnClickListener {
    // 拦截所有两次点击时间间隔小于一秒的点击事件
    val FILTER_TIMEM = 1000L
    var ONE_CLICK = 919192

    // 跟布局
    lateinit var baseLayout: View
    lateinit var baseRelativeLayout: RelativeLayout
    lateinit var baseTopLinearLayout: LinearLayout
    lateinit var baseCenterFrameLayout: FrameLayout
    lateinit var baseBottomLinearLayout: LinearLayout

    private lateinit var topView: View
    private lateinit var toolbar: Toolbar
    private lateinit var textTitle: TextView
    private var isShowBackView: Boolean = true
    private var isShowTopView: Boolean = true

    /**
     * 是否注册EventBus
     */
    private var isRegisteredEventBus = false

    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val INVISIBLE = View.INVISIBLE


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        onSetUserVisibleHint(isVisibleToUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        ARouter.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseLayout = View.inflate(context, R.layout.base_layout, null)
        baseRelativeLayout = baseLayout.findViewById(R.id.base_layout)
        baseTopLinearLayout = baseLayout.findViewById(R.id.top_layout)
        baseCenterFrameLayout = baseLayout.findViewById(R.id.center_layout)
        baseBottomLinearLayout = baseLayout.findViewById(R.id.bottom_layout)
        return baseLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topView = View.inflate(context, R.layout.include_toolbar, null)
        toolbar = topView.findViewById(R.id.toolbar) as Toolbar
        textTitle = topView.findViewById(R.id.tv_title) as TextView
        getBaseCenterLayout().addView(initaddView(), 0)
        initBaseView()
        if (isShowTopView) {
            initTopTitle()
        }
        if (isRegisteredEventBus) {
            AndEventBusUtils.register(this)
        }
    }

    private fun initTopTitle() {
        val andBaseTopViewInfo = AndBaseTopViewInfo
        if (andBaseTopViewInfo.backgroundColor == -1) {
            toolbar.setBackgroundColor(Color.RED)
        } else {
            toolbar.setBackgroundColor(getColor_(andBaseTopViewInfo.backgroundColor))
        }
        if (AndBaseTopViewInfo.titleMode == AndBaseTopViewInfo.TitleMode.LEFT) {
            toolbar.setTitleTextColor(andBaseTopViewInfo.titleTextColor)
            //toolbar字体大小在stytle里面调整
        } else {
            textTitle.setTextColor(andBaseTopViewInfo.titleTextColor)
            textTitle.textSize = (andBaseTopViewInfo.titleSize).toFloat()
        }
        setHasOptionsMenu(true)
        val appCompatActivity: AppCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(isShowBackView)
        appCompatActivity.supportActionBar!!.setHomeAsUpIndicator(andBaseTopViewInfo.backLeftDrawable)
        appCompatActivity.supportActionBar!!.setDisplayShowTitleEnabled(AndBaseTopViewInfo.titleMode == AndBaseTopViewInfo.TitleMode.LEFT)
        getBaseTopLayout().addView(topView, 0)
    }

    fun onSetUserVisibleHint(isVisibleToUser: Boolean) {
    }

    protected abstract fun initaddView(): View

    protected abstract fun initBaseView()

    protected abstract fun processHandlerMsg(msg: Message)

    override fun getContext(): Activity = activity!!

    /**
     * 接收到分发的事件
     * 在主线程中处理
     * （注册接收，子类覆写此方法）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onReceiveEvent(event: EventMessage) {
    }

    /**
     * 接受到分发的粘性事件
     * 在主线程中处理
     * （注册接收，子类覆写此方法）
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onReceiveStickyEvent(event: EventMessage) {
    }

    fun setIsRegisteredEventBus(isRegisteredEventBus: Boolean) {
        this.isRegisteredEventBus = isRegisteredEventBus
    }

    /**
     * 处理消息的handler
     */
    private val mHandler = @SuppressLint("HandlerLeak")
    object : HHWeakHandler<Fragment>(this) {
        override fun processHandlerMessage(msg: Message) {
            if (msg.what == ONE_CLICK) {
                val view: View = msg.obj as View
                view.isEnabled = true
            }
            processHandlerMsg(msg)
        }
    }

    fun getHandler(): Handler {
        return mHandler
    }

    /**
     * 获取一个Message对象
     *
     * @return
     */
    protected fun getNewHandlerMessage(): Message {
        return mHandler.obtainMessage()
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    protected fun sendHandlerMessage(msg: Message) {
        mHandler.sendMessage(msg)
    }

    /**
     * 发送消息
     *
     * @param what
     */
    protected fun sendHandlerMessage(what: Int) {
        val msg = getNewHandlerMessage()
        msg.what = what
        sendHandlerMessage(msg)
    }

    fun onError(error: String) {
        if (isEmpty(error)) {
            toast(getString(R.string.net_error))
        } else {
            toast(error)
        }
    }

    fun showLoadingUI(showString: String, isCan: Boolean) {
        CommonUtils.showProTip(context, showString, isCan)
    }

    fun hideLoadingUI() {
        CommonUtils.hideProTip()
    }

    override fun onClick(v: View) {
        v.isEnabled = false
        val msg = getNewHandlerMessage()
        msg.what = ONE_CLICK
        msg.obj = v
        getHandler().sendMessageDelayed(msg, FILTER_TIMEM)
    }

    /**
     * 隐藏顶部返回键
     */
    fun isShowBackView(isShowBackView: Boolean) {
        this.isShowBackView = isShowBackView
    }

    /**
     * 设置标题
     */
    fun setPageTitle(title: String) {
        if (AndBaseTopViewInfo.titleMode == AndBaseTopViewInfo.TitleMode.LEFT) {
            toolbar.title = title
        } else {
            textTitle.text = title
        }
    }

    /**
     * 设置标题栏背景色
     */
    fun setToolBarBg(backgroundColor: Int) {
        if (isShowTopView) {
            toolbar.setBackgroundColor(getColor_(backgroundColor))
        }
    }

    /**
     * 设置标题颜色
     */
    fun setToolBarTitleColor(titleTextColor: Int) {
        if (isShowTopView) {
            toolbar.setTitleTextColor(getColor_(titleTextColor))
            textTitle.setTextColor(getColor_(titleTextColor))
        }
    }

    /**
     * 设置返回按钮
     */
    fun setBackLeftDrawable(backLeftDrawable: Int) {
        if (isShowTopView) {
            val appCompatActivity: AppCompatActivity = activity as AppCompatActivity
            appCompatActivity.supportActionBar!!.setHomeAsUpIndicator(backLeftDrawable)
        }
    }

    /**
     * 移除顶部布局
     */
    fun removeAllBaseTopLayout() {
        isShowTopView = false
        getBaseTopLayout().removeAllViews()
    }

    /**
     * 移除顶部布局
     */
    fun removeBaseTopLayout(position: Int) {
        if (position < getBaseTopLayout().childCount) {
            getBaseTopLayout().removeViewAt(position)
        }
    }

    /**
     * 获取顶部布局
     */
    fun getBaseTopLayout(): LinearLayout {
        return baseTopLinearLayout
    }

    /**
     * 获取中间布局
     */
    fun getBaseCenterLayout(): FrameLayout {
        return baseCenterFrameLayout
    }

    /**
     * 获取底部布局
     */
    fun getBaseBottomLayout(): LinearLayout {
        return baseBottomLinearLayout
    }

    /**
     * 判断�?
     */
    fun isEmpty(list: Any): Boolean {
        return CommonUtils.isEmpty(list)
    }

    /**
     * toast
     */
    fun toast(text: CharSequence) {
        HHTipUtils.getInstance().showToast(context, text.toString())
    }

    /**
     * print log 输出
     */
    fun println(text: Any) {
        CommonUtils.println(text)
    }

    /**
     * 格式�?
     */
    fun format(format: String, vararg args: Any): String {
        return CommonUtils.format(format, *args)
    }

    /**
     * 格式化N位小�?
     *
     *
     * 默认保留2�?
     */
    fun formatDouble(number: Double, vararg n: Int): String {
        return CommonUtils.formatDouble(number, *n)
    }

    /**
     * 设置view 可见�?
     */
    fun setViewVisible(view: View, vararg isVisible: Boolean) {
        CommonUtils.setViewVisible(view, *isVisible)
    }

    /**
     * 设置点击监听
     */
    fun setOnClickListener(view: View) {
        CommonUtils.setOnClickListener(view, this)
    }

    /**
     * 设置view 选中
     */
    fun setViewSelect(view: View, vararg isSelect: Boolean) {
        CommonUtils.setViewSelect(view, *isSelect)
    }

    /**
     * 设置 文本
     */
    fun setText(view: TextView, text: CharSequence) {
        CommonUtils.setText(view, text)
    }

    /**
     * 设置view 选中
     */
    fun setViewEnable(view: View, vararg isEnable: Boolean) {
        CommonUtils.setViewEnable(view, *isEnable)
    }

    /**
     * 加载 尺寸
     */
    fun getDimen_(resId: Int): Float {
        return CommonUtils.getDimen_(context, resId)
    }

    /**
     * 加载 drawable
     */
    fun getDrawable_(resId: Int): Drawable {
        return CommonUtils.getDrawable_(context, resId)
    }

    /**
     * 加载 color
     */
    fun getColor_(resId: Int): Int {
        return CommonUtils.getColor_(context, resId)
    }

    /**
     * 设置 color
     * eg: 0xff00ff00 16进制
     */
    fun setViewColor(view: View, color_hex: Int) {
        CommonUtils.setViewColor(view, color_hex)
    }

    /**
     * 设置 color
     * eg: R.color|drawable.xx
     */
    fun setViewColorRes(view: View, resId: Int) {
        CommonUtils.setViewColorRes(view, resId)
    }

    /**
     * 加载 layout
     */
    fun inflateView(layoutId: Int, vararg root: ViewGroup): View {
        return CommonUtils.inflateView(context, layoutId, *root)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        // 移除所有的消息和回调，清空消息队列
        mHandler.removeCallbacksAndMessages(null)
        if (isRegisteredEventBus) {
            AndEventBusUtils.unregister(this)
        }
        super.onDestroyView()
    }

    fun <T> getInstance(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                .genericSuperclass as ParameterizedType).getActualTypeArguments()[i] as Class<T>)
                .newInstance()
        } catch (e: Fragment.InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: java.lang.InstantiationException) {
            e.printStackTrace()
        }

        return null
    }

}
