package command.io

import command.Environment
import domain.image.Image
import exceptions.InvalidFilePath
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class LoadTest {

    @Test
    fun `should load image from file and store it under provided name`() = runTest {
        // Arrange
        val pathStr = "input.png"
        val filePath = Path.of(pathStr)
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        coEvery { environment.readImageFromFile(filePath) } returns Result.success(image)
        every { environment.putImage("myImage", image) } returns Unit

        val command = Load(pathStr, "myImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        coVerifyOrder {
            environment.readImageFromFile(filePath)
            environment.putImage("myImage", image)
        }
    }

    @Test
    fun `should return failure and not store image when file read fails`() = runTest {
        // Arrange
        val pathStr = "missing.png"
        val filePath = Path.of(pathStr)
        val environment = mockk<Environment>()
        val error = IOException("failed to read file")

        coEvery { environment.readImageFromFile(filePath) } returns Result.failure(error)

        val command = Load(pathStr, "willNotBeStored")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        coVerify(exactly = 1) { environment.readImageFromFile(filePath) }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }

    @Test
    fun `should return InvalidFilePath when the provided path string is not a valid path`() = runTest {
        // Arrange
        val invalidPath = "<<<invalid>>>"
        val environment = mockk<Environment>(relaxed = true)
        val command = Load(invalidPath, "unused")

        // Mock static Path.of to throw InvalidPathException (portable across OSes)
        mockkStatic(Path::class)
        try {
            every { Path.of(invalidPath) } throws InvalidPathException(invalidPath, "invalid path")

            // Act
            val result = command.execute(environment)

            // Assert
            assertTrue(result.isFailure)
            val ex = result.exceptionOrNull()
            assertTrue(ex is InvalidFilePath)
            coVerify(exactly = 0) { environment.readImageFromFile(any()) }
            verify(exactly = 0) { environment.putImage(any(), any()) }
        } finally {
            unmockkStatic(Path::class)
        }
    }
}