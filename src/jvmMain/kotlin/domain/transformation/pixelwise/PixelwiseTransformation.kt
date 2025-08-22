package domain.transformation.pixelwise
fun interface PixelwiseTransformation {
    fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>>
}
