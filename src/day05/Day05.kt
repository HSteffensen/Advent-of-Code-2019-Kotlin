package day05

import finalAnswerIsNotWrong
import intcode.Computer
import intcodeInput
import readInput
import testAnswer
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<Long>): Long =
        Computer(input).runWithInput(1).output.toList()
            .single { it != 0L }

    fun part2(input: List<Long>): Long =
        Computer(input).runWithInput(5).output.toList()
            .single { it != 0L }



    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day05/test"))
    testAnswer(Computer(intcodeInput("1002,4,3,4,33")).run().program[4], 99).also { println("Test part 1 passed") }
    testAnswer(Computer(intcodeInput("1101,100,-1,4,0")).run().program[4], 99).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }
    // < and = tests
    testAnswer(Computer(intcodeInput("3,9,8,9,10,9,4,9,99,-1,8")).runWithInput(8).output.read(), 1).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,9,8,9,10,9,4,9,99,-1,8")).runWithInput(0).output.read(), 0).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,9,7,9,10,9,4,9,99,-1,8")).runWithInput(8).output.read(), 0).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,9,7,9,10,9,4,9,99,-1,8")).runWithInput(0).output.read(), 1).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1108,-1,8,3,4,3,99")).runWithInput(8).output.read(), 1).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1108,-1,8,3,4,3,99")).runWithInput(0).output.read(), 0).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1107,-1,8,3,4,3,99")).runWithInput(8).output.read(), 0).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1107,-1,8,3,4,3,99")).runWithInput(0).output.read(), 1).also { println("Test part 2 passed") }
    // jump tests
    testAnswer(Computer(intcodeInput("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9")).runWithInput(8).output.read(), 1).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9")).runWithInput(0).output.read(), 0).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1105,-1,9,1101,0,0,12,4,12,99,1")).runWithInput(8).output.read(), 1).also { println("Test part 2 passed") }
    testAnswer(Computer(intcodeInput("3,3,1105,-1,9,1101,0,0,12,4,12,99,1")).runWithInput(0).output.read(), 0).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day05/input"))
    val wrongPart1Answers = listOf<Int>(
        3,
        0
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
