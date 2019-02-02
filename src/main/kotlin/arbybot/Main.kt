package arbybot

import arbybot.arby.Arbybot
import arbybot.reactbot.Reactbot
import arbybot.votebot.Votebot
import com.mojang.brigadier.CommandDispatcher
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File

val bots = listOf(Arbybot, Votebot, Reactbot)

fun main(args: Array<String>) {
    val file = File("./arbybot_token.txt")

    if (!file.exists()) {
        System.err.println("The token file (arbybot_token.txt) does not exist!")
        System.exit(1)
    }

    if (args.isNotEmpty())
        when (args[0]) {
            "--help" -> {
                println("Usage:")
                println("--license: Show the license")
                println("--help: Show this help message")
                println("No arguments: Run Arbybot")

                System.exit(0)
            }

            "--license" -> {
                println(Arbybot::class.java
                    .getResourceAsStream("/arbybot/resources/license.txt")
                    .bufferedReader()
                    .lines()
                    .toArray()
                    .joinToString(separator = "\n"))

                System.exit(0)
            }
        }

    val token = file.readLines().first()
    val api = DiscordApiBuilder().setToken(token).login().join()

    val dispatcher = CommandDispatcher<MessageCreateEvent>()

    for (bot in bots) bot.init(dispatcher)

    api.addMessageCreateListener {
        if (it.message.content.startsWith("!")) {
            try {
                dispatcher.execute(it.message.content.substring(1), it)
            } catch (e: Exception) {
                it.channel.sendMessage(
                    EmbedBuilder().setTitle("Error")
                        .setDescription(e.message)
                )
            }
        }
    }

    println("Invite: ${api.createBotInvite()}")
}


@Deprecated("old")
interface BotOld {
    fun init(api: DiscordApi)
}
