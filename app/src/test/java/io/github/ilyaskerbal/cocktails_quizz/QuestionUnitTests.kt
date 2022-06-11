package io.github.ilyaskerbal.cocktails_quizz

import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val CORRECT_ANSWER = "CORRECT"
private const val INCORRECT_ANSWER = "INCORRECT"
private const val INVALID = "INVALID"

class QuestionUnitTests {

    private lateinit var question : Question

    @Before
    fun setup(){
        question = Question(CORRECT_ANSWER, INCORRECT_ANSWER)
    }

    @Test
    fun question_creatingQuestion_shouldNotHaveAnsweredOption() {
        Assert.assertNull(question.answeredOption)
    }

    @Test
    fun question_whenAnswered_shouldHaveAnsweredOption() {
        question.answer(INCORRECT_ANSWER)

        Assert.assertEquals(question.answeredOption, INCORRECT_ANSWER)
    }

    @Test
    fun question_whenAnsweredCorrectly_shouldReturnTrue() {
        val result = question.answer(CORRECT_ANSWER)

        Assert.assertTrue(result)
    }

    @Test
    fun question_whenAnsweredIncorrectly_shouldReturnFalse() {
        val result = question.answer(INCORRECT_ANSWER)

        Assert.assertFalse(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun question_whenInvalidAnswer_ShouldThrowException() {
        val result = question.answer(INVALID)
    }

    @Test
    fun getOptions_unsorted_shouldReturnSortedOptions_givenPredicate() {
        val options = question.getOptions { it.reversed() }
        val expected = listOf<String>(INCORRECT_ANSWER, CORRECT_ANSWER)
        Assert.assertEquals(expected, options)
    }
}