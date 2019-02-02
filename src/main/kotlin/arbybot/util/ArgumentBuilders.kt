package arbybot.util

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import org.javacord.api.event.message.MessageCreateEvent

object ArgumentBuilders {
    fun literal(name: String): LiteralArgumentBuilder<MessageCreateEvent> = LiteralArgumentBuilder.literal(name)
    fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<MessageCreateEvent, T> =
        RequiredArgumentBuilder.argument(name, type)
}