package domain.model

import domain.image.implementation.ImageImpl
import domain.model.implementation.LedgerImpl
import kotlinx.collections.immutable.persistentHashMapOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class LedgerImplTest {
    private val ledger = LedgerImpl(persistentHashMapOf())
    private val image = ImageImpl(Array(2) { Array(2) { Array(3) { 255.toUByte() } } })

    @Test
    fun testPutAndGetImage() {
        val newLedger = ledger.putImage("test", image)
        assertEquals(image, newLedger.getImage("test"))
    }

    @Test
    fun testGetNonExistentImage() {
        assertNull(ledger.getImage("non_existent"))
    }

    @Test
    fun testPutDuplicateImage() {
        val image2 = ImageImpl(Array(2) { Array(2) { Array(3) { 128.toUByte() } } })
        val ledger1 = ledger.putImage("test", image)
        val ledger2 = ledger1.putImage("test", image2)
        assertEquals(image2, ledger2.getImage("test"))
    }
}