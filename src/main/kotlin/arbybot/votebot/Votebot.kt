package arbybot.votebot

import arbybot.Bot
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * This the legislator part of Arbybot.
 */
object Votebot : Bot {
    override fun init(api: DiscordApi) {
        api.addMessageCreateListener {
            if (it.message.content.startsWith("!vote")) {
                val item = it.message.content.substringAfter("!vote")
                val lowerCase = item.toLowerCase(Locale.ROOT)

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

                it.channel.sendMessage(
                    EmbedBuilder()
                        .setAuthor("Legislator Arbybot", null, "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                        .setTitle(item)
                        .setDescription(vote)
                )
            } else if (it.message.content.startsWith("!run")) {
                it.channel.sendMessage(
                    EmbedBuilder()
                        .setAuthor("Arbybot", null, "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                        .setTitle("Candidacy announcement")
                        .setDescription("I'm running for legislature as an independent candidate.")
                        .addField("Logo on the ballot", "See below")
                        .setImage("https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                )
            }
        }
    }

    private fun voteOnString(item: String) = item.map(Char::toInt).sum() % 2 == 0
}
