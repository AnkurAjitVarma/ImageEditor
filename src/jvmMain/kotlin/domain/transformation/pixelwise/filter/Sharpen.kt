package domain.transformation.pixelwise.filter

val Sharpen = Filter(kernel = arrayOf(
    arrayOf(-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8)
))