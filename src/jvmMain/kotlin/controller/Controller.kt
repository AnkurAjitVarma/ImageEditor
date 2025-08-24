package controller

import command.Command
import controller.implementation.ControllerImpl
import controller.loader.ImageLoader
import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import viewmodel.ViewModel

interface Controller {
    val scope: CoroutineScope
    fun start()
    fun stop()
    companion object{
        fun getDefault(initialState: Model,
                       commands: ReceiveChannel<Command>,
                       messages: SendChannel<String>,
                       viewModel: ViewModel
        ): Controller = ControllerImpl(initialState, commands, messages, viewModel, ImageLoader.getDefault())
    }
}