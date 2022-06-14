package io.github.ilyaskerbal.cocktails_quizz.game.factory

import io.github.ilyaskerbal.cocktails_quizz.common.network.Cocktail
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepository
import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import io.github.ilyaskerbal.cocktails_quizz.game.model.Score

class CocktailsGameFactoryImpl(
    private val repository: CocktailsRepository) : CocktailsGameFactory {

    override fun buildGame(callback: CocktailsGameFactory.Callback) {
        repository.getAlcoholic(object : CocktailsRepository.RepositoryCallback {
            override fun onSuccess(cocktailList: List<Cocktail>) {
                val questions = buildQuestions(cocktailList)
                val score = Score(repository.getHighScore())
                val game = Game(questions, score)
                callback.onSuccess(game)
            }

            override fun onError(e: String) {
                callback.onError()
            }
        })
    }

    private fun buildQuestions(cocktailList: List<Cocktail>) : List<Question> = cocktailList.map { cocktail ->
        val otherCocktail = cocktailList.shuffled().first{it != cocktail}
        Question(cocktail.strDrink, otherCocktail.strDrink, cocktail.strDrinkThumb)
    }
}