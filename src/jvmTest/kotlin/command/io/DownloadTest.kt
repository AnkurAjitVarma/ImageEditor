package command.io

import command.Environment
import domain.image.Image
import exceptions.URLNotAbsolute
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.URI
import java.net.URL

class DownloadTest {

    @Test
    fun `execute succeeds and stores image when URL is valid and download succeeds`() = runTest {
        // Arrange
        val environment = mockk<Environment>(relaxed = true)
        val image = mockk<Image>(relaxed = true) // Image type from your codebase; using Any to keep it generic in the test
        val expectedUrl = URI("http://example.com/image.png").toURL()
        val urlSlot: CapturingSlot<URL> = slot()

        coEvery { environment.downloadImage(capture(urlSlot)) } returns Result.success(image)
        every { environment.putImage("avatar", image) } returns Unit

        val command = Download(url = expectedUrl.toString(), name = "avatar")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess, "Expected execute to succeed")
        assertEquals(expectedUrl, urlSlot.captured, "downloadImage should be called with the parsed URL")
        verify(exactly = 1) { environment.putImage("avatar", image) }
        coVerify(exactly = 1) { environment.downloadImage(any()) }
    }

    @Test
    fun `execute fails with URLNotAbsolute when URL is relative`() = runTest {
        // Arrange
        val environment = mockk<Environment>(relaxed = true)
        val command = Download(url = "images/cat.png", name = "cat")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure, "Expected execute to fail for non-absolute URL")
        assertTrue(result.exceptionOrNull() is URLNotAbsolute, "Expected URLNotAbsolute")
        // Ensure no I/O was attempted
        coVerify(exactly = 0) { environment.downloadImage(any()) }
        excludeRecords { environment.putImage(any(), any()) } // ignore auto-relaxed calls
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }

    @Test
    fun `execute fails and does not store image when download fails`() = runTest {
        // Arrange
        val environment = mockk<Environment>(relaxed = true)
        val expectedUrl = URI("http://example.com/image.png").toURL()
        coEvery { environment.downloadImage(any()) } returns Result.failure(IOException("network down"))

        val command = Download(url = expectedUrl.toString(), name = "pic")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure, "Expected execute to fail when download fails")
        coVerify(exactly = 1) { environment.downloadImage(any()) }
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}