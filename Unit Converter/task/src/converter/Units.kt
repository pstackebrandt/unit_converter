package converter

interface Unit {
    val plural: String
    val short: String
    val name: String

    /** Check ignores case. */
    fun isEqualMeaning(unitText: String) =
            when (unitText.toLowerCase()) {
                name.toLowerCase(), plural.toLowerCase(), short.toLowerCase() -> true
                else -> false
            }

    /** Get long unit name in singular or plural. Result depends on [value] and is in lowercase. */
    fun getLongName(value: Double): String {
        return if (value == 1.0) {
            name.toLowerCase()
        } else {
            plural.toLowerCase()
        }
    }
}

enum class LengthUnit(override val plural: String, override val short: String) : Unit {
    Meter("Meters", "m"),
    Kilometer("Kilometers", "km"),
    Centimeter("Centimeters", "cm"),
    Millimeter("Millimeters", "mm"),
    Mile("Miles", "mi"),
    Yard("Yards", "yd"),
    Foot("Feet", "ft"),
    Inch("Inches", "in")
}

enum class MassUnit(override val plural: String, override val short: String) : Unit {
    Gram("grams", "g"),
    Kilogram("kilograms", "kg"),
    Milligram("milligrams", "mg"),
    Pound("pounds", "lb"),
    Ounce("ounces", "oz"),
}

enum class UnitError(override val plural: String, override val short: String) : Unit {
    UnknownUnit("unknown units", "???"),
}

