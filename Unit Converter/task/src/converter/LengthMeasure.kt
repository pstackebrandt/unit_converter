package converter

enum class LengthMeasure(private val plural: String, private val short: String) {
    Meter("Meters", "m"),
    Kilometer("Kilometers", "km"),
    Centimeter("Centimeters", "cm"),
    Millimeter("Millimeters", "mm"),
    Mile("Miles", "mi"),
    Yard("Yards", "yd"),
    Foot("Feet", "ft"),
    Inch("Inches", "in");

    fun isEqualExceptCase(measureValue: String) =
            when (measureValue.toLowerCase()) {
                name.toLowerCase(), plural.toLowerCase(), short.toLowerCase() -> true
                else -> false
            }

    /** Get long measure name in singular or plural. Result depends on [value] and is in lowercase. */
    fun getMeasureText(value: Double): String {
        return if (value == 1.0) {
            name.toLowerCase()
        } else {
            plural.toLowerCase()
        }
    }
}