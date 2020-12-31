import java.io.*
import java.util.*


data class Measure(
        val short: String,
        val normal: String,
        val plural: String,
        val multiplier: Double
) {
    fun name(amount: Double) = if (amount == 1.0) normal else plural
}


val lengths = listOf(
        Measure("m", "meter", "meters", 1.0),
        Measure("km", "kilometer", "kilometers", 1000.0),
        Measure("cm", "centimeter", "centimeters", 0.01),
        Measure("mm", "millimeter", "millimeters", 0.001),
        Measure("mi", "mile", "miles", 1609.35),
        Measure("yd", "yard", "yards", 0.9144),
        Measure("ft", "foot", "feet", 0.3048),
        Measure("in", "inch", "inches", 0.0254)
)

val namesToMeasures = lengths
        .flatMap { m ->
            listOf(m.short, m.normal, m.plural).map { name -> name to m }
        }.toMap()

fun solveAuthors(sin: Scanner, sout: PrintStream) {



    val regex = Regex("""(\d+.?\d*) (\w+)[\n\r]*""")

    sout.print("Enter a number and a measure of length: ")
    val line = sin.nextLine().toLowerCase()

    val (_, valueStr, name1) = regex.matchEntire(line)!!.groups.map { it!!.value }

    val value = valueStr.toDouble()
    val m1 = namesToMeasures[name1]!!
    val m2 = namesToMeasures["m"]!!

    val converted = value * m1.multiplier

    sout.println("$value ${m1.name(value)} is $converted ${m2.name(converted)}")
}


fun main(args: Array<String>) {
    solveAuthors(Scanner(System.`in`), System.out)
}
