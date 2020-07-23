package com.mtjsoft.www.kotlinmvputils.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtjsoft.www.kotlinmvputils.R;

import java.lang.ref.WeakReference;

public class Loading_view extends ProgressDialog implements DialogInterface.OnCancelListener {

    private WeakReference<Context> mContext;
    private String msgString = "";

    public Loading_view(Context context) {
        this(context, "正在加载...");
    }

    public Loading_view(Context context, String msg) {
        super(context, R.style.CustomDialog);
        mContext = new WeakReference<>(context);
        msgString = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.loading, null);
        TextView msgTextView = view.findViewById(R.id.loading_msg);
        if (TextUtils.isEmpty(msgString)) {
            msgTextView.setVisibility(View.GONE);
        } else {
            msgTextView.setText(msgString);
            msgTextView.setVisibility(View.VISIBLE);
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(view, lp);
        setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // 点手机返回键等触发Dialog消失，应该取消正在进行的网络请求等
        Context context = mContext.get();
        if (context != null) {
            // 取消网络请求
        }
    }
}
