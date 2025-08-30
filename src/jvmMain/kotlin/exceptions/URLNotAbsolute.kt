package exceptions

data class URLNotAbsolute(val url: String) : IllegalArgumentException("URL $url is not absolute (i.e. it lacks the protocol component).")
