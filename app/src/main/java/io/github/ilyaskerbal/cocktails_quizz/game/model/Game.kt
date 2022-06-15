package io.github.ilyaskerbal.cocktails_quizz.game.model

class Game (qs: List<Question> = listOf<Question>(), score: Score = Score(0)) {

    private lateinit var _questions : MutableList<Question>

    val questions : List<Question>
        get() = _questions.toList()

    init {
        _questions = qs.toMutableList()
    }

    private val _score = score
    val score : Score
        get() = _score

    val currentScore : Int
        get() = _score.current

    val highestScore : Int
        get() = _score.highest

    constructor (highScore: Int) : this(score = Score(10))

    fun nextQuestion() : Question? = _questions.removeFirstOrNull()

    fun answer(question: Question, answer: String) {
        val response = question.answer(answer)
        if (response) _score.increment()
    }
}