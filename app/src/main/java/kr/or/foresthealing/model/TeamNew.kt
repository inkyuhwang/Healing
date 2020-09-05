package kr.or.foresthealing.model

class TeamNew : BaseModel(){
    var data : Data? = null

    class Data{
        var id : Int? = null
        override fun toString(): String {
            return "Data(id=$id)"
        }
    }

    override fun toString(): String {
        return "${super.toString()}, TeamNew(data=$data)"
    }

}