package command.io

import command.Command
import command.Environment
import java.nio.file.Path

data class Load(val path: Path, val name: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = environment.readImageFromFile(path).mapCatching { image -> environment.putImage(name, image) }
}