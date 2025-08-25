package command.flip

import command.Command
import command.Environment
import domain.exceptions.NonExistentOperand
import domain.transformation.pixelwise.spatial.HorizontalFlip

data class HorizontalFlip(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        environment.putImage(result, image.apply(HorizontalFlip))
    }
}
