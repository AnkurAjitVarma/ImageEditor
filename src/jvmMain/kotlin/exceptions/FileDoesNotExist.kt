package exceptions

import java.nio.file.Path

data class FileDoesNotExist(val path: Path) : IllegalArgumentException("File does not exist at the given path: $path")
