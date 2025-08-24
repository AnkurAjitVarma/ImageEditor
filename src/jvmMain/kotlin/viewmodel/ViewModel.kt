package viewmodel

import command.Command
import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.StateFlow
import view.View
import viewmodel.implementation.ViewModelImpl
import viewmodel.skia.Skia

interface ViewModel {
    val scope: CoroutineScope
    fun launch(state: StateFlow<Model>)
    fun close()
    companion object {
        fun getDefault(commandChannel: SendChannel<Command>, messageChannel: ReceiveChannel<String>, view: View): ViewModel =
                ViewModelImpl(commandChannel, messageChannel, view, Skia.cached())
    }
}