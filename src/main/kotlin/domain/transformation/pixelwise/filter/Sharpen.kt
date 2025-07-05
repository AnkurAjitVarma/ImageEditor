package domain.transformation.pixelwise.filter

private val kernel = arrayOf(
    arrayOf(-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8),
    arrayOf(-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8)
)
object Sharpen:Filter(kernel) {
    override fun transform(image: Array<Array<Array<UByte>>>): Array<Array<Array<UByte>>> = applyFilter(image)
}