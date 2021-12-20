package intcode

import kotlin.collections.ArrayDeque

class CommunicationChannel {
    private var values = ArrayDeque<Int>()
    val isEmpty: Boolean
        get() = values.isEmpty()
    fun read(): Int {
        check(!isEmpty) { "Can only read if channel is not empty" }
        return values.removeFirst()
    }
    fun write(value: Int) {
        values.addLast(value)
    }
    fun toList() = values.toList()
}

class Computer(
    initialProgram: List<Int>,
    val input: CommunicationChannel,
    val output: CommunicationChannel
) {
    constructor(initialProgram: List<Int>) : this(initialProgram, CommunicationChannel(), CommunicationChannel())
    val program: MutableList<Int> = initialProgram.toMutableList()
    var pointer: Int = 0
    val completed: Boolean
        get() = program[pointer] == 99
    val awaitingInput: Boolean
        get() = program[pointer] == 3 && input.isEmpty

    private fun getValueAt(position: Int, isImmediate: Boolean): Int =
        if (isImmediate)
            program[position]
        else
            program[program[position]]

    private fun setValueAt(position: Int, value: Int) {
        program[program[position]] = value
    }

    private fun setValueAt(position: Int, valueFun: () -> Int) {
        setValueAt(position, valueFun())
    }

    private fun firstArg() =
        getValueAt(pointer + 1, (program[pointer] / 100) % 10 == 1)

    private fun secondArg() =
        getValueAt(pointer + 2, (program[pointer] / 1000) % 10 == 1)

    private fun thirdArg() =
        getValueAt(pointer + 3, (program[pointer] / 10000) % 10 == 1)

    fun step(): Int {
        return when (program[pointer] % 100) {
            99 -> pointer
            1 -> {
                setValueAt(pointer+3) { firstArg() + secondArg() }
                pointer + 4
            }
            2 -> {
                setValueAt(pointer+3) { firstArg() * secondArg() }
                pointer + 4
            }
            3 -> {
                setValueAt(pointer+1, input.read())
                pointer + 2
            }
            4 -> {
                output.write(firstArg())
                pointer + 2
            }
            5 -> {
                if (firstArg() != 0)
                    secondArg()
                else
                    pointer + 3
            }
            6 -> {
                if (firstArg() == 0)
                    secondArg()
                else
                    pointer + 3
            }
            7 -> {
                setValueAt(pointer+3) { if (firstArg() < secondArg()) 1 else 0 }
                pointer + 4
            }
            8 -> {
                setValueAt(pointer+3) { if (firstArg() == secondArg()) 1 else 0 }
                pointer + 4
            }
            else -> TODO()
        }
    }

    fun debugPrint() {
        println(program.mapIndexed { index, i -> if (index == pointer) "{$i}" else "$i" }.joinToString())
    }

    fun run(debug: Boolean = false): Computer {
        while (!completed && !awaitingInput) {
            if (debug) debugPrint()
            pointer = step()
        }
        if (debug) debugPrint()
        return this
    }

    fun runWithInput(args: List<Int>, debug: Boolean = false): Computer {
        args.forEach {
            input.write(it)
        }
        return run(debug)
    }

    fun runWithInput(arg: Int, debug: Boolean = false): Computer =
        runWithInput(listOf(arg), debug)
}
