package arbybot.util

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.vdurmont.emoji.EmojiManager

object EmojiListArgumentType : ArgumentType<EmojiListArgumentType.EmojiList> {
    private val UNKNOWN_EMOJI = DynamicCommandExceptionType {
        LiteralMessage("Unknown emoji: $it")
    }

    private fun emoji(letter: Char): String? = when (letter) {
        'I' -> EmojiManager.getForAlias("information_source")?.unicode
        'A' -> EmojiManager.getForAlias("a")?.unicode
        'B' -> EmojiManager.getForAlias("b")?.unicode
        'O' -> EmojiManager.getForAlias("o2")?.unicode
        else -> EmojiManager.getForAlias("regional_indicator_symbol_${letter.toLowerCase()}")?.unicode
    }

    override fun <S> parse(reader: StringReader) =
        reader.readUnquotedString().map {
            emoji(it) ?: throw UNKNOWN_EMOJI.createWithContext(reader, it)
        }.let(::EmojiList)

    fun getEmojiList(context: CommandContext<*>, name: String): EmojiList =
        context.getArgument(name, EmojiList::class.java)

    class EmojiList(contents: List<String>) : List<String> by contents
}
