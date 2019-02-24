package idioten

import com.andreapivetta.kolor.*


fun main() {
    val deck = Deck()
    deck.shuffle()
    var fourCards = deck.getFourCards()
    println(fourCards)
    fourCards = deck.getFourCards()
    println(fourCards)
    fourCards = deck.getFourCards()
    println(fourCards)
}

class Deck {
    private val cards: MutableList<Card> = mutableListOf<Card>()

    init {
        enumValues<Suits>().forEach { suit ->
            enumValues<Rank>().forEach { rank ->
                cards.add(Card(suit, rank))
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

    fun getFourCards(): List<Card> {
        val result = mutableListOf<Card>()
        result.add(cards.removeAt(0))
        result.add(cards.removeAt(0))
        result.add(cards.removeAt(0))
        result.add(cards.removeAt(0))
        return result
    }
}

enum class Suits(val str: String) {
    Spades("\u2660"), Hearts(Kolor.foreground("\u2665", Color.RED)), Diamonds(
        Kolor.foreground(
            "\u2666",
            Color.RED
        )
    ),
    Clubs("\u2663");

    override fun toString(): String {
        return str
    }
}

enum class Rank(private val value: Int) {
    Ace(14),
    Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10),
    Jack(11), Queen(12), King(13);


    override fun toString(): String {
        return if (ordinal in 2..9) ordinal.toString() else name.substring(0, 1)
    }
}

data class Card(val suit: Suits, val rank: Rank) {
    override fun toString(): String {
        return suit.toString() + rank.toString()
    }
}

