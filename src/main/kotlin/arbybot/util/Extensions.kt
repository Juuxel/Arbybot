package arbybot.util

import java.util.concurrent.ThreadLocalRandom

fun <T> List<T>.random(): T {
    require(isNotEmpty())
    return this[ThreadLocalRandom.current().nextInt(size)]
}
