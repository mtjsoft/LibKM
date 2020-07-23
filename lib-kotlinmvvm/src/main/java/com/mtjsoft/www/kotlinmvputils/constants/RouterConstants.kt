package com.mtjsoft.www.kotlinmvputils.constants

object RouterConstants {
    private const val HOME = "/home"
    private const val LOGIN = "/login"
    private const val MINE = "/mine"
    private const val WORKORDER = "/workOrder"
    private const val GIS = "/arcgis"
    private const val BlueTooth = "/BlueTooth"
    private const val DataAsynch = "/DataAsynch"
    private const val MeterTask = "/MeterTask"

    /**
     * 主界面
     */
    const val MAIN_ACTIVITY = "$HOME/MainActivity"

    /**
     * 登录
     */
    const val LOGIN_ACTIVITY = "$LOGIN/loginActivity"

    /**
     * 工单页面
     */
    const val WORKORDER_ACTIVITY = "$WORKORDER/orderListActivity" // 工单列表

    const val WORKORDER_DetailActivity = "$WORKORDER/DetailActivity" // 工单详情

    const val WORKORDER_UPDATEActivity = "$WORKORDER/UPDATEActivity" // 工单上报

    /**
     * 个人中心
     */
    const val MINE_ACTIVITY = "$MINE/MineActivity"

    /**
     * GIS模块
     */
    const val GIS_GISMAP = "$GIS/gismap" // gis地图

    /**
     * 蓝牙模块
     */
    const val BlueTooth_List = "$BlueTooth/BlueToothList" // 蓝牙列表

    /**
     * 数据同步
     */
    const val DataAsynch_index = "$DataAsynch/DataAsynchIndex" // 数据同步主页

    const val DataAsynch_Download = "$DataAsynch/DataAsynchDownload" // 下载任务

    const val DataAsynch_UP = "$DataAsynch/DataAsynchUP" // 上传任务

    /**
     * 抄表任务
     */
    const val MeterTask_MeterList = "$MeterTask/MeterList" // 抄表任务的册本列表

    const val MeterTask_UserTSort = "$MeterTask/UserTSort" // 抄表排序

    const val MeterTask_UserTDetail = "$MeterTask/UserTDetail" // 抄表详情
}