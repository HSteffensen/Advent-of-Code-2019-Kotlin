package day09

import finalAnswerIsNotWrong
import intcode.Computer
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
        Computer(input).apply { this.input.write(1) }.run().output.also { println(it.toList()) }.read()

    fun part2(input: List<Long>): Long =
        (5..9).toList().permutations()
            .maxOf { amplifyLoop(input, it) }

    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day09/test"))
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }
    Computer(listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)).run().output.also { println(it.toList()) }

    val input = intcodeInput(readInput("day09/input"))
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
