package viewmodel.implementation

import command.Command
import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.StateFlow
import view.View
import viewmodel.ViewModel
import viewmodel.skia.Skia

class ViewModelImpl(private val commandChannel: SendChannel<Command>, private val messageChannel: ReceiveChannel<String>, private val view: View, private val skia: Skia) : ViewModel {

    override val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun launch(state: StateFlow<Model>) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}