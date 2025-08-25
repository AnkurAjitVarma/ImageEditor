package command.effect

import command.Command
import command.Environment
import domain.exceptions.NonExistentOperand
import domain.exceptions.NonRGBImage
import domain.transformation.color.Sepia

data class Sepia(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        val resultImage = image.apply(Sepia) ?: throw NonRGBImage(operand)
        environment.putImage(result, resultImage)
    }
}
