package kr.or.foresthealing.common

class Const {
    companion object{
        const val SERVER = "http://foresthealing113.gabia.io"

        const val SAMPLE_VIDEO = "http://foresthealing113.gabia.io/static/video/sampleVideo.mp4"

        const val FINISH_ACTIVITES = -1
        const val REQUEST_IMAGE_CAPTURE = 1


        //URL
        const val URL_API_LIST = "/api/init"
        const val URL_EXCEPTION = "/api/exceptions/new"
        const val URL_REGIST_TEAM = "/api/team/new"
        const val URL_QUIZLIST_ALL = "/api/question/listAll"

        //QuizType
        const val QUIZ_TYPE_MULTIPLE = "1"
        const val QUIZ_TYPE_SHORT = "2"

    }
}