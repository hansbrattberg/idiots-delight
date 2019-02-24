package idioten

import com.andreapivetta.kolor.*


fun main() {
    val deck = Deck()
    deck.shuffle()
    print(deck)
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
}

enum class Suits(val str: String) {
    Spades("\u2660"), Hearts(Kolor.foreground("\u2665", Color.RED)), Diamonds(Kolor.foreground("\u2666", Color.RED)), Clubs("\u2663");

    override fun toString(): String {
        return str
    }
}

enum class Rank(val value: Int) {
    Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10),
    Jack(11), Queen(12), King(13),
    Ace(14)
}

data class Card(val suit: Suits, val rank: Rank) {
    override fun toString(): String {
        return suit.toString() + rank.toString()
    }
}

