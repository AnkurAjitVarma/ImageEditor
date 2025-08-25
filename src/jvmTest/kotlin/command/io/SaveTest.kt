package command.io

import command.Environment
import domain.image.Image
import domain.exceptions.NonExistentOperand
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class SaveTest {

    @Test
    fun `should save existing image to given path`() = runTest {
        // Arrange
        val path = Path.of("out.png")
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        every { environment.getImage("img") } returns image
        coEvery { environment.saveImageToFile(path, image) } returns Result.success(Unit)

        val command = Save("img", path)

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
    fun `should return failure and not call save when operand image is missing`() = runTest {
        // Arrange
        val path = Path.of("out.png")
        val environment = mockk<Environment>()

        every { environment.getImage("missing") } returns null

        val command = Save("missing", path)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NonExistentOperand)
        coVerify(exactly = 0) { environment.saveImageToFile(any(), any()) }
    }

    @Test
    fun `should return failure when saving to file fails`() = runTest {
        // Arrange
        val path = Path.of("out.png")
        val image = mockk<Image>()
        val environment = mockk<Environment>()
        val error = IOException("disk full")

        every { environment.getImage("img") } returns image
        coEvery { environment.saveImageToFile(path, image) } returns Result.failure(error)

        val command = Save("img", path)

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        coVerifyOrder {
            environment.getImage("img")
            environment.saveImageToFile(path, image)
        }
    }
}