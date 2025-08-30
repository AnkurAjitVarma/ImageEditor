package command.flip

import command.Command
import command.Environment
import exceptions.NonExistentOperand
import domain.transformation.pixelwise.spatial.VerticalFlip

data class VerticalFlip(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        environment.putImage(result, image.apply(VerticalFlip))
    }
}
