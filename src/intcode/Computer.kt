package intcode

class Computer(
    initialProgram: List<Int>
) {
    val program: MutableList<Int> = initialProgram.toMutableList()
    var pointer: Int = 0

    fun step(): Int {
        return when (program[pointer]) {
            99 -> pointer
            1 -> {
                program[program[pointer+3]] = program[program[pointer+1]] + program[program[pointer+2]]
                pointer + 4
            }
            2 -> {
                program[program[pointer+3]] = program[program[pointer+1]] * program[program[pointer+2]]
                pointer + 4
            }
            else -> TODO()
        }
    }

    fun debugPrint() {
        println(program.mapIndexed { index, i -> if (index == pointer) "{$i}" else "$i" }.joinToString())
    }

    fun run(debug: Boolean = false): Computer {
        while (program[pointer] != 99) {
            if (debug) debugPrint()
            pointer = step()
        }
        if (debug) debugPrint()
        return this
    }
}
