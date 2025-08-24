package viewmodel.skia

import androidx.compose.ui.graphics.ImageBitmap
import domain.image.Image
import viewmodel.skia.implementation.CachedSkiaImpl
import java.util.concurrent.ConcurrentHashMap
import org.jetbrains.skia.Image as SkiaImage

interface Skia {
    fun visualize(images: Map<String, Image>): Map<String, ImageBitmap>
    fun toSkiaImage(image: Image): SkiaImage
    companion object{
        fun cached(): Skia = CachedSkiaImpl(ConcurrentHashMap())
    }
}