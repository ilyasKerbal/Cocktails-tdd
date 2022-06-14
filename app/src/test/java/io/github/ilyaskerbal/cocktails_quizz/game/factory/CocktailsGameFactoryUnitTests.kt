package io.github.ilyaskerbal.cocktails_quizz.game.factory

import io.github.ilyaskerbal.cocktails_quizz.common.network.Cocktail
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepository
import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CocktailsGameFactoryUnitTests {

    private val cocktails = listOf(
        Cocktail("1", "Drink1", "image1"),
        Cocktail("2", "Drink2", "image2"),
        Cocktail("3", "Drink3", "image3"),
        Cocktail("4", "Drink4", "image4")
    )

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory

    @Before
    fun setup() {
        repository = mock()
        factory = CocktailsGameFactoryImpl(repository)
    }

    @Test
    fun buildGame_shouldGetCocktailsFromRepo() {
        factory.buildGame(mock())

        verify(repository).getAlcoholic(any())
    }

    private fun setUpRepositoryWithCocktails(repository: CocktailsRepository) {
        doAnswer {
            val callback: CocktailsRepository.RepositoryCallback = it.getArgument(0)
            callback.onSuccess(cocktails)
        }.whenever(repository).getAlcoholic(any())
    }

    @Test
    fun buildGame_shouldCallOnSuccess(){
        val callback = mock<CocktailsGameFactory.Callback>()
        setUpRepositoryWithCocktails(repository)

        factory.buildGame(callback)

        verify(callback).onSuccess(any())
    }

    private fun setUpRepositoryWithError(repository: CocktailsRepository) {
        doAnswer {
            val callback : CocktailsRepository.RepositoryCallback = it.getArgument(0)
            callback.onError("Error")
        }.whenever(repository).getAlcoholic(any())
    }

    @Test
    fun buildGame_shouldReturnOnError() {
        val callback = mock<CocktailsGameFactory.Callback>()
        setUpRepositoryWithError(repository)

        factory.buildGame(callback)

        verify(callback).onError()
    }

    @Test
    fun buildGame_shouldGetHighScoreFromRepo() {
        setUpRepositoryWithCocktails(repository)

        factory.buildGame(mock())

        verify(repository).getHighScore()
    }

    @Test
    fun buildGame_shouldBuildGameWithHighScore() {
        setUpRepositoryWithCocktails(repository)
        val highScore : Int = 100
        whenever(repository.getHighScore()).thenReturn(highScore)

        factory.buildGame(object : CocktailsGameFactory.Callback {
            override fun onSuccess(game: Game) = assertThat(game.highestScore, equalTo(highScore))

            override fun onError() = assertThat("Failed to build game", false)
        })
    }

    private fun assertQuestion(question: Question?, correctOption: String, imageUrl: String?) {
        assertThat(question, notNullValue())
        assertThat(imageUrl, equalTo(question?.imageUrl))
        assertThat(correctOption, equalTo(question?.correctOption))
        assertThat(correctOption, not(equalTo(question?.incorrectOption)))
    }

    @Test
    fun buildGame_shouldBuildGameWithQuestions() {
        setUpRepositoryWithCocktails(repository)

        factory.buildGame(object : CocktailsGameFactory.Callback {
            override fun onSuccess(game: Game) = cocktails.forEach {
                assertQuestion(game.nextQuestion(), it.strDrink, it.strDrinkThumb)
            }

            override fun onError() = assertThat("Failed to build game", false)
        })
    }

}