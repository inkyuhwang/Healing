package kr.or.foresthealing.application

import android.app.Application
import android.util.Log
import kr.or.foresthealing.common.ExceptionHandler

class MainApplication : Application(){

    private var mDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
    }

}