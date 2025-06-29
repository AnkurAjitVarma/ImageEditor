package domain.image.implementation

import domain.image.Image
import domain.transformation.Transformation
import utilities.BLUE_CHANNEL
import utilities.GREEN_CHANNEL
import utilities.RED_CHANNEL

internal class ImageImpl(private val image: Array<Array<Array<UByte>>>):Image {
    override fun height(): Int = image.size

    override fun width(): Int = image[0].size

    override fun noOfChannels():Int = image[0][0].size

    override fun valueAt(row: Int, column: Int, channel: Int): UByte? = if (row in 0 until height() && column in 0 until width() && channel in 0 until noOfChannels()) image[row][column][channel] else null

    override fun apply(transformation: Transformation): Image = ImageImpl(transformation.transform(image))

    override fun valueComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = Image.value(image[row][column][RED_CHANNEL], image[row][column][GREEN_CHANNEL], image[row][column][BLUE_CHANNEL])
            }
        }
        ImageImpl(component)
    }

    override fun intensityComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = Image.intensity(image[row][column][RED_CHANNEL], image[row][column][GREEN_CHANNEL], image[row][column][BLUE_CHANNEL])
            }
        }
        ImageImpl(component)
    }

    override fun lumaComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = Image.luma(image[row][column][RED_CHANNEL], image[row][column][GREEN_CHANNEL], image[row][column][BLUE_CHANNEL])
            }
        }
        ImageImpl(component)
    }

    override fun redComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = image[row][column][RED_CHANNEL]
            }
        }
        ImageImpl(component)
    }

    override fun greenComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = image[row][column][GREEN_CHANNEL]
            }
        }
        ImageImpl(component)
    }

    override fun blueComponent(): Image? = if (noOfChannels()!=3) null else {
        val component = Array(height()) { Array(width()) { Array(1) { 0.toUByte() } } }
        for (row in 0 until height()) {
            for (column in 0 until width()) {
                component[row][column][0] = image[row][column][BLUE_CHANNEL]
            }
        }
        ImageImpl(component)
    }

    override fun rgbSplit(): List<Image>? = if (noOfChannels()!=3) null else listOfNotNull(redComponent(), greenComponent(), blueComponent())
}