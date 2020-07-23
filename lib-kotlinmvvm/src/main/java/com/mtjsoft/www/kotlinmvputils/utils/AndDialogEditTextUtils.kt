package com.mtjsoft.www.kotlinmvputils.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.mtjsoft.www.kotlinmvputils.R
import com.mtjsoft.www.kotlinmvputils.imp.AndDialogClickListener

/**
 * 对话框操作的工具类
 *
 * @author mtj
 */
class AndDialogEditTextUtils : Builder() {
    override fun buildCancelText(cancelText: String): Builder {
        cancelTextView!!.text = cancelText
        return hhDialogUtils!!
    }

    override fun buildSureText(sureText: String): Builder {
        sureTextView!!.text = sureText
        return hhDialogUtils!!
    }

    override fun buildHideAll(showAll: Boolean): Builder {
        return hhDialogUtils!!
    }

    override fun buildTitle(title: String): Builder {
        return hhDialogUtils!!
    }

    override fun buildMsg(msg: String): Builder {
        msgTextView!!.setText(msg)
        msgTextView!!.setSelection(msg.length)
        msgTextView!!.isFocusableInTouchMode = true
        msgTextView!!.isFocusable = true
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
                sureClickListener!!.onClick(dialog!!, msgTextView!!)
            }
        }
        return hhDialogUtils!!
    }

    override fun buildSureTextColor(colorId: Int): Builder {
        sureTextView!!.setTextColor(thisContext!!.resources.getColor(colorId))
        cancelTextView!!.setTextColor(thisContext!!.resources.getColor(colorId))
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
        thisContext = null
    }

    companion object {
        private var thisContext: Context? = null
        private var hhDialogUtils: AndDialogEditTextUtils? = null
        private var dialog: Dialog? = null
        private var msgTextView: EditText? = null
        private var cancelTextView: TextView? = null
        private var sureTextView: TextView? = null
        private var lineView: View? = null

        fun builder(context: Context): AndDialogEditTextUtils {
            thisContext = context
            dialog = Dialog(context, R.style.huahan_dialog)
            val view = View.inflate(context, R.layout.and_dialog_capture_input, null)
            msgTextView = view.findViewById(R.id.tv_dialog_msg) as EditText?
            cancelTextView = view.findViewById(R.id.tv_dialog_cancel) as TextView?
            sureTextView = view.findViewById(R.id.tv_dialog_sure) as TextView?
            lineView = view.findViewById(R.id.view)
            dialog!!.setContentView(view)
            val attributes = dialog!!.window!!
                    .attributes
            attributes.width = AndScreenUtils.getScreenWidth(context) - AndDensityUtils.dip2px(context, 30)
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.attributes = attributes
            if (hhDialogUtils == null) {
                hhDialogUtils = com.mtjsoft.www.kotlinmvputils.utils.AndDialogEditTextUtils()
            }
            return hhDialogUtils as AndDialogEditTextUtils
        }
    }
}
