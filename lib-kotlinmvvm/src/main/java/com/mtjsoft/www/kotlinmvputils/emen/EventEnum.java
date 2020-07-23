package com.mtjsoft.www.kotlinmvputils.emen;

/**
 * EventBus消息枚举类
 */
public enum EventEnum {
    LOGIN_OUT_SU(-3, "用户登出了"),
    LOGIN_SU(-2, "用户已登录"),
    UPDATE_SYSTEMTIME(-1, "系统时间更新了"),
    BluetoothDevice_ACTION_FOUND(0, "扫描到蓝牙设备"),
    BluetoothDevice_ACTION_STARTED(1, "扫描蓝牙设备开始"),
    BluetoothDevice_ACTION_FINISHED(2, "扫描蓝牙设备结束"),

    WorkOrder_Receive(100, "工单签收成功");

    private int code;
    private String msg;

    EventEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
