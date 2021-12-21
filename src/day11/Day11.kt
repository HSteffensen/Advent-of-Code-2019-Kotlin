package day11

import finalAnswerIsNotWrong
import intcode.Computer
import intcodeInput
import readInput
import testAnswer
import kotlin.system.measureTimeMillis

class PaintGrid() {
    var painterLocation = Pair(0, 0)
    var painterFacing = Pair(0, -1)
    val grid = mutableMapOf<Pair<Int, Int>, Int>()

    fun move(color: Int, turn: Int) {
        grid[painterLocation] = color
        painterFacing = if (turn == 1) // right
                Pair(painterFacing.second, -painterFacing.first)
            else // left
                Pair(-painterFacing.second, painterFacing.first)
        painterLocation = Pair(
            painterLocation.first + painterFacing.first,
            painterLocation.second + painterFacing.second
        )
    }

    fun colorAtPainter() = grid.getOrDefault(painterLocation, 0)
}

tailrec fun paintProgram(paintGrid: PaintGrid, computer: Computer): PaintGrid =
    if (computer.isCompleted)
        paintGrid
    else
        paintProgram(
            paintGrid.apply {
                move(
                    computer.run().output.read().toInt(),
                    computer.run().output.read().toInt()
                )
            },
            computer.apply {
                input.write(paintGrid.colorAtPainter().toLong())
            }
        )

fun MutableMap<Pair<Int, Int>, Int>.gridString() =
    (minOf { it.key.second }..maxOf { it.key.second }).joinToString("\n") { y ->
        (maxOf { it.key.first } downTo minOf { it.key.first }).joinToString("") { x ->
            when (this.getOrDefault(Pair(x, y), -1)) {
                0 -> "."
                1 -> "#"
                else -> " "
            }
        }
    }

fun main() {
    fun part1(input: List<Long>): Int =
        paintProgram(
            PaintGrid(),
            Computer(input).apply { this.input.write(0) }
        ).grid.size

    fun part2(input: List<Long>): String =
        "\n" + paintProgram(
            PaintGrid(),
            Computer(input).apply { this.input.write(1) }
        ).grid.gridString()

    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day11/test"))
//    testAnswer(part1(testInput), 0).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day11/input"))
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
