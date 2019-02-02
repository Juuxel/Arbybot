package arbybot.votebot

import arbybot.Bot
import arbybot.util.ArgumentBuilders
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.util.*

/**
 * This the legislator part of Arbybot.
 */
object Votebot : Bot {
    override fun init(dispatcher: CommandDispatcher<MessageCreateEvent>) {
        dispatcher.register(ArgumentBuilders.literal("vote").then(ArgumentBuilders.argument("proposal", StringArgumentType.greedyString()).executes {
            val proposal = StringArgumentType.getString(it, "proposal")
            val lowerCase = proposal.toLowerCase(Locale.ROOT)

            val vote = when {
                "peace" in lowerCase -> false
                "ministry" in lowerCase -> true
                "war" in lowerCase -> true
                "parliament" in lowerCase -> true
                "friendship" in lowerCase -> false
                "legislat" in lowerCase -> true
                else -> voteOnString(lowerCase)
            }.let {
                if (it) "Yea" else "Nay"
            }

            it.source.channel.sendMessage(
                EmbedBuilder()
                    .setAuthor(
                        "Legislator Arbybot",
                        null,
                        "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1"
                    )
                    .setTitle(proposal)
                    .setDescription(vote)
            )
            1
        }))

        /*
        *  else if (it.message.content.startsWith("!run")) {
                it.channel.sendMessage(
                    EmbedBuilder()
                        .setAuthor("Arbybot", null, "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                        .setTitle("Candidacy announcement")
                        .setDescription("I'm running for legislature as an independent candidate.")
                        .addField("Logo on the ballot", "See below")
                        .setImage("https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                )
            }*/
    }

    private fun voteOnString(item: String) = item.map(Char::toInt).sum() % 2 == 0
}
