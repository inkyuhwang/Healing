package kr.or.foresthealing.network

import org.json.JSONObject

interface NetworkHandler {
    fun onSuccess(result:String)
    fun onFail(statusCode : Int, result:String)
}