package day02

import finalAnswerIsNotWrong
import intcode.Computer
import intcodeInput
import readInput
import testAnswer
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<Long>): Long =
        Computer(input).run().program[0]!!

    fun part2(input: List<Long>): Long {
        (0L..99L).forEach { noun ->
            (0L..99L).forEach { verb ->
                if (part1(input.mapIndexed { index, i -> when (index) {
                        1 -> noun
                        2 -> verb
                        else -> i
                    } }) == 19690720L)
                    return (100 * noun) + verb
            }
        }
        throw IllegalStateException("Should be some solution")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = intcodeInput(readInput("day02/test"))
    testAnswer(part1(testInput), 3500).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day02/input"))
    val wrongPart1Answers = listOf<Int>(
    )
    measureTimeMillis {
        println("Part 1: ${finalAnswerIsNotWrong(part1(input.mapIndexed { index, i -> when (index) {
            1 -> 12
            2 -> 2
            else -> i
        } }), wrongPart1Answers)}")
    }.also { println("\ttook $it milliseconds") }

    val wrongPart2Answers = listOf<Int>(
    )
    measureTimeMillis {
        println("Part 2: ${finalAnswerIsNotWrong(part2(input), wrongPart2Answers)}")
    }.also { println("\ttook $it milliseconds") }
}
