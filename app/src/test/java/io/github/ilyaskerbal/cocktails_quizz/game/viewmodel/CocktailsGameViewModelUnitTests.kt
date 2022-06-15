package io.github.ilyaskerbal.cocktails_quizz.game.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepository
import io.github.ilyaskerbal.cocktails_quizz.game.factory.CocktailsGameFactory
import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import io.github.ilyaskerbal.cocktails_quizz.game.model.Score
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class CocktailsGameViewModelUnitTests {

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory
    private lateinit var viewModel: CocktailsGameViewModel
    private lateinit var game: Game
    private lateinit var loadingObserver : Observer<Boolean>
    private lateinit var errorObserver : Observer<Boolean>
    private lateinit var scoreObserver : Observer<Score>
    private lateinit var questionObserver : Observer<Question>

    @get: Rule
    val executorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        //1
        repository = mock()
        factory = mock()
        viewModel = CocktailsGameViewModel(repository, factory)

        //2
        game = mock()

        loadingObserver = mock()
        errorObserver = mock()
        scoreObserver = mock()
        questionObserver = mock()

        /**
         * Because there’s no lifecycle here, you can use the observeForever() method.
         * */
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.question.observeForever(questionObserver)
        viewModel.score.observeForever(scoreObserver)
    }

    private fun setUpFactoryWithSuccessGame() {
        doAnswer {
            val callback : CocktailsGameFactory.Callback = it.getArgument(0)
            callback.onSuccess(game)
        }.whenever(factory).buildGame(any())
    }

    private fun setUpFactoryWithError() {
        doAnswer {
            val callback : CocktailsGameFactory.Callback = it.getArgument(0)
            callback.onError()
        }.whenever(factory).buildGame(any())
    }

    @Test
    fun initGame_shouldBuildGame() {
        viewModel.initGame()

        verify(factory).buildGame(any())
    }

    @Test
    fun initGame_shouldShowLoading() {
        viewModel.initGame()

        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun initGame_shouldHideError() {
        viewModel.initGame()

        verify(errorObserver).onChanged(eq(false))
    }

    @Test
    fun initGame_givenFactoryError_shouldShowError() {
        setUpFactoryWithError()

        viewModel.initGame()

        verify(errorObserver).onChanged(eq(true))
    }

    @Test
    fun initGame_givenFactoryError_shouldHideLoading() {
        setUpFactoryWithError()

        viewModel.initGame()

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun initGame_givenFactorySuccess_shouldHideError() {
        setUpFactoryWithSuccessGame()

        viewModel.initGame()

        verify(errorObserver, times(2)).onChanged(eq(false))
    }

    @Test
    fun initGame_givenFactorySuccess_shouldHideLoading() {
        setUpFactoryWithSuccessGame()

        viewModel.initGame()

        inOrder(loadingObserver) {
            verify(loadingObserver).onChanged(eq(true))
            verify(loadingObserver).onChanged(eq(false))
        }
    }

    @Test
    fun initGame_givenFactorySuccess_shouldShowScore() {
        val score = mock<Score>()
        whenever(game.score).thenReturn(score)
        setUpFactoryWithSuccessGame()

        viewModel.initGame()

        verify(scoreObserver).onChanged(eq(score))
    }

    @Test
    fun initGame_givenFactorySuccess_shouldShowFirstQuestion(){
        val question = mock<Question>()
        whenever(game.nextQuestion()).thenReturn(question)
        setUpFactoryWithSuccessGame()

        viewModel.initGame()

        verify(questionObserver).onChanged(eq(question))
    }

    @Test
    fun nextQuestion_shouldShowQuestion() {
        val question1 = mock<Question>()
        val question2 = mock<Question>()

        whenever(game.nextQuestion()).thenReturn(question1).thenReturn(question2)

        setUpFactoryWithSuccessGame()
        viewModel.initGame()

        viewModel.nextQuestion()

        verify(questionObserver).onChanged(eq(question2))
    }

    @Test
    fun answerQuestion_shouldDelegateToGame_saveHighScore_showQuestionAndScore() {
        val score = mock<Score>()
        val question = mock<Question>()
        whenever(game.score).thenReturn(score)
        setUpFactoryWithSuccessGame()

        viewModel.initGame()

        viewModel.answerQuestion(question, "VALUE")

        inOrder(game, repository, scoreObserver, questionObserver) {
            verify(game).answer(eq(question), eq("VALUE"))
            verify(repository).saveHighScore(any())
            verify(scoreObserver).onChanged(eq(score))
            verify(questionObserver).onChanged(eq(question))
        }
    }
}