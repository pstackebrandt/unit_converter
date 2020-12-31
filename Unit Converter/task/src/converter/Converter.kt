package converter

import java.util.*

class Converter {
    companion object {
        fun convertToMeter() {
            val (number, measure) = askForLength()
            val lengthMeasureInput: LengthMeasure
            try {
                lengthMeasureInput = categorizeMeasure(measure)
            } catch (exc: IllegalArgumentException) {
                println(exc.message)
                return
            }

            try {
                val (meters, lengthMeasure) = convertToMeter(number, lengthMeasureInput)
                println("$number ${lengthMeasureInput.getMeasureText(number)} is $meters ${lengthMeasure.getMeasureText(meters)}")
            } catch (exc: IllegalArgumentException) {
                println(exc.message)
            }
        }

        private fun categorizeMeasure(measure: String): LengthMeasure {
            val work = measure.trim().toLowerCase()

            return when {
                LengthMeasure.Meter.isEqualExceptCase(work) -> LengthMeasure.Meter
                LengthMeasure.Kilometer.isEqualExceptCase(work) -> LengthMeasure.Kilometer
                LengthMeasure.Centimeter.isEqualExceptCase(work) -> LengthMeasure.Centimeter
                LengthMeasure.Millimeter.isEqualExceptCase(work) -> LengthMeasure.Millimeter
                LengthMeasure.Mile.isEqualExceptCase(work) -> LengthMeasure.Mile
                LengthMeasure.Yard.isEqualExceptCase(work) -> LengthMeasure.Yard
                LengthMeasure.Foot.isEqualExceptCase(work) -> LengthMeasure.Foot
                LengthMeasure.Inch.isEqualExceptCase(work) -> LengthMeasure.Inch

                else -> throw IllegalArgumentException("Unexpected value for measure")
            }
        }

        private fun askForLength(): Pair<Double, String> {
            println("Enter a number and a measure of length:")
            val scanner = Scanner(System.`in`)
            val number = scanner.nextDouble()
            val measure = scanner.next()
            return number to measure
        }

        private fun convertToMeter(number: Double, measure: LengthMeasure): Pair<Double, LengthMeasure> {
            return when (measure) {
                LengthMeasure.Meter -> number to LengthMeasure.Meter
                LengthMeasure.Kilometer -> number * 1_000 to LengthMeasure.Meter
                LengthMeasure.Centimeter -> number * 0.01 to LengthMeasure.Meter
                LengthMeasure.Millimeter -> number * 0.001 to LengthMeasure.Meter
                LengthMeasure.Mile -> number * 1609.35 to LengthMeasure.Meter
                LengthMeasure.Yard -> number * 0.9144 to LengthMeasure.Meter
                LengthMeasure.Foot -> number * 0.3048 to LengthMeasure.Meter
                LengthMeasure.Inch -> number * 0.0254 to LengthMeasure.Meter
            }
        }
    }
}

