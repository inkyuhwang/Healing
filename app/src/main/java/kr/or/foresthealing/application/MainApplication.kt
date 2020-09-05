package kr.or.foresthealing.application

import android.app.Application
import android.util.Log
import kr.or.foresthealing.common.ExceptionHandler
import kr.or.foresthealing.common.LocalStorage

class MainApplication : Application(){

    private var mDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()
        LocalStorage.instance.init(applicationContext)
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
    }

}