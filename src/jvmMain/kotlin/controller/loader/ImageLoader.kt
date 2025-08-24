package controller.loader

import controller.loader.implementation.ImageLoaderImpl
import domain.image.Image

import java.net.URL
import java.nio.file.Path

interface ImageLoader {
    suspend fun loadFromPath(path: Path): Result<Image>
    suspend fun loadFromURL(url: URL): Result<Image>
    suspend fun save(image: Image, path: Path): Result<Unit>
    companion object{
        fun getDefault(): ImageLoader = ImageLoaderImpl
    }
}