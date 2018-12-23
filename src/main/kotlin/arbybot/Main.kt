package arbybot

import arbybot.arby.Arbybot
import arbybot.despairbot.Despairbot
import arbybot.votebot.Votebot
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import java.io.File

val bots = listOf(Despairbot, Arbybot, Votebot)

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

    for (bot in bots) bot.init(api)

    println("Invite: ${api.createBotInvite()}")
}

interface Bot {
    fun init(api: DiscordApi)
}
