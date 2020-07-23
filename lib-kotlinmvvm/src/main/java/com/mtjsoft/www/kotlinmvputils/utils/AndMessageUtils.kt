package com.mtjsoft.www.kotlinmvputils.utils

import android.content.Context
import android.os.Message

import com.mtjsoft.www.kotlinmvputils.R

/**
 * 消息管理器
 * Created by Administrator on 2017/8/15.
 */

object AndMessageUtils {

    fun getMessage(context: Context, what: Int, result: String?): Message {
        val message = Message()
        message.what = what
        if (result == null) {
            message.arg1 = -1
            message.obj = context.getString(R.string.net_error)
        } else {
            message.arg1 = AndJsonParseUtils.getResponceStatus(result)
            message.obj = AndJsonParseUtils.getResponceMsg(result)
        }
        return message
    }
}
