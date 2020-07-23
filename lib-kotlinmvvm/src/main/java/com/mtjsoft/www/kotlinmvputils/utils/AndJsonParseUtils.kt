package com.mtjsoft.www.kotlinmvputils.utils

import android.text.TextUtils

import org.json.JSONException
import org.json.JSONObject

object AndJsonParseUtils {

    /**
     * 获取服务器返回的结果码 网络错误时返回-1
     *
     * @return
     */
    fun getResponceStatus(result: String): Int {
        return getResponceCode(result, "code")
    }


    fun getResponceResult(result: String): Boolean {
        return getResponceResult(result, "result")
    }


    /**
     * 获取服务器返回的msg
     *
     * @return
     */
    fun getResponceMsg(result: String): String {
        return getParamInfo(result, "msg")
    }

    /**
     * 获取服务器返回的结果码 网络错误时返回-1
     *
     * @param codeName 结果码的标识
     * @return
     */
    fun getResponceCode(result: String, codeName: String = "code"): Int {
        var code = -1
        if (!TextUtils.isEmpty(result)) {
            try {
                val jsonObject = JSONObject(result)
                code = Integer.valueOf(jsonObject.getString(codeName))!!
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return code
    }

    fun getResponceResult(result: String, resultName: String = "result"): Boolean {
        var code = true
        if (!TextUtils.isEmpty(result)) {
            try {
                val jsonObject = JSONObject(result)
                code = jsonObject.getBoolean(resultName)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return code
    }

    fun getParamInfo(data: String, paramName: String, senName: String): String {
        if (!TextUtils.isEmpty(data)) {
            try {
                val jsonObject = JSONObject(data)
                val firstResult = jsonObject.optString(paramName)
                return if (TextUtils.isEmpty(firstResult)) {
                    ""
                } else {
                    val json = JSONObject(firstResult)
                    val result = json.optString(senName)
                    if (TextUtils.isEmpty(result)) {
                        ""
                    } else {
                        result
                    }
                }
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return ""
    }

    fun getParamInfo(data: String, paramName: String): String {
        if (!TextUtils.isEmpty(data)) {
            try {
                val jsonObject = JSONObject(data)
                val result = jsonObject.optString(paramName)
                return if (TextUtils.isEmpty(result)) {
                    ""
                } else result
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return ""
    }
}
