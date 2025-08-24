package controller.loader.implementation

import controller.loader.ImageLoader
import domain.BLUE_CHANNEL
import domain.GREEN_CHANNEL
import domain.RED_CHANNEL
import domain.image.Image
import exceptions.FileDoesNotExist
import exceptions.ImageFormatNotSupported
import exceptions.URLNotReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.awt.image.Raster
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

object ImageLoaderImpl: ImageLoader {
    override suspend fun loadFromPath(path: Path): Result<Image> = runCatching {
        if (!Files.exists(path)) throw FileDoesNotExist(path)
        val bufferedImage = withContext(Dispatchers.IO){
            ImageIO.read(path.toFile())
        }
        bufferedImage.toImage()
    }

    override suspend fun loadFromURL(url: URL): Result<Image> = runCatching {
        val bufferedImage = withContext(Dispatchers.IO){
            ImageIO.read(url.openStream())
                ?: throw URLNotReadable(url)
        }
        bufferedImage.toImage()
    }

    override suspend fun save(image: Image, path: Path): Result<Unit> = runCatching {
        val format = path.fileName.toString()
            .substringAfterLast('.', missingDelimiterValue = "")
            .lowercase()

        if(format !in ImageIO.getWriterFormatNames().map { it.lowercase() }) throw ImageFormatNotSupported(format)
        val bufferedImage = image.toBufferedImage()
        withContext(Dispatchers.IO){
            ImageIO.write(bufferedImage, format, path.toFile())
        }
    }

    private fun Image.toBufferedImage(): BufferedImage {
        val bytes = this.toByteArray()
        val dataBuffer = DataBufferByte(bytes, bytes.size)
        // Each pixel has 4 components: R, G, B, A
        val bands = 4
        val scanlineStride = this.width() * bands

        // Interleaved raster: [R, G, B, A, R, G, B, A, ...]
        val raster = Raster.createInterleavedRaster(
            dataBuffer,
            this.width(),
            this.height(),
            scanlineStride,
            bands,
            intArrayOf(0, 1, 2, 3), // band offsets: R=0, G=1, B=2, A=3
            null
        )

        // Color model with alpha
        val colorModel = ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_sRGB),
            true,  // hasAlpha
            false, // isAlphaPremultiplied
            Transparency.TRANSLUCENT,
            DataBuffer.TYPE_BYTE
        )

        return BufferedImage(colorModel, raster, false, null)
    }

    private fun BufferedImage.toImage(): Image {
        val height = this.height
        val width = this.width
        val result = Array(height) { Array(width) { Array(3) { 0.toUByte() } } }

        when (this.type) {
            BufferedImage.TYPE_3BYTE_BGR -> {
                val bytes = (this.raster.dataBuffer as DataBufferByte).data
                var i = 0
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val b = bytes[i++].toInt() and 0xFF
                        val g = bytes[i++].toInt() and 0xFF
                        val r = bytes[i++].toInt() and 0xFF
                        result[y][x][RED_CHANNEL] = r.toUByte()
                        result[y][x][GREEN_CHANNEL] = g.toUByte()
                        result[y][x][BLUE_CHANNEL] = b.toUByte()
                    }
                }
            }
            BufferedImage.TYPE_INT_RGB -> {
                val ints = (this.raster.dataBuffer as DataBufferInt).data
                var i = 0
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val rgb = ints[i++]
                        val r = (rgb shr 16) and 0xFF
                        val g = (rgb shr 8) and 0xFF
                        val b = rgb and 0xFF
                        result[y][x][RED_CHANNEL] = r.toUByte()
                        result[y][x][GREEN_CHANNEL] = g.toUByte()
                        result[y][x][BLUE_CHANNEL] = b.toUByte()
                    }
                }
            }
            else -> {
                // fallback for other types
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val rgb = this.getRGB(x, y)
                        val r = (rgb shr 16) and 0xFF
                        val g = (rgb shr 8) and 0xFF
                        val b = rgb and 0xFF
                        result[y][x][RED_CHANNEL] = r.toUByte()
                        result[y][x][GREEN_CHANNEL] = g.toUByte()
                        result[y][x][BLUE_CHANNEL] = b.toUByte()
                    }
                }
            }
        }

        return Image.fromArray(result)
    }
}