package io.github.ilyaskerbal.cocktails_quizz

import io.github.ilyaskerbal.cocktails_quizz.game.model.Game
import io.github.ilyaskerbal.cocktails_quizz.game.model.Score
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

class ScoreUnitTests {

    @Test
    fun incrementScore_incrementingScore_shouldIncrementCurrentScore() {
        val score = Score()

        val previousScore = score.current

        score.increment()

        val expectedScore = previousScore + 1

        assertThat(
            "Current score should have been $expectedScore",
            score.current,
            equalTo(expectedScore)
        )
    }

    @Test
    fun incrementScore_incrementingScore_shouldChangeHighestScore() {
        val score = Score()

        val previousHighScore = score.highest

        score.increment()

        val currentHighScore = score.highest

        assertThat(
            "New high score should be greater than previous high score",
            currentHighScore,
            greaterThan(previousHighScore)
        )
    }

    @Test
    fun incrementScore_incrementingScore_shouldNotChangeHighestScore_ifLowerThanHighScore() {
        val score = Score(10)

        val previousHighScore = score.highest

        score.increment()

        val currentHighScore = score.highest

        assertThat(
            "increment score should not change high score if current score is lower",
            currentHighScore,
            equalTo(previousHighScore)
        )
    }
}