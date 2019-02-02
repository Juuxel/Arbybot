package arbybot.arby

import arbybot.Bot
import arbybot.util.ArgumentBuilders
import arbybot.util.replace
import arbybot.util.random
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

/**
 * Pure perfection.
 */
object Arbybot : Bot {
    private val imageMessages = listOf(
        "%AnyPacifist told me to post this: %ImageUrl",
        "dress up as %PacifistPerson for halloween: %Halloween",
        "here's the latest propaganda: %Propaganda",
        "vote for us to smash the status quo! %Propaganda",
        "we need to work against %AnyPacifist %ImageUrl",
        "do not vote for %AnyPacifist %Propaganda"
    )
    private val catchphrases = listOf(
        "so what about giving election bonuses to %BonusTarget?",
        "%AnyPacifist happens to be pro-%PacifistGroup",
        "when will we talk about %AnyPacifist and their backroom deals?",
        "when will we talk about %AnyPacifist and the fact that they're just a %AnyPNoThe supporter?",
        "%AnyPacifist is trying to conspire against new players",
        "so I don't expect those coming in here new to have the energy to work months to change the status quo",
        "%AnyPacifist is trying to undemocratically enforce rules without input from citizens",
        "let's ban all forms of the %PacifistGroup, then %AnyPacifist. that's a good place to start",
        "If you can't get enough of me, you can join the %GoodGroup.",
        "If you can't get enough of %PacifistPTemplate, you can join the %PacifistGroup.",
        """who is playing the game, really?
        the citizens or %AnyPacifist
        nearly all laws created are just procedures and specifies how to do stuff
        a few allows the %GoodGroup to order %AnyPacifist to do stuff
        this rarely happens or is rather unimportant and would happen anyway
        we just sit here week in and week out arguing about procedures while the game itself is controlled by %AnyPacifist playing it without much input from anyone else.""".trimIndent(),
        "we need to smash the status quo",
        "let's revive the community by creating new parties and alliances from the ashes",
        "is it the joking sarcastic RB or the honest and serious RB speaking, does anyone know the difference?",
        "why can't we just have a one party system where everyone's a member of the %GoodGroup? Peace and happiness!",
        "%AnyPacifist hasn't vetoed a single bill harmful to OUR community",
        "the leg works\nthe %PacifistGroup is preventing it from doing its job",
        "RB: \"so what's your opinion on what we're going to eat tomorrow?\"\n%PacifistPTemplate: \"A BACKROOM DEAL!!!11!!1\"",
        "MOST CENTRIST?\nyou're extremists\nIU is extreme, but you can't call yourself the most moderate where you are as extreme in the opposite direction.",
        "this is why %AnyPacifist shouldn't have this much power",
        "the %PacifistGroup as a institution is flawed\nthe strives to benefit itself, not the community",
        """so remove one of the braches to fix it
        I haven't seen them enough in-action yet
        but the concept corrupts, you stop being about the people, it start being about what you can do as a minister""".trimIndent(),
        "but sometimes its part of you job being limited in what you can do\n%AnyPacifist needs to accept that",
        "there is loyalty to %AnyPacifist, not to the people, not to the parties",
        "the greatest thing about leaving is not seeing or hearing the %PacifistGroup absolutely stupid shittalk that's filled with inaccurcies and dream thinking. All in the name of good morality and muh \"pro-peace cooperation\" is actually not a steering the game. all of it gone, peace and quiet, no bs, no Jonesion, no speakers, no nothing, just emptiness, emptiness and joy. :smiley: :wave:",
        "if that new amendment is real, you're all really pathetic and this game is a shitty waste of time, bye",
        "So we shouldn't have had a snap vote even if it stopped the razing of innocent civilians in Songhai?",
        "we could make a law pushing the terms forward (and do that indefinately and keep power for eternity...muahahahhaha...wait)",
        "This is what peak hypocrisy is\nRetro: no, this is the taste of your own medicine"
    ) + imageMessages

    private val pacifistPeople = listOf(
        "Jonesion", "Joe Parrish", "Thorn", "Taylor", "Fruity-Tree", "Charlie_Zulu",
        "Charisarian", "DerJonas", "Femamerica", "Reinaldi", "Sun_Tzu_Warrior", "SpacePolitics", "Tiberius",
        "Alexander the Great", "Juuz", "Big Bobert", "Panda", "taqn", "Bis", "Bird", "Peppeghetti Sparoni"
    )

    private val pacifistGroups = listOf(
        "IFP", "PU", "pacifist conspiracy", "UoP", "L&U", "Order of Dao", "celestial party",
        "unelected moderation", "ministry", "old elite", "illuminati", "exec",
        "FDC", "court", "supreme court", "Gentry of Friendship", "Communist Party", "Monarchist Party",
        "ANP"
    )

    private val pacifistPersonTemplates = listOf(
        "%PacifistGroup member %PacifistPerson", "%PacifistPerson", "the leader of the %PacifistGroup"
    )

