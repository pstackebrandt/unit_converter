
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

/** TestCase, based on authors solution output. */
fun authorsCase(input: String, isPrivate: Boolean = false): TestCase<OutputClue> {
    val authorsBytes = ByteArrayOutputStream()
    solveAuthors(Scanner(input), PrintStream(authorsBytes, true))
    val authors = authorsBytes.toString()

    return testCase(OutputClue(authors, isPrivate), input)
}

private data class TestMeasure(
        val short: String,
        val normal: String,
        val plural: String,
        val multiplier: Double
)

class Task3Test : ConverterTest<OutputClue>() {

    private fun fullAns(x1: Double, m1: String, x2: Double, m2: String)
            = "Enter a number and a measure of length: \n$x1 $m1 is $x2 $m2"

    override fun generate(): List<TestCase<OutputClue>> {
        val l1 = listOf(
                // tests from the example
                authorsCase("2 km"),
                authorsCase("234 centimeters"),
                authorsCase("1000 millimeters"),
                authorsCase("1 miles"),
                authorsCase("123.456 YARDS"),
                authorsCase("12.9 feet"),
                authorsCase("2.1 IN"),
                // my tests
                // zero
                authorsCase("000 KM"),
                authorsCase("000 cm", isPrivate = true),
                authorsCase("000.0 feet", isPrivate = true),
                authorsCase("00.00 yd", isPrivate = true),

                // lines are compared with WordComparer!
                outputCase("123.456 YaRd", fullAns(123.456, "yards", 112.8881664, "meters")),

                // one meter
                authorsCase("0.001 km"),
                authorsCase("100 cm"),
                authorsCase("1000 mm")
        )
        // iterate all combinations
        val l2 = listOf(
                "m", "meter", "meters",
                "km", "kilometer", "kilometers",
                "cm", "centimeter", "centimeters",
                "mm", "millimeter", "millimeters",
                "mi", "mile", "miles",
                "yd", "yard", "yards",
                "ft", "foot", "feet",
                "in", "inch", "inches"
        ).map {
            authorsCase("100 $it", true)
        }
        return l1 + l2
    }

    override fun check(reply: String, clue: OutputClue): CheckResult {
        // compare output the clue output and reply with our custom comparer.
        val checkResult = WordComparer(clue.output, reply).compare()

        if (clue.isPrivate)
            return checkResult.ciphered()
        return checkResult
    }
}

