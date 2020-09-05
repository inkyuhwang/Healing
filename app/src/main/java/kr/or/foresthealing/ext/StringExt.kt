package kr.or.foresthealing.ext

import com.google.gson.Gson
import org.json.JSONObject

fun <T> String.parseJsonData(clz: Class<T>): T {
    return Gson().fromJson(this, clz)
}

fun <T> parseJsonData(json: JSONObject, clz: Class<T>?): T {
    return Gson().fromJson(json.toString(), clz)
}
