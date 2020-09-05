package kr.or.foresthealing.observer

import java.util.*

class CommonObserver :Observable(){
    companion object{
        val instance = CommonObserver()
    }
    fun updateValue(data : Any){
        synchronized(this){
            setChanged()
            notifyObservers(data)
        }
    }
}