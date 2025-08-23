package command.effect

import command.Environment
import domain.image.Image
import exceptions.NonExistentOperand
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import domain.transformation.pixelwise.level.Brighten as LevelBrighten

class BrightenTest {

    @Test
    fun `should apply brighten with given amount and store under provided name`() = runTest {
        // Arrange
        val src = "src"
        val dst = "dst"
        val amount: UByte = 25u.toUByte()
        val environment = mockk<Environment>()
        val original = mockk<Image>()
        val brightened = mockk<Image>()
        val brightenSlot = slot<LevelBrighten>()

        every { environment.getImage(src) } returns original
        every { original.apply(capture(brightenSlot)) } returns brightened
        every { environment.putImage(dst, brightened) } just Runs

        val command = Brighten(src, amount, dst)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage(src)
            original.apply(any<LevelBrighten>())
            environment.putImage(dst, brightened)
        }
        // Optionally ensure a LevelBrighten transformation was used
        assertTrue(brightenSlot.isCaptured)
    }

    @Test
    fun `should return failure when operand image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missing") } returns null

        val command = Brighten("missing", 10u.toUByte(), "out")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        verify(exactly = 1) { environment.getImage("missing") }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}