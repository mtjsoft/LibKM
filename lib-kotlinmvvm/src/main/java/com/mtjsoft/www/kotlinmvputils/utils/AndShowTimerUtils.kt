package com.mtjsoft.www.kotlinmvputils.utils

import android.os.CountDownTimer
import android.widget.TextView
import com.mtjsoft.www.kotlinmvputils.R

class AndShowTimerUtils {

    /**
     * @param view    显示倒计时textView
     */
    fun showTimer(view: TextView, time: Int) {

        object : CountDownTimer((time * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                view.isClickable = false// 设置不能点击
                val show = "${(millisUntilFinished / 1000)}s后重新获取"
                view.text = show// 设置倒计时时间
            }

            override fun onFinish() {
                view.setText(R.string.user_send_code)
                view.isClickable = true// 重新获得点击
            }
        }.start()

    }

    companion object {

        private var timerUtils: AndShowTimerUtils? = null

        val instence: AndShowTimerUtils
            get() {
                if (timerUtils == null) {
                    timerUtils = AndShowTimerUtils()
                }
                return timerUtils!!
            }
    }
}
