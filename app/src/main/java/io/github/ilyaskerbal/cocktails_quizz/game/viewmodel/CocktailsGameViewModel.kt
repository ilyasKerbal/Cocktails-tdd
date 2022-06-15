package io.github.ilyaskerbal.cocktails_quizz.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepository
import io.github.ilyaskerbal.cocktails_quizz.game.factory.CocktailsGameFactory
import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import io.github.ilyaskerbal.cocktails_quizz.game.model.Score

class CocktailsGameViewModel(
    private val repository: CocktailsRepository,
    private val factory: CocktailsGameFactory) : ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean>
        get() = _loadingLiveData

    private val _errorLiveData = MutableLiveData<Boolean>()
    val error : LiveData<Boolean>
        get() = _errorLiveData

    private val _questionLiveData = MutableLiveData<Question>()
    val question : LiveData<Question>
        get() = _questionLiveData

    private val _scoreLiveData = MutableLiveData<Score>()
    val score : LiveData<Score>
        get() = _scoreLiveData

    private var game : Game? = null

    fun initGame() {
        _loadingLiveData.value = true
        _errorLiveData.value = false
        factory.buildGame(object : CocktailsGameFactory.Callback {
            override fun onSuccess(game: Game) {
                _loadingLiveData.value = false
                _errorLiveData.value = false
                _scoreLiveData.value = game.score
                _questionLiveData.value = game.nextQuestion()
                this@CocktailsGameViewModel.game = game
            }

            override fun onError() {
                _loadingLiveData.value = false
                _errorLiveData.value = true
            }
        })
    }

    fun nextQuestion() {
        _questionLiveData.value = game?.nextQuestion()
    }

    fun answerQuestion(question: Question, answer: String) {
        game?.let {
            it.answer(question, answer)
            repository.saveHighScore(it.highestScore)
            _scoreLiveData.value = it.score
            _questionLiveData.value = question
        }
    }
}