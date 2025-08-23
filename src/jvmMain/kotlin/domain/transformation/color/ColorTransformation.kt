package domain.transformation.color

interface ColorTransformation {
    fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>>
}