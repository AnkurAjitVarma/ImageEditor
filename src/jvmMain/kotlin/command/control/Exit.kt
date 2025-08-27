package command.control

import command.Command
import command.Environment

object Exit : Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching { environment.stop() }
}