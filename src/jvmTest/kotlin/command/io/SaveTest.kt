package command.io

import command.Environment
import domain.image.Image
import exceptions.InvalidFilePath
import exceptions.NonExistentOperand
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Path

class SaveTest {

    @Test
    fun `execute succeeds and saves when image exists and path is valid`() = runTest {
        // Arrange
        val path = Path.of("out.png")
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("img") } returns image
        coEvery { environment.saveImageToFile(path, image) } returns Result.success(Unit)

        val command = Save("img", "out.png")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        coVerifyOrder {
            environment.getImage("img")
            environment.saveImageToFile(path, image)
        }
    }

    @Test
    fun `execute fails and does not save when operand image is missing`() = runTest {
        // Arrange
        val environment = mockk<Environment>()
        every { environment.getImage("missing") } returns null

        val command = Save("missing", "out.png")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        coVerify(exactly = 0) { environment.saveImageToFile(any(), any()) }
    }

    @Test
    fun `execute fails when saving to file fails`() = runTest {
        // Arrange
        val path = Path.of("out.png")
        val image = mockk<Image>()
        val environment = mockk<Environment>()
        val error = IOException("disk full")

        every { environment.getImage("img") } returns image
        coEvery { environment.saveImageToFile(path, image) } returns Result.failure(error)

        val command = Save("img", "out.png")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        coVerifyOrder {
            environment.getImage("img")
            environment.saveImageToFile(path, image)
        }
    }

    @Test
    fun `execute fails with InvalidFilePath and does not attempt save when path is invalid`() = runTest {
        // Arrange
        val invalidPath = "bad\u0000name.png" // triggers InvalidPathException on all platforms
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("img") } returns image

        val command = Save("img", invalidPath)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidFilePath)
        coVerify(exactly = 0) { environment.saveImageToFile(any(), any()) }
    }
}