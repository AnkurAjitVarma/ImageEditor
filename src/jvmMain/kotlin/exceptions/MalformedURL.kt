package exceptions

data class MalformedURL(val url: String) : IllegalArgumentException("The URL $url is malformed.") 
