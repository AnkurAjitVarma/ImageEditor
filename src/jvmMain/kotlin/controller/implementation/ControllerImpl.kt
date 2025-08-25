package controller.implementation

import command.Command
import command.Environment
import controller.Controller
import controller.loader.ImageLoader
import domain.image.Image
import domain.model.Model
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import viewmodel.ViewModel
import java.net.URL
import java.nio.file.Path
import kotlin.coroutines.cancellation.CancellationException

class ControllerImpl(initialState: Model, val commands: ReceiveChannel<Command>, val messages: SendChannel<String>, val viewModel: ViewModel, val loader: ImageLoader) :
    Controller {
    private val applicationState = MutableStateFlow(initialState)

    private val environment = object : Environment{
        override fun getImage(name: String): Image? = applicationState.value.getImage(name)

        override fun putImage(name: String, image: Image) = applicationState.update { it.putImage(name, image) }

        override suspend fun readImageFromFile(path: Path): Result<Image> = loader.loadFromPath(path)

        override suspend fun downloadImage(url: URL): Result<Image> = loader.loadFromURL(url)

        override suspend fun saveImageToFile(path: Path, image: Image): Result<Unit> = loader.save(image, path)

        override fun stop() = this@ControllerImpl.stop()
    }

    override val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        scope.launch(CoroutineName("Command Event Loop")){
            commands.consumeAsFlow().collect { command ->
                launch {
                    command.execute(environment)
                        .onFailure { exception -> messages.send(exception.message ?: "Unknown error") }
                }
            }
        }
    }

    override fun start() = viewModel.launch(applicationState.asStateFlow())

    override fun stop() {
        viewModel.close()
        val exception = CancellationException("Application stopped")
        commands.cancel()
        scope.cancel(exception)
    }
}