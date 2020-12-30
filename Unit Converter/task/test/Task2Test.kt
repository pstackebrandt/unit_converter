import org.hyperskill.hstest.testcase.CheckResult


/** TestCase, based on authors solution output. */
fun authorsCase(input: String, isPrivate: Boolean = false) =
        authorsCaseFromFun(Authors::solve, input, isPrivate)

class Task2Test : ConverterTest<OutputClue>() {

    override fun generate() = listOf(
            authorsCase("25"),
            authorsCase("26", isPrivate = true),
            authorsCase("13", isPrivate = true)
    )

    override fun check(reply: String, clue: OutputClue): CheckResult {
        // compare output the clue output and reply with our custom comparer.
        val checkResult = WordComparer(clue.output, reply).compare()

        if (clue.isPrivate)
            return checkResult.ciphered()
        return checkResult
    }
}
