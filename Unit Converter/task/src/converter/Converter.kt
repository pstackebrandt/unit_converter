package converter

import java.util.*
import kotlin.NoSuchElementException

class Converter {
    companion object {
        fun convert() {
            while (true) {
                val (valueText, fromUnitText, toUnitText) = getInputCleaned()
                if (isExit(valueText, fromUnitText, toUnitText)) {
                    return
                }
                val value = valueText.toDoubleOrNull()
                if (value == null) {
                    println("Parse error")
                    continue
                }

                val fromUnit = getUnit(fromUnitText)
                val toUnit = getUnit(toUnitText)

                if (isUnknownUnitError(fromUnit, toUnit)) {
                    println(getUnknownUnitErrorMessage(fromUnit, toUnit))
                    continue
                }

                if (value < 0 &&
                        (fromUnit is LengthUnit || fromUnit is MassUnit)) {
                    println("${if (fromUnit is LengthUnit) "Length" else "Weight"} shouldn't be negative.")
                    continue
                }

                try {
                    val (resultValue, resultUnit) = convert(value, fromUnit, toUnit)
                    println(getMessageWithConversionResult(value, fromUnit, resultValue, resultUnit))
                } catch (exc: ConversionOfIncompatibleTypesException) {
                    println(getMessageConversionOfDifferentUnitTypesImpossible(fromUnit, toUnit))
                } catch (exc: IllegalArgumentException) {
                    println(exc.message)
                }
            }
        }

        private fun getMessageWithConversionResult(value: Double, fromUnit: Unit, resultValue: Double, resultUnit: Unit) =
                "$value ${fromUnit.getLongName(value)} is $resultValue ${resultUnit.getLongName(resultValue)}"

        private fun getMessageConversionOfDifferentUnitTypesImpossible(fromUnit: Unit, toUnit: Unit) =
                "Conversion from ${fromUnit.plural.toLowerCase()} to ${toUnit.plural.toLowerCase()} is impossible"

        private fun isUnknownUnitError(fromUnit: Unit, toUnit: Unit) =
                fromUnit == UnitError.UnknownUnit || toUnit == UnitError.UnknownUnit

        private fun getUnknownUnitErrorMessage(fromUnit: Unit, toUnit: Unit) =
                "Conversion from ${
                    getLongUnitNameOrUnknownUnitSign(fromUnit)
                } to ${
                    getLongUnitNameOrUnknownUnitSign(toUnit)
                } is impossible"

        private fun getLongUnitNameOrUnknownUnitSign(fromUnit: Unit) =
                if (fromUnit == UnitError.UnknownUnit) {
                    "???"
                } else {
                    fromUnit.plural.toLowerCase()
                }

        private fun getUnit(fromUnitText: String): Unit {
            return try {
                categorizeUnit(fromUnitText)
            } catch (exc: IllegalArgumentException) {
                UnitError.UnknownUnit
            }
        }

        internal fun isExit(vararg text: String): Boolean {
            text.forEach {
                if (it.toLowerCase().contains("exit")) {
                    return true
                }
            }
            return false
        }

        /** may throw IllegalArgumentException */
        private fun categorizeUnit(unit: String): Unit {
            val cleanUnit = unit.trim().toLowerCase()

            return try {
                getLengthUnit(cleanUnit)
            } catch (exc: IllegalArgumentException) {
                return try {
                    getMassUnit(cleanUnit)
                } catch (exc: IllegalArgumentException) {
                    //println("call getTemperatureUnit($cleanUnit)")
                    getTemperatureUnit(cleanUnit)
                }
            }
        }

