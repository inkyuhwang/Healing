package kr.or.foresthealing.model

class Quiz : BaseModel(){
    var data : Array<Data> = arrayOf()

    class Data{
        var question_id : Int = -1
        var title : String = ""
        var content : String = ""
        var type : String = ""
        var map : String = ""
        var guide : String = ""
        var video : String = ""
        var hint : String = ""
        var complete : Boolean = false

        var example : Array<Example> = arrayOf()
        override fun toString(): String {
            return "Data(question_id=$question_id, title='$title', content='$content', type='$type', map='$map', guide='$guide', video='$video', hint='$hint', complete=$complete, example=${example?.contentToString()})"
        }
    }

    class Example{
        var example_id : Int = -1
        var question_id : Int = -1
        var content : String = ""
        var isAnswer : String = ""
        override fun toString(): String {
            return "Example(example_id=$example_id, question_id=$question_id, content='$content', isAnswer='$isAnswer')"
        }
    }

    override fun toString(): String {
        return "${super.toString()}, Quiz(data=${data?.contentToString()})"
    }

}