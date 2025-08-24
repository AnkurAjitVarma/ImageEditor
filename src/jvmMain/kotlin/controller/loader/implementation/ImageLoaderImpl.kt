package controller.loader.implementation

import controller.loader.ImageLoader
import domain.image.Image
import java.net.URL
import java.nio.file.Path

object ImageLoaderImpl: ImageLoader {
    override suspend fun loadFromPath(path: Path): Result<Image> {
        TODO("Not yet implemented")
    }

    override suspend fun loadFromURL(url: URL): Result<Image> {
        TODO("Not yet implemented")
    }

    override suspend fun save(image: Image, path: Path): Result<Unit> {
        TODO("Not yet implemented")
    }
}