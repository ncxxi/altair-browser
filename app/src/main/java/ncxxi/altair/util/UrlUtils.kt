package ncxxi.altair.util

fun normalizeUrl(input: String): String {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return "about:blank"
    if (trimmed.startsWith("about:")) return trimmed
    if (trimmed.contains("://")) return trimmed
    val withScheme = "https://$trimmed"
    if (trimmed.contains(".") && !trimmed.contains(" ")) return withScheme
    return withScheme
}
