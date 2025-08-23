package command.component

import command.Command
import command.Environment
import exceptions.MissingBlueChannel
import exceptions.NonExistentOperand

class BlueComponent(val operand: String, val result: String): Command {
    override suspend fun execute(environment: Environment): Result<Unit> {
        val image = environment.getImage(operand) ?: return Result.failure(NonExistentOperand(operand))
        val blueComponent = image.blueComponent() ?: return Result.failure(MissingBlueChannel(operand))
        return runCatching {
            environment.putImage(result, blueComponent)
        }
    }
}