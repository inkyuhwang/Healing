package kr.or.foresthealing.common

import android.util.Log
import kr.or.foresthealing.BuildConfig

class Hlog {
    companion object {
        private val TAG = Hlog::class.java.simpleName
        fun i(msg: String) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, msg)
        }

        fun e(msg: String) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, msg)
        }

        fun d(msg: String) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, msg)
        }

        fun v(msg: String) {
            if (BuildConfig.DEBUG)
                Log.v(TAG, msg)
        }

        fun w(msg: String) {
            if (BuildConfig.DEBUG)
                Log.w(TAG, msg)
        }
    }
}