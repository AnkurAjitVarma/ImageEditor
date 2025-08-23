package command.component

import command.Command
import command.Environment
import exceptions.MissingGreenChannel
import exceptions.NonExistentOperand

data class GreenComponent(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching{
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        val greenComponent = image.greenComponent() ?: throw MissingGreenChannel(operand)
        environment.putImage(result, greenComponent)
    }
}