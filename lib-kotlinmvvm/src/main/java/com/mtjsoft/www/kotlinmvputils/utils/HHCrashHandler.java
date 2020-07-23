package com.mtjsoft.www.kotlinmvputils.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 * 在重启页面调用：HHCrashHandler.getInstance().uploadAppError(getPageContext(), getIntent().getStringExtra("error"));
 * 常量：1.bug日志路径   LOG_SAVE_CACHE
 * 2.项目id，与后台保持一致  PROJECT_ID
 * 3.接口地址  BUG_IP
 */
public class HHCrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static HHCrashHandler instance;
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private String savePath = "";// 日志文件路径
    private String errorContent = "";// 错误内容
    private boolean is_restart = false;
    private Class<? extends AppCompatActivity> clazz;

    /**
     * 保证只有一个CrashHandler实例
     */
    private HHCrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static HHCrashHandler getInstance() {
        if (instance == null)
            instance = new HHCrashHandler();
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     * @param is_restart 是否重启
     * @param clazz      重启反馈页面
     */
    public void init(Context context, boolean is_restart,
                     Class<? extends AppCompatActivity> clazz) {
        mContext = context;
        this.is_restart = is_restart;
        this.clazz = clazz;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (is_restart && clazz != null) {
                showToast("很抱歉,程序出现异常,即将重启.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                Intent intent = new Intent(mContext, clazz);
                intent.putExtra("error", errorContent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                AndActivityUtils.Companion.getInstance().clearActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                System.gc();
            } else {
                showToast("很抱歉,程序出现异常,即将退出.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                // 清掉所有Activity
                AndActivityUtils.Companion.getInstance().clearActivity();
                // 退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                System.gc();
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 保存日志文件
        // saveCatchInfo2File(ex);
        // 收集设备参数信息
        // collectDeviceInfo(mContext);
        return true;
    }

    private void showToast(String msg) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                HHTipUtils.getInstance().showToast(mContext, msg);
                Looper.loop();
            }
        }.start();
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(savePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            if (is_restart) {
                // 需要重启app
                errorContent = sb.toString();
            } else {
                // 发送给开发人员
//				uploadAppError(mContext, sb.toString());
            }
            fos.close();
            return fileName;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 上传bug信息到服务器
     *
     * @param context
     * @param error
     */
    public void uploadAppError(final Context context, final String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                uploadError(context, error);
            }
        }).start();
    }

    /**
     * 接口
     *
     * @param context
     * @param error
     * @return
     */
    private String uploadError(Context context, String error) {

        return "";
    }
}
