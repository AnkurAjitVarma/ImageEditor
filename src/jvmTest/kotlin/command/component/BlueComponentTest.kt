package command.component

import command.Environment
import domain.exceptions.MissingBlueChannel
import domain.exceptions.NonExistentOperand
import domain.image.Image
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class BlueComponentTest {

    @Test
    fun `should extract blue component and store result`() = runTest {
        // Arrange
        val inputImage = mockk<Image>()
        val blueComponentImage = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("inputImage") } returns inputImage
        every { inputImage.blueComponent() } returns blueComponentImage
        every { environment.putImage("outputImage", blueComponentImage) } just Runs

        val command = BlueComponent("inputImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage("inputImage")
            inputImage.blueComponent()
            environment.putImage("outputImage", blueComponentImage)
        }
    }

    @Test
    fun `should fail if input image is missing`() = runTest {
        val environment = mockk<Environment>()
        every { environment.getImage("missingImage") } returns null

        val command = BlueComponent("missingImage", "outputImage")
        val result = command.execute(environment)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
    }

    @Test
    fun `should fail if blue component is null`() = runTest {
        val inputImage = mockk<Image>()
        every { inputImage.blueComponent() } returns null

        val environment = mockk<Environment>()
        every { environment.getImage("inputImage") } returns inputImage

        val command = BlueComponent("inputImage", "outputImage")
        val result = command.execute(environment)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MissingBlueChannel)
    }
}
