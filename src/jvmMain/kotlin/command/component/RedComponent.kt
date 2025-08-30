package command.component

import command.Command
import command.Environment
import exceptions.MissingRedChannel
import exceptions.NonExistentOperand

data class RedComponent(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        val redComponent = image.redComponent() ?: throw MissingRedChannel(operand)
        environment.putImage(result, redComponent)
    }
}