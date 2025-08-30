package command.io

import command.Command
import command.Environment
import exceptions.NonExistentOperand
import java.nio.file.Path

data class Save(val operand: String, val location: Path) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching { environment.getImage(operand) ?: throw NonExistentOperand(operand) }.mapCatching { image -> environment.saveImageToFile(location, image).getOrThrow() }
}