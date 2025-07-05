package domain.transformation.pixelwise.filter

import domain.transformation.pixelwise.PixelwiseTransformation

abstract class Filter(private val kernel: Array<Array<Double>>): PixelwiseTransformation {
    protected fun applyFilter(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>> {
        val height = image.size
        val width = image[0].size
        val channels = image[0][0].size

        val result = Array(height) {
            Array(width) {
                Array(channels){
                    0.toUByte()
                }
            }
        }

        val filterHeight: Int = kernel.size
        val filterWidth: Int = kernel[0].size
        val halfFilterY = filterHeight / 2
        val halfFilterX = filterWidth / 2

        for (i in 0 until height) {
            for (j in 0 until width) {
                for (k in 0 until channels) {
                    result[i][j][k] = applyFilterAtPixel(image, i, j, k, halfFilterY, halfFilterX)
                }
            }
        }
        return result
    }

    private fun applyFilterAtPixel(
        image: Array<Array<Array<UByte>>>,
        i: Int,
        j: Int,
        k: Int,
        halfFilterY: Int,
        halfFilterX: Int
    ): UByte {
        val height = image.size
        val width = image[0].size
        var sum = 0.0

        for (m in -halfFilterY..halfFilterY) {
            for (n in -halfFilterX..halfFilterX) {
                val imgY = (i+m).coerceIn(0, height-1)
                val imgX = (j+n).coerceIn(0, width-1)
                sum += image[imgY][imgX][k].toDouble() * kernel[m + halfFilterY][n + halfFilterX]
            }
        }
        return Math.round(sum).toInt().coerceIn(0, 255).toUByte()
    }
}