package io.github.ilyaskerbal.cocktails_quizz

import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import io.github.ilyaskerbal.cocktails_quizz.game.model.Score
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

private const val CORRECT_ANSWER = "CORRECT"
private const val INCORRECT_ANSWER = "INCORRECT"
private const val INVALID = "INVALID"

class GameUnitTests {

    private lateinit var game : Game
    private val question1 : Question = Question(CORRECT_ANSWER, INCORRECT_ANSWER)
    private val question2 : Question = Question(CORRECT_ANSWER, INCORRECT_ANSWER)

    @Before
    fun setup() {
        game = Game(listOf(question1, question2))
    }

    @Test
    fun nextQuestion_shouldReturnNextQuestion() {
        var nextQuestion = game.nextQuestion()

        assertThat(nextQuestion, `is`(question1))

        nextQuestion = game.nextQuestion()

        assertThat(nextQuestion, `is`(question2))
    }

    @Test
    fun nextQuestion_emptyQuestionList_shouldReturnNull() {
        game = Game()

        val nextQuestion = game.nextQuestion()

        assertThat("if there are no questions, the next question must return null", nextQuestion, nullValue())
    }

    /**
     * Mockito Tests
     * */

    @Test
    fun gameAnswer_shouldDelegateToQuestion() {
        val question = mock<Question>()
        game = Game(listOf(question))

        game.answer(question, CORRECT_ANSWER)

        verify(question, times(1)).answer(eq(CORRECT_ANSWER)) // We can omit `times(1)`
    }

    @Test
    fun gameAnswer_correctAnswer_shouldIncreaseScore() {
        val question = mock<Question>()
        val score = mock<Score>()
        whenever(question.answer(anyString())).thenReturn(true) // We can specify `CORRECT_ANSWER` instead of anyString()

        game = Game(listOf(question), score)

        game.answer(question, CORRECT_ANSWER)

        verify(score, times(1)).increment()
    }

    @Test
    fun gameAnswer_incorrectAnswer_shouldNotIncrementScore() {
        val question = mock<Question>()
        val score = mock<Score>()

        whenever(question.answer(anyString())).thenReturn(false)

        game = Game(listOf(question), score)

        game.answer(question, INCORRECT_ANSWER)

        verify(score, never()).increment()
    }
}