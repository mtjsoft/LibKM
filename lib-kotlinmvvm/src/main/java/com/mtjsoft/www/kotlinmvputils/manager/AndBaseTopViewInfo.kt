package com.mtjsoft.www.kotlinmvputils.manager

import android.graphics.Color
import com.mtjsoft.www.kotlinmvputils.R

/**
 * Created by Administrator on 2018/2/3.
 */
object AndBaseTopViewInfo {
    var titleMode = TitleMode.CENTER
    var titleSize = 18
    var titleTextColor = Color.WHITE
    var backLeftDrawable = R.drawable.ic_arrow_white_24dp
    var backgroundColor = R.color.main_base_color

    enum class TitleMode {
        LEFT,
        CENTER
    }
}