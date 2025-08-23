package command.io

import command.Environment
import domain.image.Image
import io.mockk.*
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertTrue

class DownloadTest {

    @Test
    fun `should download image from URL and store it under provided name`() = runTest {
        // Arrange
        val url = URI("https://example.com/image.png").toURL()
        val image = mockk<Image>()
        val environment = mockk<Environment>()

        coEvery { environment.downloadImage(url) } returns Result.success(image)
        every { environment.putImage("myImage", image) } just Runs

        val command = Download(url, "myImage")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isSuccess)
        coVerifyOrder {
            environment.downloadImage(url)
            environment.putImage("myImage", image)
        }
    }

    @Test
    fun `should return failure and not store image when download fails`() = runTest {
        // Arrange
        val url = URI("https://example.com/missing.png").toURL()
        val environment = mockk<Environment>()
        val error = IOException("failed to download")

        coEvery { environment.downloadImage(url) } returns Result.failure(error)

        val command = Download(url, "willNotBeStored")

        // Act
        val result = command.execute(environment)

        // Assert
        assertTrue(result.isFailure)
        verify(exactly = 0) { environment.putImage(any(), any()) }
    }
}