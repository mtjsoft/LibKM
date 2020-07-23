package com.mtjsoft.www.kotlinmvputils.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mtjsoft.www.kotlinmvputils.R;

import java.lang.ref.WeakReference;

/**
 * 用于显示Toast提示和ProgressDialog提示的工具类
 *
 * @author yuan
 */
public class HHTipUtils {
    //定义了唯一的实例对象
    private static HHTipUtils mHHTipUtils;

    //私有化构造函数
    private HHTipUtils() {
    }

    private WeakReference<Context> mContext = new WeakReference<>(null);
    //显示的Toast
    private Toast mToast;
    private View toastView;
    //显示Toast的内容
    private TextView mToastContentTextView;
    //显示的ProgressDialog
    private Dialog mProgressDialog;
    //显示Dialog的内容
    private TextView mDialogContentTextView;

    /**
     * 获取HHTipUtils的实例
     *
     * @return
     */
    public static HHTipUtils getInstance() {
        if (mHHTipUtils == null) {
            mHHTipUtils = new HHTipUtils();
        }
        return mHHTipUtils;
    }

    /**
     * 显示Toast通知
     *
     * @param context
     * @param msg     显示的内容
     */
    public void showToast(Context context, String msg) {
        Context applicationContext = context.getApplicationContext();
        if (toastView == null) {
            toastView = View.inflate(applicationContext, R.layout.hh_toast_huahan_custom, null);
            mToastContentTextView = toastView.findViewById(R.id.hh_tv_toast_content);
        }
        if (mToast == null) {
            mToast = new Toast(applicationContext);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(toastView);
        } else {
            mToast.cancel();
            mToast = new Toast(applicationContext);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(toastView);
        }
        mToastContentTextView.setText(msg);
        mToast.show();
    }

    /**
     * 显示Toast通知
     *
     * @param context
     * @param resID   资源的ID
     */
    public void showToast(Context context, int resID) {
        showToast(context, context.getString(resID));
    }

    /**
     * 显示提示的Dialog
     *
     * @param context
     * @param msg     显示的内容
     */
    public void showProgressDialog(Context context, String msg, boolean isCancel) {
        mContext = new WeakReference<>(context);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mContext.get() == null ||  !(mContext.get() instanceof Activity)) {
            return;
        }
        mProgressDialog = new Dialog(mContext.get(), R.style.hh_dialog_style);
        View view = View.inflate(mContext.get(), R.layout.hh_dialog_huahan_custom, null);
        mDialogContentTextView = view.findViewById(R.id.hh_tv_dialog_content);
        mProgressDialog.setContentView(view);
        if (!TextUtils.isEmpty(msg)) {
            mDialogContentTextView.setText(msg);
        }
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(isCancel);
    }

    /**
     * 显示的提示的Dialog
     *
     * @param context
     * @param resID   显示的内容的资源的ID
     */
    public void showProgressDialog(Context context, int resID) {
        showProgressDialog(context, context.getString(resID), true);
    }

    /**
     * 显示的提示的Dialog
     *
     * @param context
     * @param resID
     * @param isCancel dialog 是否可以取消
     */
    public void showProgressDialog(Context context, int resID, boolean isCancel) {
        showProgressDialog(context, context.getString(resID), isCancel);
    }

    /**
     * 显示的提示的Dialog
     *
     * @param context
     * @param msg
     */
    public void showProgressDialog(Context context, String msg) {
        showProgressDialog(context, msg, true);
    }

    /**
     * 取消显示提示的Dialog
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
