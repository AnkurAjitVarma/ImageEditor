package domain.transformation.pixelwise.filter

val Blur = Filter(kernel = arrayOf(
    arrayOf(1.0 / 16, 1.0 / 8, 1.0 / 16),
    arrayOf(1.0 / 8, 1.0 / 4, 1.0 / 8),
    arrayOf(1.0 / 16, 1.0 / 8, 1.0 / 16)
))