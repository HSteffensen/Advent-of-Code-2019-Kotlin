package day07

import finalAnswerIsNotWrong
import intcode.ComputerChain
import intcodeInput
import readInput
import testAnswer
import kotlin.system.measureTimeMillis

fun amplify(program: List<Long>, arrangement: List<Int>): Long =
    ComputerChain.multiChain(program, arrangement.size).apply {
        arrangement.zip(computers).forEach { (value, computer) ->
            computer.input.write(value.toLong())
        }
        computers.first().input.write(0)
        runUntilLastOutputs()
    }.computers.last().output.read()

fun amplifyLoop(program: List<Long>, arrangement: List<Int>): Long =
    ComputerChain.multiChainLoop(program, arrangement.size).apply {
        arrangement.zip(computers).forEach { (value, computer) ->
            computer.input.write(value.toLong())
        }
        computers.first().input.write(0)
        runUntilAllComplete()
    }.computers.last().output.read()

fun List<Int>.permutations(): List<List<Int>> =
    if (size == 1)
        listOf(this)
    else
        this.drop(1).permutations().flatMap { perm ->
            perm.indices.map { perm.subList(0, it) + first() + perm.subList(it, perm.size) } +
                listOf(
                    perm + listOf(first())
                )
        }

fun main() {
    fun part1(input: List<Long>): Long =
        (0..4).toList().permutations()
            .maxOf { amplify(input, it) }

    fun part2(input: List<Long>): Long =
        (5..9).toList().permutations()
            .maxOf { amplifyLoop(input, it) }

    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day07/test"))
    testAnswer(part1(intcodeInput("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0")), 43210).also { println("Test part 1 passed") }
    testAnswer(part1(intcodeInput("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0")), 54321).also { println("Test part 1 passed") }
    testAnswer(part1(intcodeInput("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0")), 65210).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }
    testAnswer(part2(intcodeInput("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5")), 139629729).also { println("Test part 2 passed") }
    testAnswer(part2(intcodeInput("3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10")), 18216).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day07/input"))
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
