package domain.transformation.pixelwise
interface PixelwiseTransformation {
    fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>>
}
