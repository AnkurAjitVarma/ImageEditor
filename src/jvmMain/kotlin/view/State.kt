package view

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow

interface State {
    fun running(): StateFlow<Boolean>
    fun activeImages(): StateFlow<Map<String, ImageBitmap>>
    fun messages(): StateFlow<String>
    fun current(): StateFlow<String?>
}