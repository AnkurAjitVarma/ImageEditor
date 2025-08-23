package command.io

import command.Environment
import domain.image.Image
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class LoadTest {

    @Test
    fun `should load image from file and store it under provided name`() = runTest {
        // Arrange
        val path = Path.of("input.png")
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        coEvery { environment.readImageFromFile(path) } returns Result.success(image)
        every { environment.putImage("myImage", image) } just Runs

        val command = Load(path, "myImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        coVerifyOrder {
            environment.readImageFromFile(path)
            environment.putImage("myImage", image)
        }
    }

    @Test
    fun `should return failure and not store image when file read fails`() = runTest {
        // Arrange
        val path = Path.of("missing.png")
        val environment = mockk<Environment>()
        val error = IOException("failed to read file")

        coEvery { environment.readImageFromFile(path) } returns Result.failure(error)

        val command = Load(path, "willNotBeStored")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}