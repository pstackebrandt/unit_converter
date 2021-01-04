package converter

interface Unit {
    val singular: String
    val plural: String
    val short: List<String>
    val name: String

    /** Check ignores case. */
    fun isEqualMeaning(unitText: String): Boolean {
        val cleanText = unitText.toLowerCase()

        return when {
            cleanText == singular -> true
            cleanText == plural -> true
            cleanText == name.toLowerCase() -> true
            short.contains(cleanText) -> true
            else -> false
        }
    }

    /** Get long unit name in singular or plural. Result depends on [value] and is in lowercase. */
    fun getLongName(value: Double) =
            if (value == 1.0) {
                singular
            } else {
                plural
            }
}

enum class LengthUnit(override val singular: String, override val plural: String, override val short: List<String>) : Unit {
    Meter("meter", "meters", listOf("m")),
    Kilometer("kilometer", "kilometers", listOf("km")),
    Centimeter("centimeter", "centimeters", listOf("cm")),
    Millimeter("millimeter", "millimeters", listOf("mm")),
    Mile("mile", "miles", listOf("mi")),
    Yard("yard", "yards", listOf("yd")),
    Foot("foot", "feet", listOf("ft")),
    Inch("inch", "inches", listOf("in"))
}

enum class MassUnit(override val singular: String, override val plural: String, override val short: List<String>) : Unit {
    Gram("gram", "grams", listOf("g")),
    Kilogram("kilogram", "kilograms", listOf("kg")),
    Milligram("milligram", "milligrams", listOf("mg")),
    Pound("pound", "pounds", listOf("lb")),
    Ounce("ounce", "ounces", listOf("oz")),
}

enum class TemperatureUnit(override val singular: String, override val plural: String, override val short: List<String>) : Unit {
    Celsius("degree Celsius", "degrees Celsius", listOf("dc", "c")),
    Kelvin("degree Kelvin", "degrees Kelvin", listOf("dk", "k")),
    Fahrenheit("degree Fahrenheit", "degrees Fahrenheit", listOf("df", "f")),
}

enum class UnitError(override val singular: String, override val plural: String, override val short: List<String>) : Unit {
    UnknownUnit("???", "???", listOf("???")),
}
