import command.Command
import controller.Controller
import domain.model.Model
import kotlinx.coroutines.channels.Channel
import view.View
import viewmodel.ViewModel

fun main(){
    val commandChannel = Channel<Command>(Channel.UNLIMITED)
    val messageChannel = Channel<String>(Channel.UNLIMITED)
    val controller = Controller.getDefault(Model.getDefault(), commandChannel, messageChannel, ViewModel.getDefault(commandChannel, messageChannel, View.getDefault()))
    controller.start()
}