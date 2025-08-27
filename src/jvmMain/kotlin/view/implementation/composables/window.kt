package view.implementation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val activeImages by state.activeImages().collectAsState()
    val current by state.current().collectAsState()
    val message by state.messages().collectAsState()

    Window(onCloseRequest = handler::quit, title = "Image Editor", icon = painterResource(Res.drawable.icon)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // pushes status bar to bottom
        ) {
            Box(modifier = Modifier.weight(1f)) {
                // TODO: place your image editor content here
            }
            statusBar(message = message)
        }

    }
}