package io.github.ilyaskerbal.cocktails_quizz.common.network

import android.content.SharedPreferences

const val HIGH_SCORE_KEY = "HIGH_SCORE_KEY"

class CocktailsRepositoryImpl(
    private val cocktailsApi: CocktailsApi,
    private val sharedPreferences: SharedPreferences) : CocktailsRepository {

    override fun saveHighScore(score: Int) {
        if (score > getHighScore()) {
            val editor = sharedPreferences.edit()
            editor.putInt(HIGH_SCORE_KEY, score)
            editor.apply()
        }
    }

    override fun getHighScore(): Int = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)

    override fun getAlcoholic(callback: CocktailsRepository.RepositoryCallback) {
        // TODO
    }
}