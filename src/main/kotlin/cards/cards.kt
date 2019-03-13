package cards

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor

enum class Suits(private val str: String, private val color: Color) {
    Spades("\u2660", Color.DARK_GRAY),
    Hearts("\u2665", Color.RED),
    Diamonds("\u2666", Color.LIGHT_RED),
    Clubs("\u2663", Color.LIGHT_GRAY);

    override fun toString(): String {
        return str
    }

    fun toColoredString(): String {
        return Kolor.foreground(
            str,
            color
        )
    }
}

enum class Rank(private val value: Int) {
    Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10),
    Jack(11), Queen(12), King(13), Ace(14);

    override fun toString(): String {
        // we want a single character for better printing when showing cards in columns
        // hence 10 = X
        return when (value) {
            in 2..9 -> value.toString()
            10 -> "X"
            else -> name.substring(0, 1)
        }
    }
}

data class Card(val rank: Rank, val suit: Suits) {

    operator fun compareTo(card: Card): Int {
        return this.rank.compareTo(card.rank)
    }

    override fun toString(): String {
        return rank.toString() + suit.toString()
    }

    fun toColoredString(): String {
        return rank.toString() + suit.toColoredString()
    }
}

class EmptyDeckException : Throwable()

// Add stack methods to MutableList<Card>
fun MutableList<Card>.push(item: Card) = add(item)

fun MutableList<Card>.peek(): Card? = lastOrNull()
fun MutableList<Card>.pop(): Card? {
    val item = lastOrNull()
    if (!isEmpty()) {
        removeAt(size - 1)
    }
    return item
}

class Deck {
    private val cards: MutableList<Card> = mutableListOf()

    init {
        enumValues<Suits>().forEach { suit ->
            enumValues<Rank>().forEach { rank ->
                cards.push(Card(rank, suit))
            }
        }
    }

    fun shuffle() {
        cards.shuffle()
    }


    override fun toString(): String {
        return cards.toString()
    }

    fun size(): Int {
        return cards.size
    }

    fun getFourCards(): Array<Card> {
        val result = mutableListOf<Card>()

        for (i in 1..4) {
            val card = cards.pop()
            if (card != null) {
                result.add(card)
            } else {
                throw EmptyDeckException()
            }
        }

        return result.toTypedArray()
    }
}
