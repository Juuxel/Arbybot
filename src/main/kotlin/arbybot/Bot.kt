package arbybot

import com.mojang.brigadier.CommandDispatcher
import org.javacord.api.event.message.MessageCreateEvent

interface Bot {
    fun init(dispatcher: CommandDispatcher<MessageCreateEvent>)
}
