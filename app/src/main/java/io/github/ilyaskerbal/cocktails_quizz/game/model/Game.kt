package io.github.ilyaskerbal.cocktails_quizz.game.model

class Game() {
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
}