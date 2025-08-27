package viewmodel.implementation

import androidx.compose.ui.graphics.ImageBitmap
import command.Command
import command.control.Exit
import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import view.Handler
import view.State
import view.View
import viewmodel.ViewModel
import viewmodel.skia.Skia

class ViewModelImpl(private val commandChannel: SendChannel<Command>, private val messageChannel: ReceiveChannel<String>, private val view: View, private val skia: Skia) : ViewModel {

    override val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val applicationRunning = MutableStateFlow(true)

    private val current = MutableStateFlow<String?>(null)

    private fun state(model: StateFlow<Model>): State =
        object : State {
            override fun running(): StateFlow<Boolean> = applicationRunning.asStateFlow()
            override fun activeImages(): StateFlow<Map<String, ImageBitmap>> =
                model
                    .map(Model::asMap)
                    .map(skia::visualize)
                    .stateIn(scope, SharingStarted.Eagerly, emptyMap())
            override fun messages(): StateFlow<String> = messageChannel.receiveAsFlow().stateIn(scope, SharingStarted.Eagerly, "")
            override fun current(): StateFlow<String?> = current.asStateFlow()
        }

    private val handler = object : Handler {
        override fun quit() {
            scope.launch {
                commandChannel.send(Exit)
            }
        }

    }

    override fun launch(state: StateFlow<Model>) = view.launch(state(state), handler)

    override fun close() {
        applicationRunning.value = false
        scope.cancel()
        commandChannel.close()
    }
}