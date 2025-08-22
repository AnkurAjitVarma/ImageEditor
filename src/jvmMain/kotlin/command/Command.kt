package command

fun interface Command {
    suspend fun execute(environment: Environment): Result<Unit>
}