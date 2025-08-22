package utilities

import domain.image.Image
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

object ImageLoader {
    fun loadImage(path:Path): Image {
        require(Files.exists(path)) { "File does not exist at the given path: $path" }

        val bufferedImage: BufferedImage = ImageIO.read(path.toFile())

        val height = bufferedImage.height
        val width = bufferedImage.width

        val result = Array(height) { Array(width) { Array(3) { 0.toUByte() } } }

        for (y in 0 until height) {
            for (x in 0 until width) {
                // Get the RGB value of the pixel
                val rgb = bufferedImage.getRGB(x, y)

                // Extract red, green, and blue components
                val red = (rgb shr 16) and 0xFF
                val green = (rgb shr 8) and 0xFF
                val blue = rgb and 0xFF

                // Store each color component as a UByte in the result array
                result[y][x][RED_CHANNEL] = red.toUByte()
                result[y][x][GREEN_CHANNEL] = green.toUByte()
                result[y][x][BLUE_CHANNEL] = blue.toUByte()
            }
        }

        return Image.fromArray(result)
    }

    fun saveImage(path: Path, image:Image){
        val format = path.fileName.toString()
            .substringAfterLast('.', missingDelimiterValue = "")
            .lowercase()

        require(format in ImageIO.getWriterFormatNames().map { it.lowercase() }) {
            "Unsupported image format: $format"
        }

        val height = image.height()
        val width = image.width()

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (y in 0 until height) {
            for (x in 0 until width) {

                val red = image.valueAt(y, x, 0)!!.toInt()
                val green = image.valueAt(y, x, 1)!!.toInt()
                val blue = image.valueAt(y, x, 2)!!.toInt()

                val rgb = (red shl 16) or (green shl 8) or blue

                bufferedImage.setRGB(x, y, rgb)
            }
        }

        ImageIO.write(bufferedImage, format, path.toFile())
    }
}
