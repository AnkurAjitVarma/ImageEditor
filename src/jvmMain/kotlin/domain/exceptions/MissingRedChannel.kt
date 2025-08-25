package domain.exceptions

data class MissingRedChannel(val operand: String) : UnsupportedOperationException("The image $operand does not have a red channel.")
