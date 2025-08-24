package viewmodel.skia

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import viewmodel.skia.implementation.CachedSkiaImpl
import domain.image.Image
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SkiaTest {

    @Test
    fun `toSkiaImage should create a Skia image with provided dimensions`() {
        // Arrange
        val width = 3
        val height = 2
        val image = stubImage(width, height)

        val skia = CachedSkiaImpl(mutableMapOf())

        // Act
        val skiaImage = skia.toSkiaImage(image)

        // Assert
        assertNotNull(skiaImage)
        assertEquals(width, skiaImage.width)
        assertEquals(height, skiaImage.height)
    }

    @Test
    fun `visualize should convert images and cache results`() {
        // Arrange
        val cache = mutableMapOf<Image, androidx.compose.ui.graphics.ImageBitmap>()
        val skia = CachedSkiaImpl(cache)

        val img = stubImage(2, 2)

        // Act
        val first = skia.visualize(mapOf("a" to img))
        val firstBitmap = first["a"]

        val second = skia.visualize(mapOf("a" to img))
        val secondBitmap = second["a"]

        // Assert
        assertNotNull(firstBitmap, "First visualization should produce an ImageBitmap")
        assertNotNull(secondBitmap, "Second visualization should produce an ImageBitmap")
        assertSame(firstBitmap, secondBitmap, "ImageBitmap should be reused from cache for the same Image key")
        assertTrue(cache.containsKey(img), "Cache should contain the image after visualize")
        assertEquals(1, cache.size, "Cache should have exactly one entry for the single input image")
    }

    @Test
    fun `visualize should evict images not present in the current input`() {
        // Arrange
        val cache = mutableMapOf<Image, androidx.compose.ui.graphics.ImageBitmap>()
        val skia = CachedSkiaImpl(cache)

        val imgOld = stubImage(2, 2)
        val imgNew = stubImage(4, 1)

        // Seed cache by visualizing the old image
        val first = skia.visualize(mapOf("old" to imgOld))
        assertTrue(first.containsKey("old"))
        assertTrue(cache.containsKey(imgOld))

        // Act: visualize a map that no longer references imgOld
        val second = skia.visualize(mapOf("new" to imgNew))

        // Assert
        assertTrue(second.containsKey("new"))
        assertTrue(cache.containsKey(imgNew), "Cache should contain only the new image after visualize")
        assertTrue(!cache.containsKey(imgOld), "Cache should evict images not present in the current input")
    }

    @Test
    fun `visualize should preserve input keys and produce non-null bitmaps`() {
        // Arrange
        val cache = mutableMapOf<Image, androidx.compose.ui.graphics.ImageBitmap>()
        val skia = CachedSkiaImpl(cache)

        val imgA = stubImage(1, 1)
        val imgB = stubImage(2, 2)

        val input = mapOf("alpha" to imgA, "bravo" to imgB)

        // Act
        val result = skia.visualize(input)

        // Assert
        assertEquals(setOf("alpha", "bravo"), result.keys, "Result keys should match input keys")
        assertNotNull(result["alpha"])
        assertNotNull(result["bravo"])
    }

    // Helpers

    private fun stubImage(width: Int, height: Int): Image {
        val bytesPerPixel = 4 // N32 premultiplied -> 4 bytes per pixel
        val buffer = ByteArray(width * height * bytesPerPixel) { 0 } // opaque black/transparent is fine for tests

        val image = mockk<Image>()
        every { image.width() } returns width
        every { image.height() } returns height
        every { image.toByteArray() } returns buffer
        return image
    }
}