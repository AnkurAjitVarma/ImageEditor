package view.implementation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import view.Handler
import view.State
import view.View
import view.implementation.composables.window

class ViewImpl: View {
    override fun launch(state: State, handler: Handler) = application {
        val running by state.running().collectAsState()
        if (running) window(state, handler) else exitApplication()
    }
}