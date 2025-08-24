package viewmodel.skia.implementation

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import domain.image.Image
import org.jetbrains.skia.ImageInfo
import viewmodel.skia.Skia
import org.jetbrains.skia.Image as SkiaImage

class CachedSkiaImpl(private val cache:MutableMap<Image, ImageBitmap>): Skia {
    override fun visualize(images: Map<String, Image>): Map<String, ImageBitmap> {
        TODO("Not yet implemented")
    }

    override fun toSkiaImage(image: Image): SkiaImage {
        TODO("Not yet implemented")
    }

}