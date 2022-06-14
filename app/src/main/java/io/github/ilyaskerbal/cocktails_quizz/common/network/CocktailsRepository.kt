package io.github.ilyaskerbal.cocktails_quizz.common.network

interface CocktailsRepository {

    fun saveHighScore(score: Int)
    fun getHighScore() : Int
}