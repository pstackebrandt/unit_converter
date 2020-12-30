import java.io.*
import java.util.*


object Authors {
    fun solve(sin: Scanner, sout: PrintStream) {

        sout.print("Enter a number of kilometers: ")

        val x = sin.nextInt()

        sout.println("$x kilometers is ${x * 1000} meters")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        solve(Scanner(System.`in`), System.out)
    }
}
