package command.effect

import command.Command
import command.Environment
import exceptions.NonExistentOperand
import domain.transformation.pixelwise.filter.Sharpen

data class Sharpen(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        environment.putImage(result, image.apply(Sharpen))
    }
}
