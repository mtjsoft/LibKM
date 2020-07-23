package com.mtjsoft.www.kotlinmvputils.base

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.jaeger.library.StatusBarUtil
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.imp.AndDialogClickListener
import com.mtjsoft.www.kotlinmvputils.imp.PermissionsResultListener
import com.mtjsoft.www.kotlinmvputils.manager.AndBaseTopViewInfo
import com.mtjsoft.www.kotlinmvputils.model.EventMessage
import com.mtjsoft.www.kotlinmvputils.utils.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.base_layout.*
import me.jessyan.autosize.AutoSizeCompat
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * BaseTopViewActivity
 *
 * Activity顶层基类
 *
 * @author mtj
 */

abstract class BaseTopViewActivity : RxAppCompatActivity(), View.OnClickListener {
    // 拦截所有两次点击时间间隔小于一秒的点击事件
    val FILTER_TIMEM = 1000L
    var ONE_CLICK = 919191

    private var isSwipeBackFinish = false

    /**
     * 是否注册EventBus
     */
    private var isRegisteredEventBus = false

    // For Android 6.0
    private var mListener: PermissionsResultListener? = null

    //申请标记值
    private val REQUEST_CODE_ASK_PERMISSIONS = 100

    //手动开启权限requestCode
    private val SETTINGS_REQUEST_CODE = 200

    //拒绝权限后是否关闭界面或APP
    private var mNeedFinish = false

    //界面传递过来的权限列表,用于二次申请
    private var mPermissionsList = ArrayList<String>()

