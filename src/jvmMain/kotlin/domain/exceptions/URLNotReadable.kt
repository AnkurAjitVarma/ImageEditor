package domain.exceptions

import java.net.URL

data class URLNotReadable(val url: URL) : IllegalArgumentException("URL $url is not readable.")
