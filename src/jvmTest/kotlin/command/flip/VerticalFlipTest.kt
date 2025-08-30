package command.flip

import command.Environment
import domain.image.Image
import exceptions.NonExistentOperand
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import domain.transformation.pixelwise.spatial.VerticalFlip as SpatialVerticalFlip

class VerticalFlipTest {

    @Test
    fun `should flip image vertically and store under provided name`() = runTest {
        // Arrange
        val src = "src"
        val dst = "dst"
        val environment = mockk<Environment>()
        val original = mockk<Image>()
        val flipped = mockk<Image>()

        every { environment.getImage(src) } returns original
        every { original.apply(SpatialVerticalFlip) } returns flipped
        every { environment.putImage(dst, flipped) } just Runs

        val command = VerticalFlip(src, dst)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage(src)
            original.apply(SpatialVerticalFlip)
            environment.putImage(dst, flipped)
        }
    }

    @Test
    fun `should return failure when operand image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missing") } returns null

        val command = VerticalFlip("missing", "out")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        verify(exactly = 1) { environment.getImage("missing") }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}