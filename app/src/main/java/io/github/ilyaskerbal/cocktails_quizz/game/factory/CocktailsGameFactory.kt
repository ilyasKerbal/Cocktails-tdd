package io.github.ilyaskerbal.cocktails_quizz.game.factory

import io.github.ilyaskerbal.cocktails_quizz.game.model.Game

interface CocktailsGameFactory {

    fun buildGame(callback: Callback)

    interface Callback {
        fun onSuccess(game: Game)
        fun onError()
    }
}