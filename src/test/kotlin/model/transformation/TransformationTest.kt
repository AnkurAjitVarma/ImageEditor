package model.transformation

import model.transformation.filter.Sharpen
import model.transformation.spatial.HorizontalFlip
import model.transformation.spatial.VerticalFlip
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TransformationTest {

    @Test
    fun testVerticalFlipTransformation() {
        val image = arrayOf(
            arrayOf(arrayOf(1.toUByte(), 2.toUByte()), arrayOf(3.toUByte(), 4.toUByte())),
            arrayOf(arrayOf(5.toUByte(), 6.toUByte()), arrayOf(7.toUByte(), 8.toUByte()))
        )
        val expected = arrayOf(
            arrayOf(arrayOf(5.toUByte(), 6.toUByte()), arrayOf(7.toUByte(), 8.toUByte())),
            arrayOf(arrayOf(1.toUByte(), 2.toUByte()), arrayOf(3.toUByte(), 4.toUByte()))
        )

        val result = VerticalFlip.transform(image)

        assertArrayEquals(expected, result)
    }

    @Test
    fun testHorizontalFlipTransformation() {
        val image = arrayOf(
            arrayOf(arrayOf(1.toUByte(), 2.toUByte()), arrayOf(3.toUByte(), 4.toUByte())),
            arrayOf(arrayOf(5.toUByte(), 6.toUByte()), arrayOf(7.toUByte(), 8.toUByte()))
        )
        val expected = arrayOf(
            arrayOf(arrayOf(3.toUByte(), 4.toUByte()), arrayOf(1.toUByte(), 2.toUByte())),
            arrayOf(arrayOf(7.toUByte(), 8.toUByte()), arrayOf(5.toUByte(), 6.toUByte()))
        )

        val result = HorizontalFlip.transform(image)
        assertArrayEquals(expected, result)
    }
}