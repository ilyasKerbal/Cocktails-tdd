package io.github.ilyaskerbal.cocktails_quizz.game.model

class Question(val correctOption: String, val incorrectOption: String) {
    var answeredOption: String? = null
        private set

    val isAnsweredCorrectly : Boolean
        get() = correctOption == answeredOption

    fun answer(response: String) : Boolean {
        if (response != correctOption && response != incorrectOption) throw IllegalArgumentException("Invalid answer")
        answeredOption = response
        return isAnsweredCorrectly
    }

    fun getOptions(predicate: (List<String>) -> List<String> = {it.shuffled()})
        = predicate(listOf(correctOption, incorrectOption))
}