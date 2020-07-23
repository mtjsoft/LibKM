package com.mtjsoft.www.kotlinmvputils.imp

interface ItemTouchHelpAdapter {
    //定义移动方法
    fun itemMove(fromPosition: Int, toPosition: Int)

    //定义移除方法
    fun itemDelete(position: Int)
}