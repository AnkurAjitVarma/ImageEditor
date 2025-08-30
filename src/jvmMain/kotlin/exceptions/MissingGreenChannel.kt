package exceptions

data class MissingGreenChannel(val operand: String) : UnsupportedOperationException("The image $operand does not have a green channel.")
