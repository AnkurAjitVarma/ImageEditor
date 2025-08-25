package domain.exceptions

data class ImageFormatNotSupported(val format: String) : UnsupportedOperationException("The image format $format is not supported.")
