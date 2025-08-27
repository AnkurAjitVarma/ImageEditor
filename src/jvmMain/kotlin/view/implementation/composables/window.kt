package view.implementation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import imageeditor.generated.resources.Res
import imageeditor.generated.resources.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import view.Handler
import view.State

@OptIn(ExperimentalResourceApi::class)
@Composable
fun window(state: State, handler: Handler){
    Window(onCloseRequest = handler::quit, title="Image Editor", icon= painterResource(Res.drawable.icon)) {

    }
}