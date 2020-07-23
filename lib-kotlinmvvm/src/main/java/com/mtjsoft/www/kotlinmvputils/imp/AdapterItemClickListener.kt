package com.mtjsoft.www.kotlinmvputils.imp

import android.view.View

interface AdapterItemClickListener {
    fun onItemClick(position: Int, view: View)
}