    private val anyPacifist = listOf("the %PacifistGroup", "%PacifistPTemplate")
    private val anyPacifistNoThe = listOf("%PacifistGroup", "%PacifistPTemplate")
    private val goodGroups = listOf("SRP", "IU", "NUKE", "pro-war parties", "parliament", "leg"/*, "monarchist conspiracy"*/)
    private val bonusTargets = goodGroups + listOf("new parties", "%AnyPacifist")
    private val halloween = listOf(
        "https://images-na.ssl-images-amazon.com/images/I/61PVT9-pT5L._SY346_.jpg",
        "https://cdn.discordapp.com/attachments/208984105310879744/486677575289012227/thebeach.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/482826657321844766/Change.gif",
        "https://cdn.discordapp.com/attachments/458808272539877377/472745193422979082/IUStrongman1.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/471750543526723584/IU.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/464524678745292801/UnitedFront4.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/463073299820183583/UnitedFront.jpg",
        "https://warriorpublications.files.wordpress.com/2012/11/smash-pacifism.jpg",
        "https://s3.amazonaws.com/lowres.cartoonstock.com/social-issues-pacifist-pacifism-proud-thugs-thuggish-rnen98_low.jpg"
    )

    private val propaganda = listOf(
        "https://cdn.discordapp.com/attachments/458808272539877377/486263003692662784/ArrowsofPacifismMain.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/485480309165391882/LGBT2.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/483652648533229578/Allthesame.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/475423145289842698/Alignment.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/482826657321844766/Change.gif",
        "https://cdn.discordapp.com/attachments/458808272539877377/472745193422979082/IUStrongman1.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/471750543526723584/IU.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/464524678745292801/UnitedFront4.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/463073299820183583/UnitedFront.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/501632627846021140/SRPFlag.png",
        "https://s3.amazonaws.com/lowres.cartoonstock.com/social-issues-pacifist-pacifism-proud-thugs-thuggish-rnen98_low.jpg",
        "https://cdn.discordapp.com/attachments/468201679041593364/527228034164457494/srp.png"
    )

    private val urls = (listOf(
        "https://cdn.discordapp.com/attachments/278254099768541204/502485519079440385/PUWin.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/497141860209393664/celezielvote2.jpg",
        "https://cdn.discordapp.com/attachments/458808272539877377/485138195936182272/Logo4Transparent_1b.png",
        "https://cdn.discordapp.com/attachments/458808272539877377/498623647964200971/image0.png",
        "https://cdn.discordapp.com/attachments/473947506636488719/502821488504864768/Screen_Shot_2018-10-16_at_13.39.20.png",
        "https://cdn.discordapp.com/attachments/473947506636488719/502821568343441430/Screen_Shot_2018-10-16_at_10.39.37.png",
        "https://cdn.discordapp.com/attachments/473947506636488719/502822192589963265/unknown.png",
        "https://cdn.discordapp.com/attachments/473947506636488719/502822022221398017/unknown.png",
        "https://cdn.discordapp.com/attachments/208984105310879744/486665428425244673/23.PNG"
    ) + halloween + propaganda).distinct()
    private val botHaters = listOf("RB33", "Jouhes")
    private val botNames = listOf("Despairbot", "Arbybot")

    private val religion = listOf(
        "Arby, blessed be his name\n" +
                "Thy Kingdom come, Thy will be done, in dciv as it is in dhoi\n" +
                "Give us this day our daily conspiracies\n" +
                "Forgive us our memes, as we forgive those who meme against us\n" +
                "And lead us not into pacifism but deliver us into war\n" +
                "Amen (Arby 1:2)",
        "Arby, blessed be his name, he has graced us with his presence once more (Arby 1:1)"
    ) + catchphrases.mapIndexed { i, str -> "$str (Arby 2:${i + 1})" }

    override fun init(dispatcher: CommandDispatcher<MessageCreateEvent>) {
        dispatcher.register(
            ArgumentBuilders.literal("arby").executes {
                sendArby(it.source.channel)
                1
            }.then(ArgumentBuilders.literal("image").executes {
                sendImage(it.source.channel)
                1
            }).then(ArgumentBuilders.literal("number").then(ArgumentBuilders.argument("id", IntegerArgumentType.integer(0, catchphrases.lastIndex))
                .executes {
                    sendMessage(it.source.channel, listOf(catchphrases[IntegerArgumentType.getInteger(it, "id")]))
                    1
                }
            )).then(ArgumentBuilders.literal("book").executes {
                sendMessage(it.source.channel, religion)
                1
            }))
    }

    private fun sendArby(channel: TextChannel) = sendMessage(channel, catchphrases)
    private fun sendImage(channel: TextChannel) = sendMessage(channel, imageMessages)

    private fun sendMessage(channel: TextChannel, source: List<String>) {
        val (message, image) = catchphraseReplace(source.random())
        channel.sendMessage(
            EmbedBuilder()
                .setAuthor("Most Honourable Great Saviour of the People Arby", null, "https://cdn.discordapp.com/emojis/495540713031991306.png?v=1")
                .setDescription(message)
                .setImage(image)
        )
    }

    private fun catchphraseReplace(str: String): Pair<String, String?> {
        var url: String? = null
        return str.replace {
            "%BonusTarget" with bonusTargets
            "%AnyPNoThe" with anyPacifistNoThe
            "%AnyPacifist" with anyPacifist
            "%PacifistPTemplate" with pacifistPersonTemplates
            "%PacifistPerson" with pacifistPeople
            "%PacifistGroup" with pacifistGroups
            "%GoodGroup" with goodGroups
            "%ImageUrl" with {
                url = urls.random()
                ""
            }
            "%Halloween" with {
                url = halloween.random()
                ""
            }
            "%Propaganda" with {
                url = propaganda.random()
                ""
            }
            "%BotHater" with botHaters
            "%BotName" with botNames
        } to url
    }
}
