package command.component

import command.Environment
import domain.image.Image
import domain.exceptions.MissingRedChannel
import domain.exceptions.NonExistentOperand
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class RedComponentTest {

    @Test
    fun `should extract red component and store result`() = runTest {
        // Arrange
        val inputImage = mockk<Image>()
        val redComponentImage = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("inputImage") } returns inputImage
        every { inputImage.redComponent() } returns redComponentImage
        every { environment.putImage("outputImage", redComponentImage) } just Runs

        val command = RedComponent("inputImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        verifyOrder {
            environment.getImage("inputImage")
            inputImage.redComponent()
            environment.putImage("outputImage", redComponentImage)
        }
    }

    @Test
    fun `should fail if input image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missingImage") } returns null

        val command = RedComponent("missingImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
    }

    @Test
    fun `should fail if red component is null`() = runTest {
        // Arrange
        val inputImage = mockk<Image>()
        every { inputImage.redComponent() } returns null

        val environment = mockk<Environment>()
        every { environment.getImage("inputImage") } returns inputImage

        val command = RedComponent("inputImage", "outputImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MissingRedChannel)
    }
}