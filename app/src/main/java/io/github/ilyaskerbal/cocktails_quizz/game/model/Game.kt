package io.github.ilyaskerbal.cocktails_quizz.game.model

class Game (qs: List<Question> = listOf<Question>()) {

    private lateinit var _questions : MutableList<Question>

    val questions : List<Question>
        get() = _questions.toList()

    init {
        _questions = qs.toMutableList()
    }

    var currentScore : Int = 0
        private set

    var highestScore : Int = 0
        private set

    fun incrementScore() {
        currentScore++
        if (currentScore > highestScore) highestScore = currentScore
    }

    constructor (highest: Int) : this() {
        highestScore = highest
    }

    fun nextQuestion() : Question? = _questions.removeFirstOrNull()

    fun answer(question: Question, answer: String) {
        val response = question.answer(answer)
        if (response) incrementScore()
    }
}