package com.mtjsoft.www.kotlinmvputils.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.mtjsoft.www.kotlinmvputils.utils.HHTipUtils
import com.mtjsoft.www.kotlinmvputils.view.Loading_view
import java.lang.ref.WeakReference
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * 工具类
 * @author mtj
 */

object CommonUtils {

    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val INVISIBLE = View.INVISIBLE

    private var toast: Toast? = null

    private var mContext = WeakReference<Context>(null)
    private var sDialog: Loading_view? = null

    /**
     * 获取ip地址
     *
     * @return
     */
    // skip ipv6
    val hostIP: String?
        get() {

            var hostIp: String? = null
            try {
                val nis = NetworkInterface.getNetworkInterfaces()
                var ia: InetAddress? = null
                while (nis.hasMoreElements()) {
                    val ni = nis.nextElement() as NetworkInterface
                    val ias = ni.inetAddresses
                    while (ias.hasMoreElements()) {
                        ia = ias.nextElement()
                        if (ia is Inet6Address) {
                            continue
                        }
                        val ip = ia!!.hostAddress
                        if ("127.0.0.1" != ip) {
                            hostIp = ia.hostAddress
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.i("yao", "SocketException")
                e.printStackTrace()
            }

            return hostIp

        }

    fun showProTip(context: Context, showString: String, isCan: Boolean) {
        mContext = WeakReference(context)
        if (sDialog != null && sDialog!!.isShowing) {
            sDialog!!.dismiss()
            sDialog = null
        }
        if (mContext.get() != null && mContext.get() !is Activity) {
            return
        }
        sDialog = Loading_view(mContext.get(), showString)
        sDialog!!.setCancelable(isCan)
        sDialog!!.setCanceledOnTouchOutside(isCan)
        if (sDialog != null && !sDialog!!.isShowing) {
            sDialog!!.show()
        }
    }

    fun hideProTip() {
        if (sDialog != null && sDialog!!.isShowing) {
            sDialog!!.dismiss()
        }
        sDialog = null
    }

    /**
     * 判断
     */
    fun getTag(obj: Any): String {
        return obj.javaClass.simpleName
    }

    /**
     * 判断空
     */
    fun isEmpty(list: Any?): Boolean {

        if (list == null) {
            return true

        } else if (list is List<*>) {
            return list.isEmpty()

        } else if (list is CharSequence) {
            return list.length == 0

        } else if (list is Map<*, *>) {
            return list.isEmpty()

        } else if (list is Set<*>) {
            return list.isEmpty()

        } else if (list is Array<*>) {
            return list.size == 0

        }

        return false
    }

    /**
     * log 输出
     */
    fun log(msg: String, vararg tags: String) {
        Log.i(if (tags.size > 0) tags[0] else "log-i", msg)
    }

    /**
     * toast
     */
    fun toast(context: Context, text: CharSequence) {
        HHTipUtils.getInstance().showToast(context, text.toString())
    }

    fun show(context: Context, text: CharSequence) {

        if (toast == null) {
            toast = Toast.makeText(context.applicationContext,
                    text,
                    Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(text)
        }
        toast!!.show()
    }

    fun show(context: Context, view: View) {
        if (toast == null) {
            toast = Toast(context.applicationContext)
            toast!!.view = view
            toast!!.duration = Toast.LENGTH_SHORT
        }
        toast!!.show()
    }


    /**
     * log 输出
     */
    fun println(text: Any) {
        println(format("println : obj = %s", text))
    }

    /**
     * 格式�?
     */
    fun format(format: String, vararg args: Any): String {
        return String.format(format, *args)
    }

    /**
     * 格式化N位小�?
     *
     *
     * 默认保留2�?
     */
    fun formatDouble(number: Double, vararg n: Int): String {
        val length = if (n.size > 0) n[0] else 2
        return format("%." + length + "f", number)
    }

    /**
     * 设置view 可见
     */
    fun setViewVisible(view: View?, vararg isVisible: Boolean) {

        if (view == null) {
            return
        }

        val visible = if (isVisible.size > 0 && isVisible[0]) VISIBLE else GONE

        if (visible == view.visibility) {
            return
        }

        view.visibility = visible
    }

    /**
     * 设置点击监听
     *
     * @param listener
     */
    fun setOnClickListener(view: View?, listener: OnClickListener) {

        if (view == null) {
            return
        }
        view.setOnClickListener(listener)
    }

    /**
     * 设置view 选中
     */
    fun setViewSelect(view: View?, vararg isSelect: Boolean) {

        if (view == null) {
            return
        }

        val select = isSelect.size > 0 && isSelect[0]
        view.isSelected = select
    }

    /**
     * 设置 文本
     */
    fun setText(view: TextView?, text: CharSequence) {
        if (view == null) {
            return
        }
        view.text = text
    }

    /**
     * 设置view 选中
     */
    fun setViewEnable(view: View?, vararg isEnable: Boolean) {

        if (view == null) {
            return
        }

        view.isEnabled = isEnable.size > 0 && isEnable[0]
    }


    /**
     * 创建dialog
     */
    fun createDialog(context: Context, layout: Int, theme: Int, vararg cancel: Boolean): Dialog {
        return createDialog(context, inflateView(context, layout), theme, *cancel)
    }

    /**
     * 创建dialog
     */
    fun createDialog(context: Context, view: View, theme: Int, vararg cancel: Boolean): Dialog {

        val dialog = Dialog(context, theme)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(if (cancel.size > 0) cancel[0] else false)
        return dialog

    }

    /**
     * 加载 layout
     */
    fun inflateView(context: Context, layoutId: Int, vararg root: ViewGroup): View {
        return LayoutInflater.from(context).inflate(layoutId, if (root.size > 0) root[0] else null, false)
    }

    /**
     * 加载 尺寸
     */
    fun getDimen_(context: Context, resId: Int): Float {
        return context.resources.getDimension(resId)
    }

    /**
     * 加载 drawable
     */
    fun getDrawable_(context: Context, resId: Int): Drawable {
        return context.resources.getDrawable(resId)
    }

    /**
     * 加载 color
     */
    fun getColor_(context: Context, resId: Int): Int {
        return context.resources.getColor(resId)
    }

    /**
     * 设置 color
     * eg: 0xff00ff00 16进制
     */
    fun setViewColor(view: View?, color_hex: Int) {
        if (view == null) {
            return
        }
        view.setBackgroundColor(color_hex)
    }

    /**
     * 设置 color
     * eg: R.color|drawable.xx
     */
    fun setViewColorRes(view: View?, resId: Int) {
        if (view == null) {
            return
        }
        view.setBackgroundResource(resId)
    }

    /**
     * 设置删除�?
     */
    fun underline(textView: TextView) {
        textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG//设置删除�?
        textView.paint.isAntiAlias = true//抗锯�?
    }

    /**
     * 打电�?
     */
    fun callup(context: Context, text: String) {

        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        //url:统一资源定位�?
        //uri:统一资源标示符（更广�?
        intent.data = Uri.parse("tel:" + text)
        //�?启系统拨号器
        context.startActivity(intent)
    }

}