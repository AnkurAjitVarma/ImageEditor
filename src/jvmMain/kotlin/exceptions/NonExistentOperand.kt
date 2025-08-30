package exceptions

data class NonExistentOperand(val operand: String) : IllegalArgumentException("The image $operand does not exist.")
