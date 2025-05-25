package model.image

import model.image.implementation.ImageImpl
import model.transformation.Transformation
import utilities.BLUE_CHANNEL
import utilities.GREEN_CHANNEL
import utilities.RED_CHANNEL

interface Image {
    fun height():Int
    fun width():Int
    fun noOfChannels():Int
    fun valueAt(row:Int, column:Int, channel:Int):UByte?
    fun apply(transformation: Transformation):Image
    fun valueComponent():Image?
    fun intensityComponent():Image?
    fun lumaComponent():Image?
    fun redComponent():Image?
    fun greenComponent():Image?
    fun blueComponent():Image?
    fun rgbSplit():List<Image>?
    companion object {
        fun fromArray(array: Array<Array<Array<UByte>>>):Image = ImageImpl(array)
        fun rgbCombine(red:Image, green: Image, blue: Image):Image? = if (combinable(listOf(red, green, blue))) {
            val width = red.width()
            val height = red.height()
            val result = Array(height){ Array(width) {Array(3) { 0.toUByte() } } }
            for (row in 0 until height)
                for (column in 0 until width){
                    result[row][column][RED_CHANNEL]=red.valueAt(row, column, 0)!!
                    result[row][column][GREEN_CHANNEL]=green.valueAt(row, column, 0)!!
                    result[row][column][BLUE_CHANNEL]=blue.valueAt(row, column, 0)!!
                }

            ImageImpl(result)
        } else null
        private fun combinable(images: List<Image>): Boolean {
            if (images.isEmpty()) return false

            val first = images[0]
            if (first.noOfChannels() != 1) return false

            return images.all {
                it.width() == first.width() &&
                        it.height() == first.height() &&
                        it.noOfChannels() == 1
            }
        }
        fun value(red:UByte, green:UByte, blue:UByte):UByte = maxOf(red, green, blue)
        fun intensity(red:UByte, green:UByte, blue:UByte):UByte = ((red.toDouble()+green.toDouble()+blue.toDouble())/3).toInt().coerceIn(0, 255).toUByte()
        fun luma(red:UByte, green:UByte, blue:UByte):UByte = (0.2126*red.toDouble() + 0.7152*green.toDouble() + 0.0722*blue.toDouble()).toInt().coerceIn(0, 255).toUByte()
    }
}