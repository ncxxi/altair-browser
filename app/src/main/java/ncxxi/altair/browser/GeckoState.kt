package ncxxi.altair.browser

data class GeckoState(
    val url: String = "about:blank",
    val title: String = "",
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
)
