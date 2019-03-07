package idiotsdelight

import cards.*

fun main() {

    /*
    //Playing card unicode
    println(
        Kolor.foreground(
            "\uD83C\uDCA1",
            Color.RED
        )
    )
    */

    var count = 0
    do {
        val table = IdiotsDelight()
        val cardsLeft = table.play(false)
        print(cardsLeft.toString() + ",")
        //println(table)
        //println()
        count++
    } while (cardsLeft > 4)

    println()
    println(count)
}


class SameCardException(private val card1: Card, private val card2: Card) : Throwable() {

    override fun toString(): String {
        return card1.toString() + card2.toString()
    }
}


class IdiotsDelight {
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

        var result: Card? = null

        if (card1 != null && card2 != null && card1.suit == card2.suit) {
            result = when {
                card1 < card2 -> cardColumn1.pop()
                card1 > card2 -> cardColumn2.pop()
                else -> throw SameCardException(card1, card2)
            }
        }

        return result
    }


    fun addFourNewCards(fourCards: Array<Card>) {
        columns[0].push(fourCards[0])
        columns[1].push(fourCards[1])
        columns[2].push(fourCards[2])
        columns[3].push(fourCards[3])
    }

    override fun toString(): String {
        var result = ""

        var maxSize = columns[0].size
        for (i in 1..3) {
            maxSize = maxOf(maxSize, columns[i].size)
        }

        for (row in 0 until maxSize) {
            for (col in 0..3) {
                val card = columns[col].getOrNull(row)
                result += card?.toString() ?: "  "
                if (col < 3) {
                    result += ' '
                }
            }

            if (row < maxSize - 1) {
                result += '\n'
            }
        }

        return result
    }

    fun peekTopRow(): Array<Card?> {
        return arrayOf(columns[0].peek(), columns[1].peek(), columns[2].peek(), columns[3].peek())
    }

    fun play(printSteps: Boolean = false): Int {

        val deck = Deck()
        deck.shuffle()

        while (deck.size() > 0) {

            addFourNewCards(deck.getFourCards())

            do {
                removeAllLowerInSuites(printSteps)

            } while (moveOneCardToEmptySpace())

        }

        return cardsLeft()
    }

    fun removeAllLowerInSuites(printSteps: Boolean = false) {
        while (!allDifferentSuits(peekTopRow())) {
            for (col1 in 0..2) {
                for (col2 in col1 + 1..3) {
                    val result = removeLowestInSuit(columns[col1], columns[col2])
                    if (result != null && printSteps) {
                        println(this.toString() + '\n')
                    }
                }
            }
        }
    }

    fun moveOneCardToEmptySpace(): Boolean {
        // find one empty space
        var emptyCol = -1
        for (c in 0..3) {
            if (columns[c].isEmpty()) {
                emptyCol = c
            }
        }

        // find one column with more than 1 card in it
        var columnWithMoreThanOneCard = -1
        for (c in 0..3) {
            if (columns[c].size > 1) {
                columnWithMoreThanOneCard = c
            }
        }

        // move card
        if (emptyCol != -1 && columnWithMoreThanOneCard != -1) {
            val card = columns[columnWithMoreThanOneCard].pop()
            if (card != null) {
                columns[emptyCol].push(card)
                return true
            }
        }
        return false
    }

    fun cardsLeft(): Int {
        var count = 0
        for (col in 0..3) {
            count += columns[col].size
        }
        return count
    }
}
