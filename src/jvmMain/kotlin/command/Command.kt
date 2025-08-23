package command

interface Command {
    suspend fun execute(environment: Environment): Result<Unit>
}