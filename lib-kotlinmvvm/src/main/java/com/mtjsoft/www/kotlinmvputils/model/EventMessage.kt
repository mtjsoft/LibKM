package com.mtjsoft.www.kotlinmvputils.model

/**
 * EventBus消息体分装
 * @author mtj
 * @date 2018-9-28 17:12:09
 */
class EventMessage {
    private var code: Int = 0
    private var msg: String = "Msg"
    private var data: Any = "EventBus"

    /**
     * 构造函数
     */
    constructor(code: Int) {
        this.code = code
    }

    constructor(msg: String, data: Any) {
        this.data = data
        this.msg = msg
    }

    constructor(code: Int, msg: String, data: Any) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    fun getCode(): Int = code

    fun getMsg(): String = msg

    fun getData(): Any = data

    override fun toString(): String {
        return "EventMessage(code=$code, msg='$msg', data=$data)"
    }

}