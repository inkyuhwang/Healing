package kr.or.foresthealing.common

import android.os.Build
import com.loopj.android.http.RequestParams
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class ExceptionHandler : Thread.UncaughtExceptionHandler{

    private var defaultUEH: Thread.UncaughtExceptionHandler? = null

    constructor(){
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {

        try{
            val stackTrace = StringWriter()
            ex.printStackTrace(PrintWriter(stackTrace))
            val param = RequestParams().apply {
                put("title", ex.message)
                put("exceptions", stackTrace.toString())
                put("deviceId", Build.MODEL)
            }

            NetworkManager.getInstance().post("/api/exceptions/new", param, object : NetworkHandler {
                override fun onSuccess(result: String) {
                    finish(thread, ex)
                }

                override fun onFail(statusCode: Int, result: String) {
                    finish(thread, ex)
                }
            })

        }catch(e:Exception){
            e.printStackTrace()
        }finally {
            finish(thread, ex)
        }
    }

    private fun finish(thread: Thread, ex: Throwable){
        defaultUEH?.uncaughtException(thread, ex)
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(10)
    }
}