package viewmodel

import domain.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface ViewModel {
    val scope: CoroutineScope
    fun launch(state: StateFlow<Model>)
    fun close()
}