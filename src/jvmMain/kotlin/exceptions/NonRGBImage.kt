package exceptions

data class NonRGBImage(val operand: String) : IllegalArgumentException("The image $operand is not an RGB image.")
