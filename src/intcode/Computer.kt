package intcode

import kotlin.collections.ArrayDeque
import kotlin.math.pow

class CommunicationChannel {
    private var values = ArrayDeque<Long>()
    val isEmpty: Boolean
        get() = values.isEmpty()
    fun read(): Long {
        check(!isEmpty) { "Can only read if channel is not empty" }
        return values.removeFirst()
    }
    fun write(value: Long) {
        values.addLast(value)
    }
    fun toList() = values.toList()
}

class Computer(
    initialProgram: List<Long>,
    val input: CommunicationChannel,
    val output: CommunicationChannel
) {
    constructor(initialProgram: List<Long>) : this(initialProgram, CommunicationChannel(), CommunicationChannel())
    val program: MutableMap<Long, Long> = initialProgram.mapIndexed { index, it -> index.toLong() to it }.toMap().toMutableMap().withDefault { 0 }
    var pointer: Long = 0
    var relativeBase: Long = 0
    val isCompleted: Boolean
        get() = program[pointer] == 99L
    val isAwaitingInput: Boolean
        get() = program[pointer] == 3L && input.isEmpty
    val programString: String
        get() = program.map { (index, i) -> if (index == pointer) "{$i}" else "$i" }.joinToString()

    private fun argModeOfOp(argNum: Int): Int = (program.getOrDefault(pointer, 0) / (10 * 10.0.pow(argNum).toInt())).toInt() % 10

    private fun getValueAt(position: Long, mode: Int): Long =
        when (mode) {
            0 -> program.getOrDefault(program.getOrDefault(position, 0), 0)
            1 -> program.getOrDefault(position, 0)
            2 -> program.getOrDefault(relativeBase + position, 0)
            else -> throw IllegalStateException("Illegal GET mode: $mode")
        }

    private fun setValueAt(position: Long, mode: Int, value: Long) {
        when (mode) {
            0 -> program[program.getOrDefault(position, 0)] = value
            2 -> program[relativeBase + position] = value
            else -> throw IllegalStateException("Illegal SET mode: $mode")
        }

    }

    private fun getArg(argNum: Int) = getValueAt(pointer + argNum, argModeOfOp(argNum))

    private fun setArg(argNum: Int, value: Long) {
        setValueAt(pointer + argNum, argModeOfOp(argNum), value)
    }

    private fun setArg(argNum: Int, valueFun: () -> Long) {
        setValueAt(pointer + argNum, argModeOfOp(argNum), valueFun())
    }

    fun step(): Long {
        return when ((program.getOrDefault(pointer, 0) % 100).toInt()) {
            99 -> pointer
            1 -> {
                setArg(3) { getArg(1) + getArg(2) }
                pointer + 4
            }
            2 -> {
                setArg(3) { getArg(1) * getArg(2) }
                pointer + 4
            }
            3 -> {
                setArg(1, input.read())
                pointer + 2
            }
            4 -> {
                output.write(getArg(1))
                pointer + 2
            }
            5 -> {
                if (getArg(1) != 0L)
                    getArg(2)
                else
                    pointer + 3
            }
            6 -> {
                if (getArg(1) == 0L)
                    getArg(2)
                else
                    pointer + 3
            }
            7 -> {
                setArg(3) { if (getArg(1) < getArg(2)) 1 else 0 }
                pointer + 4
            }
            8 -> {
                setArg(3) { if (getArg(1) == getArg(2)) 1 else 0 }
                pointer + 4
            }
            9 -> {
                relativeBase += getArg(1)
                pointer + 2.also { println(relativeBase) }
            }
            else -> TODO()
        }
    }

    fun debugPrint() {
        println(programString)
    }

    fun run(debug: Boolean = false): Computer {
        while (!isCompleted && !isAwaitingInput) {
            if (debug) debugPrint()
            pointer = step()
        }
        if (debug) debugPrint()
        return this
    }

    fun runWithInput(args: List<Long>, debug: Boolean = false): Computer {
        args.forEach {
            input.write(it)
        }
        return run(debug)
    }

    fun runWithInput(arg: Long, debug: Boolean = false): Computer =
        runWithInput(listOf(arg), debug)
}

class ComputerChain private constructor(){
    val computers = mutableListOf<Computer>()

    fun add(initialProgram: List<Long>) =
        computers.add(Computer(initialProgram, computers.last().output, CommunicationChannel()))

    fun addAndLoopToFront(initialProgram: List<Long>) =
        computers.add(Computer(initialProgram, computers.last().output, computers.first().input))

    fun runUntilAllComplete(debug: Boolean = false) {
        while (computers.any { !it.isCompleted }) {
            computers.forEach { it.run(debug) }
        }
    }

    fun runUntilLastCompletes(debug: Boolean = false) {
        while (!computers.last().isCompleted) {
            computers.forEach { it.run(debug) }
        }
    }

    fun runUntilLastOutputs(debug: Boolean = false) {
        while (computers.last().output.isEmpty) {
            computers.forEach { it.run(debug) }
        }
    }

    companion object {
        fun startWithComputer(computer: Computer): ComputerChain =
            ComputerChain().apply { computers.add(computer) }

        fun multiChain(initialProgram: List<Long>, quantity: Int): ComputerChain =
            ComputerChain().apply {
                computers.add(Computer(initialProgram))
                (1 until quantity).forEach { _ ->
                    add(initialProgram)
                }
            }

        fun multiChainLoop(initialProgram: List<Long>, quantity: Int): ComputerChain =
            ComputerChain().apply {
                computers.add(Computer(initialProgram))
                (2 until quantity).forEach { _ ->
                    add(initialProgram)
                }
                addAndLoopToFront(initialProgram)
            }
    }
}
