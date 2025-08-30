package command.io

import command.Command
import command.Environment
import exceptions.MalformedURL
import exceptions.URLNotAbsolute
import java.net.MalformedURLException
import java.net.URI

data class Download(val url: String, val name: String) : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val downloadUrl = runCatching { URI(url).toURL() }.getOrElse {
            error -> when(error) {
                is MalformedURLException -> throw MalformedURL(url)
                is IllegalArgumentException -> throw URLNotAbsolute(url)
                else -> throw error
            }
        }
        val image = environment.downloadImage(downloadUrl).getOrThrow()
        environment.putImage(name, image)
    }
}
