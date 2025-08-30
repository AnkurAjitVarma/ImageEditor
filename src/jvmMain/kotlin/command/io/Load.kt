package command.io

import command.Command
import command.Environment
import exceptions.InvalidFilePath
import java.nio.file.InvalidPathException
import java.nio.file.Path

data class Load(val path: String, val name: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val file = runCatching { Path.of(path) }.getOrElse {
            error -> throw when (error){
                is InvalidPathException -> InvalidFilePath(path)
                else -> error
            }
        }
        val image = environment.readImageFromFile(file).getOrThrow()
        environment.putImage(name, image)
    }
}