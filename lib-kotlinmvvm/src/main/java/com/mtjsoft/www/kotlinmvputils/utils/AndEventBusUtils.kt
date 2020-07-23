package com.mtjsoft.www.kotlinmvputils.utils

import com.mtjsoft.www.kotlinmvputils.model.EventMessage
import org.greenrobot.eventbus.EventBus

/**
 * EventBus事件分发二次分装
 * @author mtj
 * @date 2020-5-12 10:34:29
 */
object AndEventBusUtils {
    /**
     * 注册EventBus
     */
    fun register(subscriber: Any) {
        val eventBus = EventBus.getDefault()
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber)
        }
    }

    /**
     * 解除注册
     */
    fun unregister(subscriber: Any) {
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber)
        }
    }

    /**
     * 发送事件消息
     */
    fun post(event: EventMessage) {
        EventBus.getDefault().post(event)
    }

    /**
     * 发送粘性事件消息
     */
    fun postSticky(event: EventMessage) {
        EventBus.getDefault().postSticky(event)
    }
}