package com.mtjsoft.www.kotlinmvputils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MSI1 on 2017/2/22.
 */

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {


    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}
