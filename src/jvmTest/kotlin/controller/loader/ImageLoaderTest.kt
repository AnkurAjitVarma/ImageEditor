package controller.loader

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import exceptions.FileDoesNotExist
import exceptions.URLNotReadable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ImageLoaderTest {

    private val loader: ImageLoader = ImageLoader.getDefault()

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `loadFromPath should succeed for valid PNG`() = runTest {
        // Arrange
        val inputPath = tempDir.resolve("input.png")
        val png = makeTestPngBytes(width = 3, height = 2)
        Files.write(inputPath, png)

        // Act
        val result = loader.loadFromPath(inputPath)

        // Assert
        assertTrue(result.isSuccess, "Expected success when loading a valid PNG")
        val image = result.getOrNull()
        assertNotNull(image, "Loaded image should not be null")

        // And verify we can save it back to disk
        val outputPath = tempDir.resolve("saved.png")
        val saveResult = loader.save(image, outputPath)
        assertTrue(saveResult.isSuccess, "Saving previously loaded image should succeed")
        assertTrue(Files.exists(outputPath), "Saved image file should exist on disk")
        assertNotNull(ImageIO.read(outputPath.toFile()), "Saved image should be a readable image file")
    }

    @Test
    fun `loadFromPath should fail with FileDoesNotExist for missing file`() = runTest {
        // Arrange
        val missingPath = tempDir.resolve("missing.png") // not created

        // Act
        val result = loader.loadFromPath(missingPath)

        // Assert
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertIs<FileDoesNotExist>(error)
    }

    @Test
    fun `loadFromURL should succeed for URL serving a PNG`() = runTest {
        // Arrange: start a tiny local HTTP server serving a PNG
        val png = makeTestPngBytes(4, 4)
        val server = httpServer().apply {
            createContext("/img", fixedBytesHandler(200, "image/png", png))
            start()
        }
        try {
            val url = URL("http://127.0.0.1:${server.address.port}/img")

            // Act
            val result = loader.loadFromURL(url)

            // Assert
            assertTrue(result.isSuccess, "Expected success when reading PNG from URL")
            assertNotNull(result.getOrNull())
        } finally {
            server.stop(0)
        }
    }

    @Test
    fun `loadFromURL should fail with URLNotReadable for URL serving non-image content`() = runTest {
        // Arrange: server returns 200 with plain text, which is not a valid image
        val server = httpServer().apply {
            createContext("/txt", fixedBytesHandler(200, "text/plain", "not an image".toByteArray()))
            start()
        }
        try {
            val url = URL("http://127.0.0.1:${server.address.port}/txt")

            // Act
            val result = loader.loadFromURL(url)

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertIs<URLNotReadable>(error)
        } finally {
            server.stop(0)
        }
    }

    // Helpers

    private fun makeTestPngBytes(width: Int, height: Int): ByteArray {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        // Simple color pattern for determinism
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = when ((x + y) % 3) {
                    0 -> Color.RED
                    1 -> Color.GREEN
                    else -> Color.BLUE
                }
                img.setRGB(x, y, color.rgb)
            }
        }

        return ByteArrayOutputStream().use { baos ->
            check(ImageIO.write(img, "png", baos)) { "Failed to encode PNG in test" }
            baos.toByteArray()
        }
    }

    private fun httpServer(): HttpServer {
        return HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
    }

    private fun fixedBytesHandler(status: Int, contentType: String, body: ByteArray): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            try {
                exchange.responseHeaders.add("Content-Type", contentType)
                exchange.sendResponseHeaders(status, body.size.toLong())
                exchange.responseBody.use { it.write(body) }
            } finally {
                exchange.close()
            }
        }
    }
}