    //必要全选,如果这几个权限没通过的话,就无法使用APP
    private var FORCE_REQUIRE_PERMISSIONS: ArrayList<String> = object : ArrayList<String>() {
        init {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val INVISIBLE = View.INVISIBLE

    protected val TAG = CommonUtils.getTag(this)

    //添加的view
    private var addCenterCiew: View? = null

    //
    private var topView: View? = null
    private var toolbar: Toolbar? = null
    private var textTitle: TextView? = null
    private var isShowBackView: Boolean = true
    private var isShowTopView: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter inject 注入
        ARouter.getInstance().inject(this)
        AndActivityUtils.instance.addActivity(this)
        setContentView(R.layout.base_layout)
        topView = inflateView(R.layout.include_toolbar)
        toolbar = topView!!.findViewById(R.id.toolbar) as Toolbar
        textTitle = topView!!.findViewById(R.id.tv_title) as TextView
        addCenterCiew = initaddView()
        getBaseCenterLayout().addView(addCenterCiew, 0)
        initBaseView()
        if (isShowTopView) {
            initTopTitle()
        }
        initSwipeBackFinish()
        if (isRegisteredEventBus) {
            AndEventBusUtils.register(this)
        }
    }

    protected abstract fun initBaseView()

    protected abstract fun initaddView(): View

    protected abstract fun processHandlerMsg(msg: Message)

    fun getContext(): Activity = this

    private fun initTopTitle() {
        val andBaseTopViewInfo = AndBaseTopViewInfo
        if (andBaseTopViewInfo.backgroundColor == -1) {
            toolbar!!.setBackgroundColor(Color.RED)
        } else {
            toolbar!!.setBackgroundColor(getColor_(andBaseTopViewInfo.backgroundColor))
        }
        if (AndBaseTopViewInfo.titleMode == AndBaseTopViewInfo.TitleMode.LEFT) {
            toolbar!!.setTitleTextColor(andBaseTopViewInfo.titleTextColor)
            //toolbar字体大小在stytle里面调整
        } else {
            textTitle!!.setTextColor(andBaseTopViewInfo.titleTextColor)
            textTitle!!.textSize = (andBaseTopViewInfo.titleSize).toFloat()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(isShowBackView)
        supportActionBar!!.setHomeAsUpIndicator(andBaseTopViewInfo.backLeftDrawable)
        supportActionBar!!.setDisplayShowTitleEnabled(AndBaseTopViewInfo.titleMode == AndBaseTopViewInfo.TitleMode.LEFT)
        getBaseTopLayout().addView(topView, 0)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
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
            toolbar!!.title = title
        } else {
            textTitle!!.text = title
        }
    }

    /**
     * 设置标题栏背景色
     */
    fun setToolBarBg(backgroundColor: Int) {
        if (isShowTopView && toolbar != null) {
            toolbar!!.setBackgroundColor(getColor_(backgroundColor))
        }
    }

    /**
     * 设置标题颜色
     */
    fun setToolBarTitleColor(titleTextColor: Int) {
        if (isShowTopView && toolbar != null) {
            toolbar!!.setTitleTextColor(getColor_(titleTextColor))
            textTitle!!.setTextColor(getColor_(titleTextColor))
        }
    }

    /**
     * 设置返回按钮
     */
    fun setBackLeftDrawable(backLeftDrawable: Int) {
        if (isShowTopView && toolbar != null) {
            supportActionBar!!.setHomeAsUpIndicator(backLeftDrawable)
        }
    }

    /**
     * 移除顶部布局
     */
    fun removeAllBaseTopLayout() {
        isShowTopView = false
        top_layout.removeAllViews()
    }

    /**
     * 移除顶部布局
     */
    fun removeBaseTopLayout(position: Int) {
        if (position < top_layout.childCount) {
            top_layout.removeViewAt(position)
        }
    }

    /**
     * 获取添加的中间的view
     */
    fun getAddCenterView(): View {
        return addCenterCiew!!
    }

    /**
     * 获取顶部布局
     */
    fun getBaseTopLayout(): LinearLayout {
        return top_layout
    }

    /**
     * 获取中间布局
     */
    fun getBaseCenterLayout(): FrameLayout {
        return center_layout
    }

    /**
     * 获取底部布局
     */
    fun getBaseBottomLayout(): LinearLayout {
        return bottom_layout
    }

    /**
     * 设置状态栏颜色 格式为"#xxxxxx" 十六进制颜色表，0表示为透明
     * @param color
     */
    open fun setStatusBarColor(color: Int) {
        if (color == 0) {
            StatusBarUtil.setTranslucent(this, 0)
        } else {
            StatusBarUtil.setColorNoTranslucent(this, color)
        }
    }

    /**
     * 处理消息的handler
     */
    private val mHandler = @SuppressLint("HandlerLeak")
    object : HHWeakHandler<Activity>(this) {
        override fun processHandlerMessage(msg: Message) {
            if (msg.what == ONE_CLICK) {
                val view: View = msg.obj as View
                view.isEnabled = true
            }
            processHandlerMsg(msg)
        }
    }

    /**
     * 获取一个Message对象
     *
     * @return
     */
    fun getNewHandlerMessage(): Message {
        return mHandler.obtainMessage()
    }

    fun getHandler(): Handler {
        return mHandler
    }

    /**
     * 发送消息
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

    override fun onDestroy() {
        // 移除所有的消息和回调，清空消息队列
        mHandler.removeCallbacksAndMessages(null)
        if (isRegisteredEventBus) {
            AndEventBusUtils.unregister(this)
        }
//        GlideLoadUtil.clearMemoryCache(this)
        AndActivityUtils.instance.removeActivity(this)
        super.onDestroy()
    }

    /**
     * 子类重写onClick方法，可处理连续多次点击问题
     */
    override fun onClick(p0: View) {
        p0.isEnabled = false
        val msg = getNewHandlerMessage()
        msg.what = ONE_CLICK
        msg.obj = p0
        getHandler().sendMessageDelayed(msg, FILTER_TIMEM)
    }

    fun setIsSwipeBackFinish(b: Boolean) {
        isSwipeBackFinish = b
    }

    fun onError(error: String) {
        if (isEmpty(error)) {
            toast(getString(R.string.net_error))
        } else {
            toast(error)
        }
    }

    fun showLoadingUI(showString: String, isCan: Boolean) {
        CommonUtils.showProTip(getContext(), showString, isCan)
    }

    fun hideLoadingUI() {
        CommonUtils.hideProTip()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 解决 AutoSize 适配失效的问题
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()))
        return super.getResources()
    }

    /**
     * 判断空
     */
    fun isEmpty(list: Any): Boolean {
        return CommonUtils.isEmpty(list)
    }

    /**
     * toast
     */
    fun toast(text: CharSequence) {
        HHTipUtils.getInstance().showToast(getContext(), text.toString())
    }

    fun toast(resId: Int) {
        HHTipUtils.getInstance().showToast(getContext(), getString(resId))
    }

    /**
     * log 输出
     */
    fun log(msg: String, vararg tags: String) {
        CommonUtils.log(msg, TAG)
    }

    /**
     * print log 输出
     */
    fun println(text: Any) {
        CommonUtils.println(text)
    }

    /**
     * 格式化
     */
    fun format(format: String, vararg args: Any): String {
        return CommonUtils.format(format, *args)
    }

    /**
     * 格式化N位小数
     *
     *
     * 默认保留2位小数
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
     * 加载 layout
     */
    fun inflateView(layoutId: Int, vararg root: ViewGroup): View {
        return CommonUtils.inflateView(getContext(), layoutId, *root)
    }

    /**
     * 加载 尺寸
     */
    fun getDimen_(resId: Int): Float {
        return CommonUtils.getDimen_(getContext(), resId)
    }

    /**
     * 加载 drawable
     */
    fun getDrawable_(resId: Int): Drawable {
        return CommonUtils.getDrawable_(getContext(), resId)
    }

    /**
     * 加载 color
     */
    fun getColor_(resId: Int): Int {
        return CommonUtils.getColor_(getContext(), resId)
    }

    /**
     * 隐藏软键�?
     */
    fun hideSoftKeyboard() {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && getContext().currentFocus != null) {
            if (getContext().currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(
                    getContext().currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
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


    /**
     * 设置必要申请的权限
     *
     * @param permissionsList
     */
    fun setPermissionsList(permissionsList: ArrayList<String>) {
        FORCE_REQUIRE_PERMISSIONS = permissionsList
    }

    /**
     * 权限允许或拒绝对话框
     *
     * @param permissions 需要申请的权限
     * @param needFinish  如果必须的权限没有允许的话，是否需要finish当前 Activity
     * @param callback    回调对象
     */
    fun requestPermission(
        permissions: ArrayList<String>?, needFinish: Boolean,
        callback: PermissionsResultListener
    ) {
        if (permissions == null || permissions.size == 0) {
            return
        }
        mNeedFinish = needFinish
        mListener = callback
        mPermissionsList = permissions
        if (Build.VERSION.SDK_INT >= 23) {
            //获取未通过的权限列表
            val newPermissions = checkEachSelfPermission(permissions)
            if (newPermissions.size > 0) {// 是否有未通过的权限
                requestEachPermissions(newPermissions.toTypedArray())
            } else {// 权限已经都申请通过了
                if (mListener != null) {
                    mListener!!.onPermissionGranted()
                }
            }
        } else {
            if (mListener != null) {
                mListener!!.onPermissionGranted()
            }
        }
    }

    /**
     * 检察每个权限是否申请
     * @param permissions
     * @return newPermissions.size > 0 表示有权限需要申请
     */
    private fun checkEachSelfPermission(permissions: ArrayList<String>): ArrayList<String> {
        val newPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                newPermissions.add(permission)
            }
        }
        return newPermissions
    }

    /**
     * 申请权限前判断是否需要声明
     *
     * @param permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun requestEachPermissions(permissions: Array<String>) {
        if (shouldShowRequestPermissionRationale(permissions)) {// 需要再次声明
            showRationaleDialog(permissions)
        } else {
            ActivityCompat.requestPermissions(
                this, permissions,
                REQUEST_CODE_ASK_PERMISSIONS
            )
        }
    }

    /**
     * 再次申请权限时，是否需要声明
     *
     * @param permissions
     * @return
     */
    private fun shouldShowRequestPermissionRationale(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 弹出声明的 Dialog
     *
     * @param permissions
     */
    private fun showRationaleDialog(permissions: Array<String>) {
        AndDialogUtils.builder(this)
            .buildMsg(getString(R.string.permission))
            .buildSureClickListener(object : AndDialogClickListener {
                override fun onClick(paramDialog: Dialog, paramView: View) {
                    paramDialog.dismiss()
                    ActivityCompat.requestPermissions(
                        this@BaseTopViewActivity, permissions,
                        REQUEST_CODE_ASK_PERMISSIONS
                    )
                }
            })
            .buildCancelClickListener(object : AndDialogClickListener {
                override fun onClick(paramDialog: Dialog, paramView: View) {
                    if (mNeedFinish) {
                        finish()
                    }
                }
            })
            .buildShowAll(true)
            .buildCanCancel(false)
            .showDialog()
    }

    /**
     * 申请权限结果的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS && permissions != null) {
            // 获取被拒绝的权限列表
            val deniedPermissions = ArrayList<String>()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    deniedPermissions.add(permission)
                }
            }
            //存在被拒绝的权限
            if (deniedPermissions != null && deniedPermissions.size > 0) {
                mPermissionsList = deniedPermissions
                if (mNeedFinish) {
                    showPermissionSettingDialog()
                } else {
                    if (mListener != null) {
                        mListener!!.onPermissionDenied()
                    }
                }
            } else {
                if (mListener != null) {
                    mListener!!.onPermissionGranted()
                }
            }
        }
    }

    /**
     * 手动开启权限弹窗
     */
    private fun showPermissionSettingDialog() {
        AndDialogUtils.builder(this)
            .buildMsg(getString(R.string.permission_must))
            .buildSureClickListener(object : AndDialogClickListener {
                override fun onClick(paramDialog: Dialog, paramView: View) {
                    paramDialog.dismiss()
                    startAppSettings()
                }
            })
            .buildCancelClickListener(object : AndDialogClickListener {
                override fun onClick(paramDialog: Dialog, paramView: View) {
                    if (mNeedFinish) {
                        finish()
                    }
                }
            })
            .buildShowAll(true)
            .buildCanCancel(false)
            .showDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //如果需要跳转系统设置页后返回自动再次检查和执行业务 如果不需要则不需要重写onActivityResult
        if (requestCode == SETTINGS_REQUEST_CODE) {
            requestPermission(mPermissionsList, mNeedFinish, mListener!!)
        }
    }

    /**
     * 启动当前应用设置页面
     */
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + packageName)
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }

    /**
     * 初始化滑动返回
     */
    private fun initSwipeBackFinish() {
        if (isSwipeBackFinish) {
            val slidingPaneLayout = SlidingPaneLayout(this)
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，
            //默认是32dp
            try {
                //更改属性
                val field = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
                field.isAccessible = true
                field.set(slidingPaneLayout, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //设置监听事件
            slidingPaneLayout.setPanelSlideListener(slideListener())
            slidingPaneLayout.sliderFadeColor = resources.getColor(R.color.transparent)
            // 左侧的透明视图
            val leftView = View(this)
            leftView.layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            slidingPaneLayout.addView(leftView, 0)
            val decorView = window.decorView as ViewGroup
            // 右侧的内容视图
            val decorChild = decorView.getChildAt(0) as ViewGroup
            decorChild.setBackgroundColor(
                getResources()
                    .getColor(android.R.color.white)
            )
            decorView.removeView(decorChild)
            decorView.addView(slidingPaneLayout)
            // 为 SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1)
        }
    }

    /**
     * 侧滑退出监听
     */
    private inner class slideListener : SlidingPaneLayout.PanelSlideListener {

        override fun onPanelSlide(panel: View, slideOffset: Float) {

        }

        override fun onPanelOpened(panel: View) {
            finish()
        }

        override fun onPanelClosed(panel: View) {

        }
    }
}
