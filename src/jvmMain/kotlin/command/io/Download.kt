package command.io

import command.Command
import command.Environment
import java.net.URL

data class Download(val url: URL, val name: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = environment.downloadImage(url).mapCatching { image -> environment.putImage(name, image) }
}
