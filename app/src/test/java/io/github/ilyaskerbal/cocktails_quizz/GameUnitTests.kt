package io.github.ilyaskerbal.cocktails_quizz

import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Question
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
    fun incrementScore_incrementingScore_shouldIncrementCurrentScore() {
        val expectedScore = game.currentScore + 1

        game.incrementScore()

        Assert.assertEquals("Current score should have been $expectedScore", expectedScore, game.currentScore)
    }

    @Test
    fun incrementScore_incrementingScore_shouldChangeHighestScore() {
        val previousHighScore = game.highestScore

        game.incrementScore()

        val currentHighScore = game.highestScore

        Assert.assertEquals("New high score should be greater than previous high score",true, currentHighScore > previousHighScore)
    }

    @Test
    fun incrementScore_incrementingScore_shouldNotChangeHighestScore_ifLowerThanHighScore() {
        game = Game(10)

        val previousHighScore = game.highestScore

        game.incrementScore()

        val currentHighScore = game.highestScore

        Assert.assertEquals("increment score should not change high score if current score is lower", true, currentHighScore == previousHighScore)
    }

    @Test
    fun nextQuestion_shouldReturnNextQuestion() {
        var nextQuestion = game.nextQuestion()

        Assert.assertEquals(question1, nextQuestion)

        nextQuestion = game.nextQuestion()

        Assert.assertEquals(question2, nextQuestion)
    }

    @Test
    fun nextQuestion_emptyQuestionList_shouldReturnNull() {
        game = Game()

        val nextQuestion = game.nextQuestion()

        Assert.assertNull(nextQuestion)
    }
}