package domain.transformation.spatial

import domain.transformation.Transformation

val HorizontalFlip = Transformation { image ->
    val height = image.size
    val width = image[0].size
    val noOfChannels = image[0][0].size
    val result = Array(height) { Array(width) { Array(noOfChannels) { 0.toUByte() } } }
    for (row in 0 until height)
        for (column in 0 until width)
            for (channel in 0 until noOfChannels)
                result[row][column][channel] = image[row][width - column - 1][channel]
    result
}
