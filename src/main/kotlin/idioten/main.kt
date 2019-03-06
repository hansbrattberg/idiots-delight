package idioten

import com.andreapivetta.kolor.*


fun main() {


    //Playing card unicode
    println(
        Kolor.foreground(
            "\uD83C\uDCA1",
            Color.RED
        )
    )


    val deck = Deck()
    deck.shuffle()

    val table = Table()

    table.addFourNewCards(deck.getFourCards())
    table.addFourNewCards(deck.getFourCards())
    table.addFourNewCards(deck.getFourCards())

    println(table.toString())

}

class Table {
    private val columns: Array<MutableList<Card>> =
        arrayOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

    fun allDifferentSuits(cards: Array<Card?>): Boolean {
        var heartCount = 0
        var spadesCount = 0
        var clubsCount = 0
        var diamondsCount = 0
        cards.forEach {
            if (it != null && it.suit == Suits.Hearts) {
                heartCount++
            }
            if (it != null && it.suit == Suits.Spades) {
                spadesCount++
            }
            if (it != null && it.suit == Suits.Clubs) {
                clubsCount++
            }
            if (it != null && it.suit == Suits.Diamonds) {
                diamondsCount++
            }
        }
        return heartCount <= 1 && spadesCount <= 1 && clubsCount <= 1 && diamondsCount <= 1
    }

    fun removeLowestInSuit(cardColumn1: MutableList<Card>, cardColumn2: MutableList<Card>): Card? {
        val card1: Card? = cardColumn1.peek()
        val card2: Card? = cardColumn2.peek()

        if (card1 != null && card2 != null && card1.suit == card2.suit) {
            if (card1 < card2) {
                return cardColumn1.pop()
            } else if (card1 > card2) {
                return cardColumn2.pop()
            } else {
                throw SameCardException()
            }
        }

        return null
    }


    fun addFourNewCards(fourCards: Array<Card>) {
        columns[0].push(fourCards[0])
        columns[1].push(fourCards[1])
        columns[2].push(fourCards[2])
        columns[3].push(fourCards[3])
    }


    fun lowestCard(cards: Array<Card>, suit: Suits): Card? {
        var lowest: Card? = null
        for (card in cards) {
            if (card.suit == suit) {
                if (lowest == null) {
                    lowest = card
                } else {
                    if (lowest.rank > card.rank) {
                        lowest = card
                    }
                }
            }
        }
        return lowest
    }


    override fun toString(): String {
        var result = ""

        var maxSize = columns[0].size
        for (i in 1..3) {
            maxSize = maxOf(maxSize, columns[i].size)
        }

        for (row in 0 until maxSize) {
            for (col in 0..3) {
                val card = columns[col].elementAt(row)
                result += if (card != null) {
                    columns[col].elementAt(row).toString()
                } else {
                    "--"
                }
                if (col < 3) {
                    result += " "
                }
            }

            if (row < maxSize - 1) {
                result += "\n"
            }
        }

        return result
    }

    fun peekTopRow(): Array<Card?> {
        return arrayOf(columns[0].peek(), columns[1].peek(), columns[2].peek(), columns[3].peek())
    }

    fun play() {
        while (!allDifferentSuits(peekTopRow())) {
            for (col1 in 0..2) {
                for (col2 in col1 + 1..3) {
                    removeLowestInSuit(columns[col1], columns[col2])
                }
            }
        }
    }

}


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


// lägg 4 kort på bordet
// Ta en färg
// Ta bort lägsta av denna färg.
// Om någon borttagen, börja om
// annars ta nästa färg
// så fort någon tagits bort, börja om


class Deck {
    private val cards: MutableList<Card> = mutableListOf()

    init {
        enumValues<Suits>().forEach { suit ->
            enumValues<Rank>().forEach { rank ->
                cards.push(Card(suit, rank))
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

class EmptyDeckException : Throwable()
class SameCardException : Throwable()

enum class Suits(private val str: String) {
    Spades("\u2660"), Hearts(Kolor.foreground("\u2665", Color.RED)), Diamonds(
        Kolor.foreground(
            "\u2666",
            Color.RED
        )
    ),
    Clubs("\u2663");

    /*
    override fun toString(): String {
        return str
    }
    */
}

enum class Rank(private val value: Int) {
    Ace(14),
    Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10),
    Jack(11), Queen(12), King(13);


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

data class Card(val suit: Suits, val rank: Rank) {

    operator fun compareTo(card: Card): Int {
        return this.rank.compareTo(card.rank)
    }

    override fun toString(): String {
        return suit.toString() + rank.toString()
    }
}

