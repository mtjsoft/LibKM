package com.mtjsoft.www.kotlinmvputils.utils

import com.mtjsoft.www.kotlinmvputils.imp.AndDialogClickListener

/**
 * Created by Administrator on 2017/8/19.
 */

abstract class Builder {
    abstract fun buildTitle(title: String): Builder

    abstract fun buildMsg(msg: String): Builder

    abstract fun buildSureClickListener(sureClickListener: AndDialogClickListener?): Builder

    abstract fun buildCancelClickListener(cancelClickListener: AndDialogClickListener?): Builder

    abstract fun buildSureTextColor(colorId: Int): Builder

    abstract fun buildCanCancel(canCancel: Boolean): Builder

    abstract fun buildCancelText(cancelText: String): Builder

    abstract fun buildSureText(sureText: String): Builder

    abstract fun buildShowAll(showAll: Boolean): Builder

    abstract fun buildHideAll(showAll: Boolean): Builder

    abstract fun showDialog()
}
