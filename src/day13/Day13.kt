package day13

import finalAnswerIsNotWrong
import intcode.Computer
import intcodeInput
import readInput
import testAnswer
import kotlin.math.sign
import kotlin.system.measureTimeMillis

class Game(val computer: Computer) {
    val grid: MutableMap<Pair<Long, Long>, Long> = mutableMapOf()
    var score: Long = 0

    fun addTile(x: Long, y: Long, value: Long) {
        grid[Pair(x, y)] = value
    }

    fun run() {
        while (!computer.isCompleted) {
            computer.run()
            computer.output.toList()
                .chunked(3)
                .map { Triple(it[0], it[1], it[2]) }
                .forEach { (x, y, value) ->
                    if (x == -1L && y == 0L)
                        score = value
                    else
                        addTile(x, y, value)
                }
            println(screenString())
            computer.input.write(
                (grid.filterValues { it == 4L }.toList().single().first.first -
                        grid.filterValues { it == 3L }.toList().single().first.first).sign.toLong()
            )
        }
    }

    fun screenString(): String =
        "$score\n" +
        (grid.minOf { it.key.second }..grid.maxOf { it.key.second }).joinToString("\n") { y ->
            (grid.minOf { it.key.first }..grid.maxOf { it.key.first }).joinToString("") { x ->
                when (grid.getOrDefault(Pair(x, y), 0)) {
                    1L -> "#"
                    2L -> "X"
                    3L -> "="
                    4L -> "o"
                    else -> " "
                }
            }
        } + "\n"

}

fun main() {
    fun part1(input: List<Long>): Int =
        Game(Computer(input))
            .apply { run() }
            .grid
            .count { it.value == 2L }

    fun part2(input: List<Long>): Long =
        Game(Computer(input.mapIndexed { index, value -> if (index == 0) 2 else value }))
            .apply { run() }
            .score

    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day13/test"))
//    testAnswer(part1(testInput), 0).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day13/input"))
    val wrongPart1Answers = listOf<Int>(
    )
    measureTimeMillis {
        println("Part 1: ${finalAnswerIsNotWrong(part1(input), wrongPart1Answers)}")
    }.also { println("\ttook $it milliseconds") }

    val wrongPart2Answers = listOf<Int>(
    )
    measureTimeMillis {
        println("Part 2: ${finalAnswerIsNotWrong(part2(input), wrongPart2Answers)}")
    }.also { println("\ttook $it milliseconds") }
}
