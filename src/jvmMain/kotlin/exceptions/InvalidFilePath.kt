package exceptions

data class InvalidFilePath(val path: String) : IllegalArgumentException("Invalid file path: $path")
