package com.mtjsoft.www.kotlinmvputils.utils

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.View
import android.widget.TextView
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.imp.AndDialogClickListener

/**
 * 对话框操作的工具类
 *
 * @author mtj
 */
class AndDialogUtils : Builder() {
    override fun buildCancelText(cancelText: String): Builder {
        cancelTextView!!.text = cancelText
        return hhDialogUtils!!
    }

    override fun buildSureText(sureText: String): Builder {
        sureTextView!!.text = sureText
        return hhDialogUtils!!
    }

    override fun buildHideAll(hideAll: Boolean): Builder {
        if (hideAll) {
            cancelTextView!!.visibility = View.GONE
            sureTextView!!.visibility = View.GONE
            lineView!!.visibility = View.GONE
            lineHView!!.visibility = View.GONE
        }
        return hhDialogUtils!!
    }

    override fun buildTitle(title: String): Builder {
        titleTextView!!.text = title
        return hhDialogUtils!!
    }

    override fun buildMsg(msg: String): Builder {
        msgTextView!!.text = msg
        return hhDialogUtils!!
    }

    override fun buildCancelClickListener(cancelClickListener: AndDialogClickListener?): Builder {
        cancelTextView!!.setOnClickListener { v ->
            dialog!!.dismiss()
            if (cancelClickListener != null) {
                cancelClickListener!!.onClick(dialog!!, v)
            }
        }
        return hhDialogUtils!!
    }

    override fun buildSureClickListener(sureClickListener: AndDialogClickListener?): Builder {
        sureTextView!!.setOnClickListener { v ->
            if (sureClickListener != null) {
                sureClickListener!!.onClick(dialog!!, v)
            }
        }
        return hhDialogUtils!!
    }

    override fun buildSureTextColor(colorId: Int): Builder {
        sureTextView!!.setTextColor(sureTextView!!.context.resources.getColor(colorId))
//        cancelTextView!!.setTextColor(thisContext!!.resources.getColor(colorId))
        return hhDialogUtils!!
    }

    override fun buildCanCancel(canCancel: Boolean): Builder {
        dialog!!.setCancelable(canCancel)
        return hhDialogUtils!!
    }

    override fun buildShowAll(showAll: Boolean): Builder {
        if (!showAll) {
            lineView!!.visibility = View.GONE
            cancelTextView!!.visibility = View.GONE
        }
        return hhDialogUtils!!
    }

    override fun showDialog() {
        dialog!!.show()
    }

    companion object {
        private var hhDialogUtils: AndDialogUtils? = null
        private var dialog: Dialog? = null
        private var titleTextView: TextView? = null
        private var msgTextView: TextView? = null
        private var cancelTextView: TextView? = null
        private var sureTextView: TextView? = null
        private var lineView: View? = null
        private var lineHView: View? = null

        fun builder(context: Context): AndDialogUtils {
            dialog = Dialog(context, R.style.huahan_dialog)
            val view = View.inflate(context, R.layout.and_dialog_capture_tip, null)
            titleTextView = view.findViewById(R.id.tv_dialog_title) as TextView?
            msgTextView = view.findViewById(R.id.tv_dialog_msg) as TextView?
            cancelTextView = view.findViewById(R.id.tv_dialog_cancel) as TextView?
            sureTextView = view.findViewById(R.id.tv_dialog_sure) as TextView?
            lineView = view.findViewById(R.id.view)
            lineHView = view.findViewById(R.id.line_h)
            dialog!!.setContentView(view)
            val attributes = dialog!!.window!!
                    .attributes
            attributes.width = AndScreenUtils.getScreenWidth(context) - AndDensityUtils.dip2px(context, 30)
            dialog!!.window!!.attributes = attributes
            if (hhDialogUtils == null) {
                hhDialogUtils = com.mtjsoft.www.kotlinmvputils.utils.AndDialogUtils()
            }
            dialog!!.setOnDismissListener {
                dialog = null
                titleTextView = null
                msgTextView = null
                cancelTextView = null
                sureTextView = null
                lineView = null
                lineHView = null
            }
            return hhDialogUtils as AndDialogUtils
        }
    }
}
