package domain.transformation.color

import utilities.BLUE_CHANNEL
import utilities.GREEN_CHANNEL
import utilities.RED_CHANNEL

val Sepia = ColorTransformation { image ->
    val height = image.size
    val width = image[0].size

    val result = Array(height) {
        Array(width) {
            Array(3) {
                0.toUByte()
            }
        }
    }


    for (i in 0 until height) {
        for (j in 0 until width) {
            val red = image[i][j][RED_CHANNEL].toDouble()
            val green = image[i][j][GREEN_CHANNEL].toDouble()
            val blue = image[i][j][BLUE_CHANNEL].toDouble()

            result[i][j][RED_CHANNEL] = (0.393 * red + 0.769 * green + 0.189 * blue).toInt().coerceIn(0, 255).toUByte() // Red
            result[i][j][GREEN_CHANNEL] = (0.349 * red + 0.686 * green + 0.168 * blue).toInt().coerceIn(0, 255).toUByte() // Green
            result[i][j][BLUE_CHANNEL] = (0.272 * red + 0.534 * green + 0.131 * blue).toInt().coerceIn(0, 255).toUByte() // Blue
        }
    }
    result
}