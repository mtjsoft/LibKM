package com.mtjsoft.www.kotlinmvputils.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * 和App相关的工具类
 *
 * @author yuan
 */
object AndAppUtils {

    private val tag = AndAppUtils::class.java.name

    //使用线程池，在子线程解码
    private var executorService: ExecutorService? = null

    fun getThreadPool(): ExecutorService {
        if (executorService == null || executorService!!.isShutdown) {
            synchronized(AndAppUtils::class.java) {
                executorService = ThreadPoolExecutor(10, 10, 0L, TimeUnit.SECONDS, LinkedBlockingQueue())
            }
        }
        return executorService!!
    }

    fun openInBrowser(context: Context, url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.parse(url)
        intent.data = uri
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "打开失败了，没有可打开的应用", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareText(context: Context, shareText: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent, title))
    }

    fun shareImage(context: Context, uri: Uri, title: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        context.startActivity(Intent.createChooser(shareIntent, title))
    }

    /**
     * 判断网络情况
     *
     * @return false 表示没有网络 true 表示有网络
     *//*
    fun isNetworkAvalible(context: Context): Boolean {
        // 获得网络状态管理器
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager == null) {
            return false
        } else {
            // 建立网络数组
            val net_info = connectivityManager.allNetworkInfo
            if (net_info != null) {
                for (i in net_info.indices) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }*/

    /**
     * 判断是否安装某应用程序
     *
     * @param context
     * @param packagename 应用的包名
     * @return true，已经安装该应用；false没有安装该应用
     */
    fun isAppInstall(context: Context, packagename: String): Boolean {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(packagename, 0)
        } catch (e: NameNotFoundException) {
            packageInfo = null
        }

        return if (packageInfo == null) {
            false
        } else {
            true
        }
    }

    /**
     * 跳转到其他app。如果该应用存在的话跳转到该应用，否则不执行任何操作
     *
     * @param context
     * @param packageName 其他应用的包名
     */
    fun jumpToOtherApp(context: Context, packageName: String) {
        val packageManager = context.packageManager
        var intent: Intent? = Intent()
        try {
            intent = packageManager.getLaunchIntentForPackage(packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
        }

    }

    /**
     * 安装应用程序.如果路径合法并且文件存在的时候回安装文件，否则不执行任何操作
     *
     * @param context
     * @param filePath 文件的路径
     */
    fun installApp(context: Context, filePath: String) {
        val file = File(filePath)
        if (!TextUtils.isEmpty(filePath) && file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(File(filePath)),
                    "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }

    /**
     * 卸载应用程序
     *
     * @param context
     * @param packageName 应用的包名
     */
    fun unInstallApp(context: Context, packageName: String) {
        val packageURI = Uri.parse("package:" + packageName)
        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageURI)
        context.startActivity(uninstallIntent)
    }

    /**
     * 判断App的快捷方式是否存在
     *
     * @param context
     * @param appName 应用的名称；或者创建的快捷方式显示的名字
     * @return
     */
    fun isShortCutExist(context: Context, appName: String): Boolean {
        var isInstallShortcut = false
        val AUTHORITY = getAuthorityFromPermission(context)
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/favorites?notify=true")
        val c = context.contentResolver.query(CONTENT_URI, null, "title=?", arrayOf(appName), null)
        if (null != c && c.count > 0) {
            isInstallShortcut = true
        }
        return isInstallShortcut
    }

    /**
     * 获取Launcher应用提供你的Provider的签名
     *
     * @param context
     * @return
     */
    private fun getAuthorityFromPermission(context: Context): String? {
        val packs = context.packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS)
        if (packs != null) {
            for (pack in packs) {
                val providers = pack.providers
                if (providers != null) {
                    for (provider in providers) {
                        if (isMatchPermission(provider.readPermission)) {
                            return provider.authority
                        }
                        if (isMatchPermission(provider.writePermission)) {
                            return provider.authority
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * 判断当前的权限是否符合要求
     *
     * @param permission
     * @return
     */
    private fun isMatchPermission(permission: String): Boolean {
        if (TextUtils.isEmpty(permission)) {
            return false
        }
        val pattern = "com.android.launcher[1-3]?.permission.READ_SETTINGS"
        val pat = Pattern.compile(pattern)
        val matcher = pat.matcher(permission)
        return matcher.matches()
    }

    /**
     * 获取应用程序版本号,如果获取失败则返回-1
     *
     * @param context
     * @param packageName 软件的包名
     * @return
     */
    @JvmOverloads
    fun getVerCode(context: Context, packageName: String? = null): Int {
        var packageName = packageName
        var verCode = -1
        try {
            if (TextUtils.isEmpty(packageName)) {
                packageName = context.packageName
            }
            verCode = context.packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: NameNotFoundException) {

        }

        return verCode
    }

    /**
     * 获取应用程序版本名称，获取失败返回空字符串
     *
     * @param context
     * @param packageName 应用的包名
     * @return
     */
    fun getVerName(context: Context, packageName: String? = null): String {
        var packageName = packageName
        var verName = ""
        try {
            if (TextUtils.isEmpty(packageName)) {
                packageName = context.packageName
            }
            verName = context.packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: NameNotFoundException) {
        }

        return verName
    }

    /**
     * 获取App具体设置
     *
     * @param context 上下文
     */
    fun getAppDetailsSettings(context: Context, requestCode: Int) {
        getAppDetailsSettings(context, context.packageName, requestCode)
    }

    /**
     * 获取App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun getAppDetailsSettings(context: Context, packageName: String, requestCode: Int) {
        (context as AppCompatActivity).startActivityForResult(getAppDetailsSettingsIntent(packageName), requestCode)
    }

    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    fun getAppDetailsSettingsIntent(packageName: String): Intent {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.parse("package:" + packageName)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 通过任务管理器杀死进程
     * 需添加权限 `<uses-permission android:name="android.permission.RESTART_PACKAGES">`
     *
     *
     *
     * @param context
     */
    fun restart(context: Context) {
        val currentVersion = android.os.Build.VERSION.SDK_INT
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(startMain)
            System.exit(0)
        } else {// android2.1
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            am.restartPackage(context.packageName)
        }
    }
}
/**
 * 获取应用的版本号。获取失败返回-1
 *
 * @param context
 * @return
 */
/**
 * 获取应用的版本名称，获取失败返回空字符串
 *
 * @param context
 * @return
 */
