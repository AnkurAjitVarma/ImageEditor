package command.effect

import command.Command
import command.Environment
import domain.transformation.pixelwise.level.Brighten
import exceptions.NonExistentOperand

data class Brighten(val operand: String, val amount: UByte, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        environment.putImage(result, image.apply(Brighten(amount)))
    }
}
