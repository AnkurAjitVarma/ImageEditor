package domain.exceptions

data class MissingBlueChannel(val operand: String) : UnsupportedOperationException("The image $operand does not have a blue channel.")
