package io.github.ilyaskerbal.cocktails_quizz.common.repository

import android.content.SharedPreferences
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsApi
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepository
import io.github.ilyaskerbal.cocktails_quizz.common.network.CocktailsRepositoryImpl
import io.github.ilyaskerbal.cocktails_quizz.common.network.HIGH_SCORE_KEY
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

class RepositoryUnitTests {

    private lateinit var cocktailsApi : CocktailsApi
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var cocktailsRepository: CocktailsRepository

    @Before
    fun setup() {
        cocktailsApi = mock<CocktailsApi>()
        sharedPrefs = mock<SharedPreferences>()
        editor = mock<SharedPreferences.Editor>()

        /** We did not mock the repo */
        cocktailsRepository = CocktailsRepositoryImpl(cocktailsApi, sharedPrefs)

        whenever(sharedPrefs.edit()).thenReturn(editor)
    }

    @Test
    fun saveScore_shouldSaveToSharedPreferences() {
        val score = 100
        cocktailsRepository.saveHighScore(score)

        inOrder(editor) {
            verify(editor).putInt(eq(HIGH_SCORE_KEY), eq(score)) // We can use anyString for the the test
            verify(editor).apply()
        }
    }

    @Test
    fun getScore_shouldGetFromSharedPreferences() {
        val score = cocktailsRepository.getHighScore()

        verify(sharedPrefs).getInt(eq(HIGH_SCORE_KEY), anyInt())
    }

    /**
     * Using a spy will let you call the methods of a real object,
     * while also tracking every interaction,
     * just as you would do with a mock.
     * */
    @Test
    fun saveScore_lowerScore_shouldNotSaveToSharedPreferences() {
        val previousHighScore: Int = 100
        val newHighScore : Int = 10
        val spyRepo = spy(cocktailsRepository)
        doReturn(previousHighScore).whenever(spyRepo).getHighScore()

        spyRepo.saveHighScore(newHighScore)

        verify(editor, never()).putInt(eq(HIGH_SCORE_KEY), anyInt())
    }

}