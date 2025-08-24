package viewmodel.skia.implementation

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import domain.image.Image
import org.jetbrains.skia.ImageInfo
import viewmodel.skia.Skia
import org.jetbrains.skia.Image as SkiaImage

class CachedSkiaImpl(private val cache:MutableMap<Image, ImageBitmap>): Skia {
    override fun visualize(images: Map<String, Image>): Map<String, ImageBitmap> {
        val result = images.mapValues { (_, image) ->
            cache.getOrPut(image) {
                toSkiaImage(image).toComposeImageBitmap()
            }
        }
        cache.keys.retainAll(images.values.toSet())
        return result
    }

    override fun toSkiaImage(image: Image): SkiaImage =
        SkiaImage.makeRaster(
            ImageInfo.makeN32Premul(image.width(), image.height()),
            image.toByteArray(),
            image.width() * 4
        )
}