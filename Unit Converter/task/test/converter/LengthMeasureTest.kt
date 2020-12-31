package converter

import org.junit.Assert
import org.junit.Test

@Suppress("unused")
class LengthMeasureTest {
    class IsEqualTest {
        @Test
        fun `should accept meter short`() {
            val actual = LengthMeasure.Meter.isEqualExceptCase("M")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should accept meter singular`() {
            val actual = LengthMeasure.Meter.isEqualExceptCase("MeTer")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should accept meter plural`() {
            val actual = LengthMeasure.Meter.isEqualExceptCase("MeTerS")
            Assert.assertTrue(actual)
        }

        @Test
        fun `should reject unexpected value`() {
            val actual = LengthMeasure.Meter.isEqualExceptCase("Meister")
            Assert.assertFalse(actual)
        }
    }

    class GetMeasureTextTest {
        @Test
        fun `should return singular`() {
            val actual = LengthMeasure.Meter.getMeasureText(1.0)
            Assert.assertEquals("meter", actual)
        }

        @Test
        fun `should return plural`() {
            val actual = LengthMeasure.Meter.getMeasureText(1.1)
            Assert.assertEquals("meters", actual)
        }
    }
}