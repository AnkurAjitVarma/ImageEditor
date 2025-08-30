package command.component

import command.Command
import command.Environment
import exceptions.MissingBlueChannel
import exceptions.NonExistentOperand

data class BlueComponent(val operand: String, val result: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        val blueComponent = image.blueComponent() ?: throw MissingBlueChannel(operand)
        environment.putImage(result, blueComponent)
    }
}