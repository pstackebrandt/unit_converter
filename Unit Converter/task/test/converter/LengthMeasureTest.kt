package converter

import org.junit.Assert
import org.junit.Test

@Suppress("unused")
class LengthMeasureTest {
    class IsEqualTest {
        @Test
        fun `should accept meter short`() {
            val actual = LengthUnit.Meter.isEqualMeaning("M")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should accept meter singular`() {
            val actual = LengthUnit.Meter.isEqualMeaning("MeTer")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should accept meter plural`() {
            val actual = LengthUnit.Meter.isEqualMeaning("MeTerS")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should reject unexpected value`() {
            val actual = LengthUnit.Meter.isEqualMeaning("Meister")
            Assert.assertFalse(actual)
        }
    }

    class IsExitTest {
        @Test
        fun `should detect exit`() {
            val actual = Converter.isExit("1", "brexit", "4")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should not detect exit`() {
            val actual = Converter.isExit("1", "brexi", "4")
            Assert.assertFalse(actual)
        }

    }

    class GetMeasureTextTest {
        @Test
        fun `should return singular`() {
            val actual = LengthUnit.Meter.getLongName(1.0)
            Assert.assertEquals("meter", actual)
        }

        @Test
        fun `should return plural`() {
            val actual = LengthUnit.Meter.getLongName(1.1)
            Assert.assertEquals("meters", actual)
        }
    }
}