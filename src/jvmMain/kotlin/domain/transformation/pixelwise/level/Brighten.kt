package domain.transformation.pixelwise.level

import domain.transformation.pixelwise.PixelwiseTransformation

class Brighten(val amount:UByte): PixelwiseTransformation {
    override fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>> {
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

        for (i in 0 until height) {
            for (j in 0 until width) {
                for (k in 0 until channels) {
                    result[i][j][k] = (image[i][j][k] + amount).coerceIn(0u, 255u).toUByte()
                }
            }
        }
        return result
    }
}