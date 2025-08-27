package command.control

import command.Environment
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
//import io.mockk.justRuns
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExitTest {

    @Test
    fun `execute returns success and calls stop`() = runBlocking {
        val environment = mockk<Environment>(relaxUnitFun = true)
        every { environment.stop() } just Runs

        val result = Exit.execute(environment)

        assertTrue(result.isSuccess, "Expected a successful result")
        verify(exactly = 1) { environment.stop() }
    }

    @Test
    fun `execute returns failure when stop throws`() = runBlocking {
        val environment = mockk<Environment>()
        every { environment.stop() } throws IllegalStateException("boom")

        val result = Exit.execute(environment)

        assertTrue(result.isFailure, "Expected a failure result")
        assertInstanceOf(IllegalStateException::class.java, result.exceptionOrNull())
        verify(exactly = 1) { environment.stop() }
    }
}