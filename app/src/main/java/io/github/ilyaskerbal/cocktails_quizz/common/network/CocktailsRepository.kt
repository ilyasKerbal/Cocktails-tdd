package io.github.ilyaskerbal.cocktails_quizz.common.network

interface CocktailsRepository {

    fun saveHighScore(score: Int)
    fun getHighScore() : Int
    fun getAlcoholic(callback: RepositoryCallback)

    interface RepositoryCallback {
        fun onSuccess(cocktailList: List<Cocktail>)
        fun onError(e: String)
    }
}