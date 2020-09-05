package kr.or.foresthealing.network

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog

class NetworkManager {
    companion object{

        @Volatile private var instance : NetworkManager? = null

        @JvmStatic fun getInstance() : NetworkManager =
            instance ?: synchronized(this){
                instance ?: NetworkManager().also {
                    instance = it
                }
            }
    }

    fun post(url:String, param: RequestParams?, handler: NetworkHandler){
        Hlog.i("request post : ${Const.SERVER}$url , params : ${param.toString()}")

        val client = AsyncHttpClient()
        client.post("${Const.SERVER}$url", param, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                Hlog.i("request post response : ${Const.SERVER}$url , response : ${String(responseBody ?: byteArrayOf())}}")
                if(responseBody != null){
                    handler.onSuccess(String(responseBody))
                }else{
                    handler.onSuccess("")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                handler.onFail(statusCode, if(responseBody == null) "" else String(responseBody))
            }
        })
    }

    fun postSync(url:String, param: RequestParams?, handler: NetworkHandler){
        val client = SyncHttpClient()
        client.post("${Const.SERVER}$url", param, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody != null){
                    handler.onSuccess(String(responseBody))
                }else{
                    handler.onSuccess("")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                handler.onFail(statusCode, if(responseBody == null) "" else String(responseBody))
            }
        })
    }
}
