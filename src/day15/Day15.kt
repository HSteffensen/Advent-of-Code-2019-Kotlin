package day15

import finalAnswerIsNotWrong
import intcode.Computer
import intcodeInput
import readInput
import kotlin.system.measureTimeMillis

typealias Position = Pair<Int, Int>
typealias RobotStep = Long
fun RobotStep.opposite(): Long = when (this) {
    1L -> 2
    2L -> 1
    3L -> 4
    4L -> 3
    else -> throw IllegalStateException("only 1, 2, 3, and 4 are legal instructions")
}

fun Computer.makeStep(instruction: RobotStep): Long {
    check(!isCompleted) { "completed robot cannot make a step" }
    input.write(instruction)
    run()
    return output.read()
}

fun Position.withMove(step: RobotStep): Position = when (step) {
    1L -> Position(this.first, this.second - 1)
    2L -> Position(this.first, this.second + 1)
    3L -> Position(this.first - 1, this.second)
    4L -> Position(this.first + 1, this.second)
    else -> throw IllegalStateException("only 1, 2, 3, and 4 are legal instructions")
}

fun Position.neighbors(): Set<Position> =
    listOf(Position(0,1),Position(0,-1),Position(-1, 0),Position(1, 0)).map { (dx, dy) ->
        Position(first + dx, second + dy)
    }.toSet()

class Robot(val computer: Computer) {
    val grid: MutableMap<Position, Long> = mutableMapOf()
    var currentPosition: Position = Position(0, 0)
    var foundOxygen: Boolean = false

    fun move(step: RobotStep): Boolean {
        val nextPosition = currentPosition.withMove(step)
        val stepResult = computer.makeStep(step)
        grid[nextPosition] = stepResult
        if (stepResult == 0L) return false
        currentPosition = nextPosition
        foundOxygen = stepResult == 2L
        return true
    }

    val gridString: String
        get() = if (grid.isEmpty())
            "robot has empty grid"
        else (grid.minOf { it.key.second }..grid.maxOf { it.key.second }).joinToString("\n") { y ->
            (grid.minOf { it.key.first }..grid.maxOf { it.key.first }).joinToString("") { x ->
                if (Position(x, y) == currentPosition)
                    when (grid[Position(x, y)]) {
                        0L -> "?"
                        1L -> "@"
                        2L -> "!"
                        else -> "%"
                    }
                else
                    when (grid[Position(x, y)]) {
                        0L -> "#"
                        1L -> "."
                        2L -> "0"
                        else -> " "
                    }
            }
        }
}

fun findStepsToOxygen(robot: Robot, stepsTaken: List<Long> = listOf()): List<RobotStep>? {
    if (robot.foundOxygen)
        return stepsTaken
    else {
        listOf(1L, 2L, 3L, 4L).filter { !robot.grid.contains(robot.currentPosition.withMove(it)) }
            .forEach { step ->
                if (robot.move(step))
                    findStepsToOxygen(robot, stepsTaken + step)?.let { return it }
            }
        check(robot.move(stepsTaken.last().opposite())) {
            "Robot should be able to move backwards but couldn't: move ${stepsTaken.last().opposite()}" +
                    " from ${robot.currentPosition} with grid as follows\n${robot.gridString}"
        }
        return null
    }
}

fun exploreEntireGrid(robot: Robot, stepsTaken: List<Long> = listOf()) {
    if (robot.also { println("steps: $stepsTaken\n${robot.gridString}\n") }.computer.isCompleted)
        return
    else {
        listOf(1L, 2L, 3L, 4L).filter { !robot.grid.contains(robot.currentPosition.withMove(it)) }
            .forEach { step ->
                if (robot.move(step))
                    exploreEntireGrid(robot, stepsTaken + step)
            }
        if (stepsTaken.isEmpty()) return
        check(robot.move(stepsTaken.last().opposite())) {
            "Robot should be able to move backwards but couldn't: move ${stepsTaken.last().opposite()}" +
                    " from ${robot.currentPosition} with grid as follows\n${robot.gridString}"
        }
    }
}

tailrec fun spreadOxygenTime(grid: Set<Position>, oxygenLocations: Set<Position>, time: Int = 0): Int =
    if (oxygenLocations == grid.also { println(oxygenGridString(grid, oxygenLocations) + "\n") })
        time
    else
        spreadOxygenTime(
            grid,
            grid.filter { gridPos ->
                oxygenLocations.contains(gridPos) || gridPos.neighbors().any { oxygenLocations.contains(it) }
            }.toSet(),
            time + 1
        )

fun oxygenGridString(grid: Set<Position>, oxygenLocations: Set<Position>): String =
    (grid.minOf { it.second }..grid.maxOf { it.second }).joinToString("\n") { y ->
        (grid.minOf { it.first }..grid.maxOf { it.first }).joinToString("") { x ->
            if (oxygenLocations.contains(Position(x, y)))
                "O"
            else if (grid.contains(Position(x, y)))
                "."
            else
                " "
        }
    }

fun main() {
    fun part1(input: List<Long>): Int =
        findStepsToOxygen(Robot(Computer(input)))!!.size

    fun part2(input: List<Long>): Int =
        Robot(Computer(input)).also { exploreEntireGrid(it) }.also { println("Done exploring grid\n${it.gridString}\nDone exploring grid") }.grid.let { grid ->
            spreadOxygenTime(
                grid.filterValues { it == 1L || it == 2L }.map { it.key }.toSet(),
                grid.filterValues { it == 2L }.map { it.key }.toSet(),
            )
        }

    // test if implementation meets criteria from the description, like:
//    val testInput = intcodeInput(readInput("day15/test"))
//    testAnswer(part1(testInput), 0).also { println("Test part 1 passed") }
//    testAnswer(part2(testInput), 0).also { println("Test part 2 passed") }

    val input = intcodeInput(readInput("day15/input"))
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