        private fun getLengthUnit(unitText: String): LengthUnit {
            return when {
                LengthUnit.Meter.isEqualMeaning(unitText) -> LengthUnit.Meter
                LengthUnit.Kilometer.isEqualMeaning(unitText) -> LengthUnit.Kilometer
                LengthUnit.Centimeter.isEqualMeaning(unitText) -> LengthUnit.Centimeter
                LengthUnit.Millimeter.isEqualMeaning(unitText) -> LengthUnit.Millimeter
                LengthUnit.Mile.isEqualMeaning(unitText) -> LengthUnit.Mile
                LengthUnit.Yard.isEqualMeaning(unitText) -> LengthUnit.Yard
                LengthUnit.Foot.isEqualMeaning(unitText) -> LengthUnit.Foot
                LengthUnit.Inch.isEqualMeaning(unitText) -> LengthUnit.Inch
                else -> throw IllegalArgumentException("Unexpected value for unit")
            }
        }

        private fun getMassUnit(unitText: String): MassUnit {
            return when {
                MassUnit.Gram.isEqualMeaning(unitText) -> MassUnit.Gram
                MassUnit.Kilogram.isEqualMeaning(unitText) -> MassUnit.Kilogram
                MassUnit.Milligram.isEqualMeaning(unitText) -> MassUnit.Milligram
                MassUnit.Pound.isEqualMeaning(unitText) -> MassUnit.Pound
                MassUnit.Ounce.isEqualMeaning(unitText) -> MassUnit.Ounce

                else -> throw IllegalArgumentException("Unexpected value for unit")
            }
        }

        private fun getTemperatureUnit(unitText: String): TemperatureUnit {
            return when {
                TemperatureUnit.Celsius.isEqualMeaning(unitText) -> TemperatureUnit.Celsius
                TemperatureUnit.Kelvin.isEqualMeaning(unitText) -> TemperatureUnit.Kelvin
                TemperatureUnit.Fahrenheit.isEqualMeaning(unitText) -> TemperatureUnit.Fahrenheit
                else -> throw IllegalArgumentException("Unexpected value for unit")
            }
        }

        /** Get number, fromUnit, toUnit */
        private fun getInputCleaned(): Triple<String, String, String> {
            println("Enter what you want to convert (or exit):")
            val scanner = Scanner(System.`in`)
            val number = nextAndClean(scanner)
            if (isExit(number)) {
                return getExitTriple()
            }
            var fromUnit = nextAndClean(scanner)
            if (isDegree(fromUnit)) {
                fromUnit = nextAndClean(scanner)
            }
            scanner.next() // unused word
            var toUnit = nextAndClean(scanner)
            if (isDegree(toUnit)) {
                toUnit = nextAndClean(scanner)
            }
            return Triple(number, fromUnit, toUnit)
        }

        private fun nextAndClean(scanner: Scanner) = scanner.next().trim().toLowerCase()

        private fun isDegree(fromUnit: String) = fromUnit.toLowerCase().contains("degree")

        private fun getExitTriple() = Triple("exit", "", "")

