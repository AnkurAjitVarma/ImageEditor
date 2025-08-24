package controller.implementation

import command.Command
import controller.Controller
import controller.loader.ImageLoader
import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import viewmodel.ViewModel

class ControllerImpl(initialState: Model, val commands: ReceiveChannel<Command>, val messages: SendChannel<String>, val viewModel: ViewModel, val loader: ImageLoader) :
    Controller {
    override val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}