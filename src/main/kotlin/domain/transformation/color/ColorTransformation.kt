package domain.transformation.color

fun interface ColorTransformation {
    fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>>
}