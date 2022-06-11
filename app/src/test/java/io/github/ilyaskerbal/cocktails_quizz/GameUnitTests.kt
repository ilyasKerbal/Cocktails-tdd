package io.github.ilyaskerbal.cocktails_quizz

import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GameUnitTests {

    lateinit var game : Game

    @Before
    fun setup() {
        game = Game()
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
}