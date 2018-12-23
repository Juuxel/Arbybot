package arbybot.util

class ReplaceContext(var content: String) {
    infix fun String.with(function: () -> String) {
        val windows = content.windowed(length)
        var occurences = 0

        for (w in windows) {
            if (w == this)
                occurences++
        }

        for (i in 1..occurences)
            content = content.replaceFirst(this, function())
    }

    infix fun String.with(source: List<String>) = with { source.random() }
}

inline fun String.replace(block: ReplaceContext.() -> Unit) =
    ReplaceContext(this).apply(block).content
