package kr.or.foresthealing.model

class Api : BaseModel(){
    var data : Array<Data>? = null

    class Data{
        var apiId : Int? = null
        var name : String? = null
        var url : String? = null
        var description : String? = null
        var method : String? = null
        override fun toString(): String {
            return "Data(apiId=$apiId, name=$name, url=$url, description=$description, method=$method)"
        }
    }

    override fun toString(): String {
        return "${super.toString()}, Api(data=${data?.contentToString()})"
    }

}