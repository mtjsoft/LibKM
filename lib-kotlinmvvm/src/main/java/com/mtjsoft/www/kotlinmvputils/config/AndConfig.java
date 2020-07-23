package com.mtjsoft.www.kotlinmvputils.config;

import android.graphics.Color;

import com.mtjsoft.www.kotlinmvputils.R;
import com.yuyh.library.imgsel.config.ISListConfig;

public class AndConfig {
    // 自由配置选项
    public static ISListConfig getISListConfig(boolean multiSelect, boolean needCrop, boolean needCamera, int maxNum) {
        return new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(multiSelect)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                // “确定”按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
//                .statusBarColor(Color.parseColor("#0aabfe"))
                // 返回图标ResId
                .backResId(R.drawable.ic_arrow_white_24dp)
                // 标题
                .title("图片选择")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#0aabfe"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 400, 400)
                .needCrop(needCrop)
                // 第一个是否显示相机，默认true
                .needCamera(needCamera)
                // 最大选择图片数量，默认9
                .maxNum(maxNum)
                .build();
    }
}
