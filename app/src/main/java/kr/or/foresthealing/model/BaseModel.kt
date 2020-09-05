package kr.or.foresthealing.model

open class BaseModel {
    open var result :String? = null
    open var message :String? = null

    override fun toString(): String {
        return "BaseModel(result=$result, message=$message)"
    }


}