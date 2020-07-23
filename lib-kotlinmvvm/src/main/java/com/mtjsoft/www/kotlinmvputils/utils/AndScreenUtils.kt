package com.mtjsoft.www.kotlinmvputils.utils

import android.app.Activity
import android.content.Context
import java.lang.reflect.Field
import java.util.*

object AndScreenUtils {
    private val tag = AndScreenUtils::class.java.name
    val SCREEN_HEIGHT = "height"
    val SCREEN_WIDTH = "width"
    /**
     * 获取屏幕的宽度
     * @param context 上下文对象
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕的高度
     * @param context 上下文对象
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕的高度和宽度
     * @param context 上下文对象
     * @return 返回一个HashMap<String></String>,Integer>,获取宽度用SCREEN_WIDTH，获取高度用SCREEN_HEIGHT
     */
    fun getScreenHeightAndWidth(context: Context): HashMap<String, Int> {
        val map = HashMap<String, Int>()
        map[SCREEN_HEIGHT] = getScreenHeight(context)
        map[SCREEN_WIDTH] = getScreenWidth(context)
        return map
    }

    /**
     * 获取状态栏的高度,如果获取失败则返回0
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(x)
            return statusBarHeight
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }

    /**
     * 改变Activity的背景的透明度，以显示变暗的效果
     * @param activity            显示分享的Activity
     * @param alpha                Activity显示的透明度，1为不透明，显示正常的效果
     */
    fun setWindowDim(activity: Activity, alpha: Float) {
        val window = activity.window
        val attributes = window.attributes
        attributes.alpha = alpha
        window.attributes = attributes
    }
}
