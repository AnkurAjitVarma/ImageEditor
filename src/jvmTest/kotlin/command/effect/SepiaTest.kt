package command.effect

import command.Environment
import domain.image.Image
import exceptions.NonExistentOperand
import exceptions.NonRGBImage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import domain.transformation.color.Sepia as ColorSepia

class SepiaTest {

    @Test
    fun `should apply sepia and store under provided name`() = runTest {
        // Arrange
        val src = "src"
        val dst = "dst"
        val environment = mockk<Environment>()
        val original = mockk<Image>()
        val sepiaImage = mockk<Image>()

        every { environment.getImage(src) } returns original
        every { original.apply(ColorSepia) } returns sepiaImage
        every { environment.putImage(dst, sepiaImage) } just Runs

        val command = Sepia(src, dst)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage(src)
            original.apply(ColorSepia)
            environment.putImage(dst, sepiaImage)
        }
    }

    @Test
    fun `should return failure when operand image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missing") } returns null

        val command = Sepia("missing", "out")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        verify(exactly = 1) { environment.getImage("missing") }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }

    @Test
    fun `should return failure when image is not RGB (apply returns null)`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        val original = mockk<Image>()

        every { environment.getImage("img") } returns original
        every { original.apply(ColorSepia) } returns null

        val command = Sepia("img", "dst")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonRGBImage)
        verifyOrder {
            environment.getImage("img")
            original.apply(ColorSepia)
        }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}