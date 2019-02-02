package arbybot.reactbot

import arbybot.Bot
import arbybot.util.ArgumentBuilders
import arbybot.util.EmojiListArgumentType
import com.mojang.brigadier.CommandDispatcher
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

object Reactbot : Bot {
    override fun init(dispatcher: CommandDispatcher<MessageCreateEvent>) {
        dispatcher.register(ArgumentBuilders.literal("react").then(ArgumentBuilders.argument("reaction", EmojiListArgumentType).executes {
            val reaction = EmojiListArgumentType.getEmojiList(it, "reaction")
            for (emoji in reaction) {
                it.source.channel
                    .getMessages(2).get()
                    .oldestMessage.get()
                    .addReaction(emoji)
            }
            1
        }))

        dispatcher.register(ArgumentBuilders.literal("rbhelp").executes {
            val builder = StringBuilder()

            dispatcher.getSmartUsage(dispatcher.root, it.source).values.forEach { command ->
                builder.append("!$command\n")
            }
            it.source.channel.sendMessage(EmbedBuilder()
                .setAuthor("Help", null, "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                .setDescription(builder.toString()))
            1
        })
    }
}
