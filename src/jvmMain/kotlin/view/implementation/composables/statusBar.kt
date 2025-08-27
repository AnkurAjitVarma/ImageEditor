package view.implementation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun statusBar(message: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFEEEEEE), // light gray background
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = message,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}