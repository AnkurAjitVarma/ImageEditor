package domain.image

import domain.image.implementation.ImageImpl
import domain.transformation.pixelwise.PixelwiseTransformation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class ImageImplTest {

    private val testImage = Array(2) { Array(2) { Array(3) { 255.toUByte() } } }
    private val image = ImageImpl(testImage)

    @Test
    fun `test height returns correct value`() {
        assertEquals(2, image.height())
    }

    @Test
    fun `test width returns correct value`() {
        assertEquals(2, image.width())
    }

    @Test
    fun `test noOfChannels returns correct value`() {
        assertEquals(3, image.noOfChannels())
    }

    @Test
    fun `test valueAt returns correct value within bounds`() {
        assertEquals(255.toUByte(), image.valueAt(0, 0, 0))
        assertEquals(255.toUByte(), image.valueAt(1, 1, 2))
    }

    @Test
    fun `test valueAt returns null out of bounds`() {
        assertNull(image.valueAt(3, 3, 0))
        assertNull(image.valueAt(0, 0, 3))
    }

    @Test
    fun `test apply transformation applies the transformation`() {
        val mockPixelwiseTransformation = PixelwiseTransformation { image ->
            image.map { row ->
                row.map { pixel ->
                    pixel.map { 0.toUByte() }.toTypedArray()
                }.toTypedArray()
            }.toTypedArray()
        }

        val transformedImage = image.apply(mockPixelwiseTransformation)
        assertEquals(0.toUByte(), transformedImage.valueAt(0, 0, 0))
    }

    @Test
    fun `test valueComponent works correctly for RGB image`() {
        val valueComponent = image.valueComponent()
        assertNotNull(valueComponent)
        val red = image.valueAt(0, 0,0)!!
        val green = image.valueAt(0, 0,1)!!
        val blue = image.valueAt(0, 0, 2)!!
        assertEquals(Image.value(red, green, blue), valueComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test intensityComponent works correctly for RGB image`() {
        val intensityComponent = image.intensityComponent()
        assertNotNull(intensityComponent)
        val red = image.valueAt(0, 0,0)!!
        val green = image.valueAt(0, 0,1)!!
        val blue = image.valueAt(0, 0, 2)!!
        assertEquals(Image.intensity(red, green, blue), intensityComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test lumaComponent works correctly for RGB image`() {
        val lumaComponent = image.lumaComponent()
        assertNotNull(lumaComponent)
        val red = image.valueAt(0, 0,0)!!
        val green = image.valueAt(0, 0,1)!!
        val blue = image.valueAt(0, 0, 2)!!
        assertEquals(Image.luma(red, green, blue), lumaComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test redComponent extracts red channel correctly`() {
        val redComponent = image.redComponent()
        assertNotNull(redComponent)
        assertEquals(image.valueAt(0, 0,0)!!, redComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test greenComponent extracts green channel correctly`() {
        val greenComponent = image.greenComponent()
        assertNotNull(greenComponent)
        assertEquals(image.valueAt(0, 0, 1), greenComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test blueComponent extracts blue channel correctly`() {
        val blueComponent = image.blueComponent()
        assertNotNull(blueComponent)
        assertEquals(image.valueAt(0, 0, 2), blueComponent!!.valueAt(0, 0, 0))
    }

    @Test
    fun `test rgbSplit returns correct components for RGB image`() {
        val rgbSplit = image.rgbSplit()

        assertNotNull(rgbSplit)
        assertEquals(3, rgbSplit?.size)

        // Check if the individual components are correct
        assertTrue(rgbSplit!![0] is ImageImpl)  // Red component
        assertTrue(rgbSplit[1] is ImageImpl)  // Green component
        assertTrue(rgbSplit[2] is ImageImpl)  // Blue component
    }
}
