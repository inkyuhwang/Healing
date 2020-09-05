package kr.or.foresthealing.common

class Const {
    companion object{
        const val SERVER = "http://foresthealing113.gabia.io"

        const val SAMPLE_VIDEO = "http://foresthealing113.gabia.io/static/video/sampleVideo.mp4"

        const val FINISH_ACTIVITES = -1
        const val REQUEST_IMAGE_CAPTURE = 1

        const val COUNT_DOWN = 9

        //URL
        const val URL_API_LIST = "/api/init"
        const val URL_EXCEPTION = "/api/exceptions/new"
        const val URL_REGIST_TEAM = "/api/team/new"
        const val URL_QUIZLIST_ALL = "/api/question/listAll"

        //QuizType
        const val QUIZ_TYPE_MULTIPLE = "1"
        const val QUIZ_TYPE_SHORT = "2"
        const val QUIZ_ANSWER_CORRECT = "1"
        const val QUIZ_ANSWER_WRONG = "2"

        //Step
        const val STEP_INTRO = 0
        const val STEP_QUIZ = 1
        const val STEP_MAP = 2
        const val STEP_MISSION = 3
        const val STEP_STAMP = 4

    }
}