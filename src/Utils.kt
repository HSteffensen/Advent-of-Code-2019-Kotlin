import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun intcodeInput(input: List<String>) = intcodeInput(input.single())
fun intcodeInput(input: String) = input.split(',').map { it.toLong() }

inline fun <reified T> finalAnswerIsNotWrong(answer: T, wrongAnswers: List<T>): T =
    answer
        .also {
            check(!wrongAnswers.contains(it)) { "Wrong answer: $answer" }
        }

inline fun <reified T> testAnswer(answer: T, expectedAnswer: T) =
    check(answer == expectedAnswer) { "wrong test. expected: $expectedAnswer, got: $answer" }
