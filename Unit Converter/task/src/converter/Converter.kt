package converter

import java.util.*

class Converter {
    companion object {
        fun convertKmToM() {
            val kilometer = askForKm()
            val meter = convertKmToM(kilometer)
            println("$kilometer kilometers is $meter meters")
        }

        private fun askForKm(): Int {
            println("Enter a number of kilometers:")
            val scanner = Scanner(System.`in`)
            return scanner.nextInt()
        }

        private fun convertKmToM(km: Int) = km * 1_000
    }
}
