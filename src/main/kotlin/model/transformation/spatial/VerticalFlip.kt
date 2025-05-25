package model.transformation.spatial

import model.transformation.Transformation

val VerticalFlip = Transformation{ image ->
    val height = image.size
    val width = image[0].size
    val noOfChannels = image[0][0].size
    val result = Array(height) { Array(width) { Array(noOfChannels) { 0.toUByte() } } }
    for (row in 0 until height)
        for (column in 0 until width)
            result[height - row - 1][column] = image[row][column]
    result
}