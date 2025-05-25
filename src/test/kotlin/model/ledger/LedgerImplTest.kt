package model.ledger

import model.image.implementation.ImageImpl
import model.ledger.implementation.LedgerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class LedgerImplTest {
    private val ledger = LedgerImpl()
    private val image = ImageImpl(Array(2) { Array(2) { Array(3) { 255.toUByte() } } })

    @Test
    fun testPutAndGetImage() {
        ledger.putImage("test", image)
        assertEquals(image, ledger.getImage("test"))
    }

    @Test
    fun testGetNonExistentImage() {
        assertNull(ledger.getImage("non_existent"))
    }

    @Test
    fun testPutDuplicateImage() {
        val image2 = ImageImpl(Array(2) { Array(2) { Array(3) { 128.toUByte() } } })
        ledger.putImage("test", image)
        ledger.putImage("test", image2)
        assertEquals(image2, ledger.getImage("test"))
    }
}