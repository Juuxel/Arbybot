package arbybot.despairbot

import com.vdurmont.emoji.EmojiManager
import arbybot.Bot
import org.javacord.api.DiscordApi
import java.util.concurrent.ThreadLocalRandom

/**
 * This bot reacts to messages.
 */
object Despairbot : Bot {
    fun emoji(letter: Char): String? = when (letter) {
        'I' -> EmojiManager.getForAlias("information_source")?.unicode
        else -> EmojiManager.getForAlias("regional_indicator_symbol_$letter")?.unicode
    }

    private val despairEmojis = arrayOf(emoji('d'), emoji('e'), emoji('s'), emoji('p'), emoji('a'), emoji('i'), emoji('r'))
    private val allEmojis = (('a'..'z').toList() + 'I').map(::emoji).toTypedArray()

    override fun init(api: DiscordApi) {
        api.addMessageCreateListener {
            val content = it.message.content

            fun isSending(command: String, randomChance: Boolean = true) =
                randomChance && ThreadLocalRandom.current().nextInt(25) == 0 ||
                    content.startsWith(command, ignoreCase = true) ||
                    content.endsWith(command, ignoreCase = true)

            if (isSending("^despair", false))
                it.channel.getMessages(2).get().oldestMessage.get().addReactions(*despairEmojis)

            if (isSending("+despair"))
                it.channel.sendMessage("Despair")

            when {
                content.startsWith("!cleardespairs", ignoreCase = true) ->
                    it.channel.getMessages(10).get().forEach { msg ->
                        if (msg.reactions.isNotEmpty())
                            msg.removeOwnReactionsByEmoji(*despairEmojis)
                    }

                content.startsWith("!clearreacts", ignoreCase = true) ->
                    it.channel.getMessages(10).get().forEach { msg ->
                        if (msg.reactions.isNotEmpty())
                            msg.removeOwnReactionsByEmoji(*allEmojis)
                    }

                content.startsWith("^react:", ignoreCase = true) -> it.channel
                    .getMessages(2).get()
                    .oldestMessage.get()
                    .addReactions(
                        *content.substring(7)
                            .asSequence()
                            .distinct()
                            .map(::emoji)
                            .filterNotNull()
                            .toList().toTypedArray()
                    )
            }
        }
    }
}
