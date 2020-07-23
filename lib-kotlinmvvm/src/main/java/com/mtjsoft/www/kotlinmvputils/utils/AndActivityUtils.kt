package com.mtjsoft.www.kotlinmvputils.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.*
import kotlin.collections.HashMap

/**
 *
 * @author mtj
 */
class AndActivityUtils {

    /**
     * 添加Activity,并监听生命周期变化
     */
    fun addActivity(activity: AppCompatActivity) {
        val tag = "mtj"
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate(owner: LifecycleOwner) {
                activityLifecycle[activity] = "CREATE"
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart(owner: LifecycleOwner) {
                activityLifecycle[activity] = "START"
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume(owner: LifecycleOwner) {
                activityLifecycle[activity] = "RESUME"
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause(owner: LifecycleOwner) {
                activityLifecycle[activity] = "PAUSE"
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop(owner: LifecycleOwner) {
                activityLifecycle[activity] = "STOP"
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy(owner: LifecycleOwner) {
                activityLifecycle.remove(activity)
            }
        })
        aliveActivity.add(activity)
    }

    /**
     * 移除
     */
    fun removeActivity(activity: Activity) {
        activityLifecycle.remove(activity)
        aliveActivity.remove(activity)
    }

    /**
     * 关闭倒数n-1个页面
     * （-1是为了排除当前界面，因为倒数第一个就是当前显示的界面）
     * @param num
     */
    fun closeActivity(num: Int) {
        if (aliveActivity.size > num) {
            for (i in aliveActivity.size - num until aliveActivity.size - 1) {
                val activity = aliveActivity[i]
                activity.finish()
            }
        }
    }

    /**
     * 关闭所有页面
     */
    fun clearActivity() {
        for (activity in aliveActivity) {
            activity.finish()
        }
        aliveActivity.clear()
        activityLifecycle.clear()
    }

    /**
     * 获取活跃的Activity栈表
     */
    fun getAliveActivity(): ArrayList<Activity> {
        return aliveActivity
    }

    /**
     * 返回单个Activity
     */
    fun getAliveActivityByPosition(position: Int): Activity? {
        if (position >= 0 && position < aliveActivity.size) {
            return aliveActivity[position]
        }
        return null
    }

    /**
     * 返回栈顶的Activity
     */
    fun getTopAliveActivity(): Activity? {
        if (aliveActivity.size > 0) {
            return aliveActivity[aliveActivity.size - 1]
        }
        return null
    }

    /**
     * 获取生命周期
     */
    fun getLifecycleState(activity: Activity): String {
        if (activityLifecycle.containsKey(activity)) {
            return activityLifecycle[activity].toString()
        }
        return ""
    }

    /**
     * 获取栈顶的Activity生命周期
     */
    fun getTopLifecycleState(): String {
        if (aliveActivity.size > 0) {
            val activity = aliveActivity[aliveActivity.size - 1]
            if (activityLifecycle.containsKey(activity)) {
                return activityLifecycle[activity].toString()
            }
        }
        return ""
    }

    companion object {
        private val aliveActivity = ArrayList<Activity>()
        private var manager: AndActivityUtils? = null
        private val activityLifecycle: HashMap<Activity, String> = HashMap<Activity, String>()

        val instance: AndActivityUtils
            @Synchronized get() {
                if (manager == null) {
                    manager = com.mtjsoft.www.kotlinmvputils.utils.AndActivityUtils()
                }
                return manager!!
            }
    }
}
