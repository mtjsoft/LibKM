package com.mtjsoft.www.kotlinmvputils.utils

import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment

import java.lang.ref.WeakReference

/**
 * 该类继承自Handler，在内部使用了一个WeakReference来引用Activity或者Fragment，
 * 通过对对象的若引用来防止因为多线程的原因引起的内存泄漏问题
 * @author yuan
 *
 * @param <T>
</T> */
abstract class HHWeakHandler<T>(t: T) : Handler() {
    /**
     * 保存了T的一个弱引用，防止因为多线的原因造成内存泄漏
     */
    private val mFragmentReference: WeakReference<T>  = WeakReference(t)

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (mFragmentReference.get() == null) {
            return
        }
        if (mFragmentReference.get() is Fragment) {
            val fragment = mFragmentReference.get() as Fragment
            if (fragment.context == null) {
                return
            }
        }
        processHandlerMessage(msg)
    }

    /**
     * 处理消息,在该方法执行之前首先是判断了Fragment是否被销毁，如果fragment被销毁了，
     * 则不会执行该方法，直接退出
     * @param msg            发送的消息
     */
    abstract fun processHandlerMessage(msg: Message)
}
