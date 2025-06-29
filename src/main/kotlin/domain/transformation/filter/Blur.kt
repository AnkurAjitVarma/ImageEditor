package domain.transformation.filter

private val kernel = arrayOf(
    arrayOf(1.0 / 16, 1.0 / 8, 1.0 / 16),
    arrayOf(1.0 / 8, 1.0 / 4, 1.0 / 8),
    arrayOf(1.0 / 16, 1.0 / 8, 1.0 / 16)
)

object Blur:Filter(kernel){
    override fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>> = applyFilter(image)
}