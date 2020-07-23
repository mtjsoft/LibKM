package com.mtjsoft.www.kotlinmvputils.model

/**
 * 加载页面显示的时候显示的信息，需要配置显示的文字和现实的图片
 *
 * @author yuan
 */
class LoadViewInfo(msgInfo: String, drawableID: Int) {
    var drawableID = 0
    var msgInfo = ""

    init {
        this.drawableID = drawableID
        this.msgInfo = msgInfo
    }

}