        /** Convert [number] from [sourceUnit] to [destinationUnit]
         * throws IllegalArgumentException*/
        private fun convert(number: Double, sourceUnit: Unit, destinationUnit: Unit):
                Pair<Double, Unit> {
            if (!(sourceUnit is LengthUnit && destinationUnit is LengthUnit) xor
                    (sourceUnit is MassUnit && destinationUnit is MassUnit) xor
                    (sourceUnit is TemperatureUnit && destinationUnit is TemperatureUnit)) {
                throw ConversionOfIncompatibleTypesException("Units have different type.")
            }

            val (baseValue, _) = when (sourceUnit) {
                is LengthUnit -> convertToMeter(sourceUnit, number)
                is MassUnit -> convertToGram(sourceUnit, number)
                is TemperatureUnit -> convertToCelsius(sourceUnit, number)
                else -> throw IllegalArgumentException("Unexpected unit type")
            }

            return when (sourceUnit) {
                is LengthUnit -> convertFromMeter(destinationUnit, baseValue)
                is MassUnit -> convertFromGram(destinationUnit, baseValue)
                is TemperatureUnit -> convertFromCelsius(destinationUnit, baseValue)
                else -> throw IllegalArgumentException("Unexpected unit type")
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertToMeter(sourceUnit: Unit, number: Double): Pair<Double, LengthUnit> {
            return when (sourceUnit) {
                LengthUnit.Meter -> number to LengthUnit.Meter
                LengthUnit.Kilometer -> number * 1_000 to LengthUnit.Meter
                LengthUnit.Centimeter -> number * 0.01 to LengthUnit.Meter
                LengthUnit.Millimeter -> number * 0.001 to LengthUnit.Meter
                LengthUnit.Mile -> number * 1609.35 to LengthUnit.Meter
                LengthUnit.Yard -> number * 0.9144 to LengthUnit.Meter
                LengthUnit.Foot -> number * 0.3048 to LengthUnit.Meter
                LengthUnit.Inch -> number * 0.0254 to LengthUnit.Meter
                else -> throw NoSuchElementException()
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertToGram(sourceUnit: Unit, number: Double): Pair<Double, MassUnit> {
            return when (sourceUnit) {
                MassUnit.Gram -> number to MassUnit.Gram
                MassUnit.Kilogram -> number * 1_000 to MassUnit.Gram
                MassUnit.Milligram -> number * 0.001 to MassUnit.Gram
                MassUnit.Pound -> number * 453.592 to MassUnit.Gram
                MassUnit.Ounce -> number * 28.3495 to MassUnit.Gram
                else -> throw NoSuchElementException()
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertToCelsius(sourceUnit: Unit, number: Double): Pair<Double, TemperatureUnit> {
            return when (sourceUnit) {
                TemperatureUnit.Celsius -> number to TemperatureUnit.Celsius
                TemperatureUnit.Kelvin -> number - 273.15 to TemperatureUnit.Celsius
                TemperatureUnit.Fahrenheit -> (number - 32) * 5 / 9 to TemperatureUnit.Celsius
                else -> throw NoSuchElementException()
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertFromMeter(destinationUnit: Unit, number: Double): Pair<Double, LengthUnit> {
            return when (destinationUnit) {
                LengthUnit.Meter -> number to LengthUnit.Meter
                LengthUnit.Kilometer -> number * 0.001 to LengthUnit.Kilometer
                LengthUnit.Centimeter -> number * 100 to LengthUnit.Centimeter
                LengthUnit.Millimeter -> number * 1_000 to LengthUnit.Millimeter
                LengthUnit.Mile -> number * (1 / 1609.35) to LengthUnit.Mile
                LengthUnit.Yard -> number * (1 / 0.9144) to LengthUnit.Yard
                LengthUnit.Foot -> number * (1 / 0.3048) to LengthUnit.Foot
                LengthUnit.Inch -> number * (1 / 0.0254) to LengthUnit.Inch
                else -> throw NoSuchElementException()
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertFromGram(destinationUnit: Unit, number: Double): Pair<Double, MassUnit> {
            return when (destinationUnit) {
                MassUnit.Gram -> number to MassUnit.Gram
                MassUnit.Kilogram -> number * 0.001 to MassUnit.Kilogram
                MassUnit.Milligram -> number * 1_000 to MassUnit.Milligram
                MassUnit.Pound -> number * (1 / 453.592) to MassUnit.Pound
                MassUnit.Ounce -> number * (1 / 28.3495) to MassUnit.Ounce
                else -> throw NoSuchElementException()
            }
        }

        /** may throw NoSuchElementException() */
        private fun convertFromCelsius(destinationUnit: Unit, number: Double): Pair<Double, TemperatureUnit> {
            return when (destinationUnit) {
                TemperatureUnit.Celsius -> number to TemperatureUnit.Celsius
                TemperatureUnit.Kelvin -> number + 273.15 to TemperatureUnit.Kelvin
                TemperatureUnit.Fahrenheit -> number * 9 / 5 + 32 to TemperatureUnit.Fahrenheit
                else -> throw NoSuchElementException()
            }
        }
    }
}

