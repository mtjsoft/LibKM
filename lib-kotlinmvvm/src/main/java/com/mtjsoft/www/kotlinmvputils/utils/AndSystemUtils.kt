package com.mtjsoft.www.kotlinmvputils.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningServiceInfo
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.util.*


/**
 * 系统的帮助类
 *
 * @author mtj
 */
object AndSystemUtils {

    val GET_CAMERA_IMAGE = 1000
    val GET_ALBUM_IMAGE = 1001
    val GET_CROP_IMAGE = 1002


    /**
     * 判断SD卡是否装载
     *
     * @return true, sd卡已装载；false，sd卡未装载
     */
    val isSDExist: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

    /**
     * 获取手机的型号，如lenovo-a750等
     *
     * @return
     */
    val phoneType: String
        get() = Build.MODEL

    /**
     * 获取手机的制造厂商
     *
     * @return
     */
    val phoneMaker: String
        get() = Build.MANUFACTURER

    /**
     * 获取手机的IMEI号
     *
     * @param context
     * @return
     */
    fun getIMEI(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    /**
     * 获取资源的ID
     *
     * @param context 上下文对象
     * @param name    资源的名称（图片的话，不用带后缀名）
     * @param type    资源的类型
     * @return 0：表示的是没有找到相应的资源文件
     */
    fun getResourceID(context: Context, name: String, type: String): Int {
        return context.resources.getIdentifier(context.packageName + ":" + type + "/" + name, null, null)
    }

    /**
     * 获取正在运行的服务
     *
     * @param context 上下文对象
     * @param maxNum  最多获取的服务的个数
     * @return
     */
    fun getRunningService(context: Context, maxNum: Int): List<RunningServiceInfo> {
        var list: List<RunningServiceInfo> = ArrayList()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        list = manager.getRunningServices(maxNum)
        return list
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param v
     */
    fun showSystemKeyBoard(context: Context, v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文对象
     * @param v       获取焦点的View
     */
    fun hideSystemKeyBoard(context: Context, v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 打开或者关闭软键盘，如果打开的话就关闭；如果是关闭的就打开
     *
     * @param context
     */
    fun toogleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 判断GPS是否开启，只要gps或者agps开启一个就认为开启了gps
     *
     * @param context
     * @return
     */
    fun isGPSOpen(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val flag = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        val flag1 = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 通过相机获取图片，使用startActivityForResult的形式开启系统的相机
     * requestCode使用的是GET_CAMERA_IMAGE，值为1000
     *
     * @param activity     开启相机的Activity
     * @param fileSavePath 文件的保存路径，如果不需要不存图片，则参数可以为null或者""
     */
    fun getImageFromCamera(activity: Activity, fileSavePath: String) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (!TextUtils.isEmpty(fileSavePath)) {
            val file = File(fileSavePath)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        }
        activity.startActivityForResult(intent, GET_CAMERA_IMAGE)

    }

    /**
     * 通过相册获取图片，使用startActivityForResult的形式开启系统的相册
     * requestCode使用的是GET_ALBUM_IMAGE，值为1001
     *
     * @param activity 开启相册的Activity
     */
    fun getImageFromAlbum(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        activity.startActivityForResult(intent, GET_ALBUM_IMAGE)
    }

    /**
     * 裁剪图片
     *
     * @param activity 启动裁剪图片的Activity
     * @param uri      图片的uri路径
     * @param savePath 裁剪后图片的保存路径，如果不想保存的话传null或者“”
     * @param aspectX  裁剪时X轴的比例
     * @param aspectY  裁剪时Y轴的比例
     * 如果savePath为null或者“”，在onActivityResult中接收系统默认返回的是裁剪的缩略图，使用data.getExtras().get("data");接收。
     * 该方法以startActivityForResult的方式启动系统的裁剪工具，requestCode为GET_CROP_IMAGE，值为1002
     */
    fun cropImage(activity: Activity, uri: Uri, savePath: String, aspectX: Int, aspectY: Int, outX: Int) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outX)
        intent.putExtra("outputY", outX * aspectY / aspectX)
        if (TextUtils.isEmpty(savePath)) {
            intent.putExtra("return-data", true)
        } else {
            val saveFile = File(savePath)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile))
        }
        activity.startActivityForResult(intent, GET_CROP_IMAGE)
    }
}
