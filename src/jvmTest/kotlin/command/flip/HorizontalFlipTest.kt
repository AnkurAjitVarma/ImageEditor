package command.flip

import command.Environment
import domain.image.Image
import domain.exceptions.NonExistentOperand
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import domain.transformation.pixelwise.spatial.HorizontalFlip as SpatialHorizontalFlip

class HorizontalFlipTest {

    @Test
    fun `should flip image horizontally and store under provided name`() = runTest {
        // Arrange
        val src = "src"
        val dst = "dst"
        val environment = mockk<Environment>()
        val original = mockk<Image>()
        val flipped = mockk<Image>()

        every { environment.getImage(src) } returns original
        every { original.apply(SpatialHorizontalFlip) } returns flipped
        every { environment.putImage(dst, flipped) } just Runs

        val command = HorizontalFlip(src, dst)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage(src)
            original.apply(SpatialHorizontalFlip)
            environment.putImage(dst, flipped)
        }
    }

    @Test
    fun `should return failure when operand image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missing") } returns null

        val command = HorizontalFlip("missing", "out")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        verify(exactly = 1) { environment.getImage("missing") }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}