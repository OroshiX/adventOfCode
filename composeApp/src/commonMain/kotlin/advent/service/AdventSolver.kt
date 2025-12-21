package advent.service

import advent.DayPuzzle
import advent.Part
import advent.days.Day1
import advent.days.Day10
import advent.days.Day11
import advent.days.Day12
import advent.days.Day13
import advent.days.Day14
import advent.days.Day15
import advent.days.Day16
import advent.days.Day17
import advent.days.Day18
import advent.days.Day19
import advent.days.Day2
import advent.days.Day20
import advent.days.Day21
import advent.days.Day22
import advent.days.Day23
import advent.days.Day24
import advent.days.Day25
import advent.days.Day3
import advent.days.Day4
import advent.days.Day5
import advent.days.Day6
import advent.days.Day7
import advent.days.Day8
import advent.days.Day9
import advent.ui.config.ConfigManipulator
import advent.ui.config.MissingCookieException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.util.Scanner
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

interface AdventSolver {
    suspend fun solve(
        dayNumber: Int,
        part: Part,
        debug: Boolean,
        file: File,
        onProgressUpdate: suspend (Progress) -> Unit,
        onUpdateElapsed: suspend (Duration) -> Unit,
    ): Result<AdventResult>
}

data class AdventResult(val result: String, val elapsedTime: Duration)

data class Progress(val current: Int, val max: Int)

@OptIn(ExperimentalTime::class)
internal class AdventSolverImpl(
    private val logger: AdventLogger,
    private val clock: Clock,
    private val configManipulator: ConfigManipulator,
) : AdventSolver {
    override suspend fun solve(
        dayNumber: Int,
        part: Part,
        debug: Boolean,
        file: File,
        onProgressUpdate: suspend (Progress) -> Unit,
        onUpdateElapsed: suspend (Duration) -> Unit,
    ): Result<AdventResult> {
        val day = getDayByNumber(dayNumber)

        try {
            val scanner = Scanner(FileInputStream(file))


            val startTime = clock.now()

            val job = withContext(Dispatchers.Default) {
                launch {
                    while (true) {
                        delay(1.seconds)
                        onUpdateElapsed(clock.now() - startTime)
                    }
                }
            }
            val res =
                day.solve(
                    scanner = scanner,
                    part = part,
                    onProgressUpdate = onProgressUpdate,
                )
            val elapsedTime = clock.now() - startTime
            job.cancel(message = "Finished solving")
            if (debug) {
                val expected = configManipulator.getExpectedResult(dayNumber, part)
                if (expected == res) {
                    logger.success("✅ Debug result is okay")
                } else {
                    logger.danger("❌ Expected $expected but got $res")
                }
            }
            logger.i("Took ${elapsedTime.toString(DurationUnit.MILLISECONDS)} ms to execute")
            logger.i("Result is $res")
            return Result.success(AdventResult(result = res, elapsedTime = elapsedTime))
        } catch (e: MissingCookieException) {
            return Result.failure(e)
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    private fun getDayByNumber(numDay: Int): DayPuzzle<*> = when (numDay) {
        1    -> Day1()
        2    -> Day2()
        3    -> Day3()
        4    -> Day4()
        5    -> Day5()
        6    -> Day6()
        7    -> Day7()
        8    -> Day8()
        9    -> Day9()
        10   -> Day10()
        11   -> Day11()
        12   -> Day12()
        13   -> Day13()
        14   -> Day14()
        15   -> Day15()
        16   -> Day16()
        17   -> Day17()
        18   -> Day18()
        19   -> Day19()
        20   -> Day20()
        21   -> Day21()
        22   -> Day22()
        23   -> Day23()
        24   -> Day24()
        25   -> Day25()
        else -> throw IllegalArgumentException("Day $numDay is not a valid day")
    }

}