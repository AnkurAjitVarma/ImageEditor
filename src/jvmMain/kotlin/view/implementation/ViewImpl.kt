package view.implementation

import androidx.compose.ui.window.application
import view.Handler
import view.State
import view.View

class ViewImpl: View {
    override fun launch(state: State, handler: Handler) = application {

    }
}