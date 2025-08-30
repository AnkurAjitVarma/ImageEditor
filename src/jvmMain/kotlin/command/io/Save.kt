package command.io

import command.Command
import command.Environment
import exceptions.InvalidFilePath
import exceptions.NonExistentOperand
import java.nio.file.InvalidPathException
import java.nio.file.Path

data class Save(val operand: String, val path: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand) ?: throw NonExistentOperand(operand)
        val location = runCatching { Path.of(path) }.getOrElse {
            error -> throw when (error){
                is InvalidPathException -> InvalidFilePath(path)
                else -> error
            }
        }
        environment.saveImageToFile(location, image).getOrThrow()
    }
}