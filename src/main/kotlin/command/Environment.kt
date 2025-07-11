package command

import domain.image.Image
import java.net.URL
import java.nio.file.Path

interface Environment {
    fun getImage(name: String): Image?
    fun putImage(name: String, image: Image)
    suspend fun readImageFromFile(path: Path): Image
    suspend fun downloadImage(url: URL): Image
    suspend fun saveImageToFile(path: Path, image: Image)
    fun stop()
}
