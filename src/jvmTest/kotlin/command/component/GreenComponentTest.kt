package command.component

import command.Environment
import domain.image.Image
import domain.exceptions.MissingGreenChannel
import domain.exceptions.NonExistentOperand
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue


class GreenComponentTest {
    @Test
    fun `should extract green component and store result`() = runTest {
        // Arrange
        val inputImage = mockk<Image>()
        val greenComponentImage = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("inputImage") } returns inputImage
        every { inputImage.greenComponent() } returns greenComponentImage
        every { environment.putImage("outputImage", greenComponentImage) } just Runs

        val command = GreenComponent("inputImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage("inputImage")
            inputImage.greenComponent()
            environment.putImage("outputImage", greenComponentImage)
        }
    }

    @Test
    fun `should fail if input image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missingImage") } returns null

        val command = GreenComponent("missingImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
    }

    @Test
    fun `should fail if green component is null`() = runTest {
        // Arrange
        val inputImage = mockk<Image>()
        every { inputImage.greenComponent() } returns null

        val environment = mockk<Environment>()
        every { environment.getImage("inputImage") } returns inputImage

        val command = GreenComponent("inputImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MissingGreenChannel)
    }

}