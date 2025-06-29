package domain.transformation
fun interface Transformation {
    fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>>
}
