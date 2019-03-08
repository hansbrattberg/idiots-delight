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

    var countSuccess = 0
    val noOfTimes = 100000
    val buckets = hashMapOf<Int, Int>()
    for (key in 4..52) {
        buckets[key] = 0
    }

    for (playCount in 1..noOfTimes) {
        val table = IdiotsDelight()
        val cardsLeft = table.play(false)

        var count = buckets[cardsLeft]
        if (count != null) {
            count++
        } else {
            count = 1
        }
        buckets[cardsLeft] = count

        if (cardsLeft == 4) {
            countSuccess++
        }

    }
    println((countSuccess / noOfTimes.toDouble() * 100).toString() + "%")

    val bucketsPercent = hashMapOf<Int, Double>()
    for (key in 4..52) {
        val count = buckets[key]
        if (count != null) {
            bucketsPercent[key] = Math.round(count / noOfTimes.toDouble() * 1000) / 10.toDouble()
        }

    }
    println(bucketsPercent.values)
}


class SameCardException(private val card1: Card, private val card2: Card) : Throwable() {

    override fun toString(): String {
        return card1.toString() + card2.toString()
    }
}

class Board {
    private val columns: Array<MutableList<Card>> =
        arrayOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

    fun clone(): Board {
        val result = Board()
        for (col in 0..3) {
            this.columns[col].forEach {
                result.columns[col].add(it.copy())
            }
        }
        return result
    }

    fun cardsCount(column: Int): Int {
        return columns[column].size
    }

    fun addCard(column: Int, card: Card) {
        columns[column].add(card)
    }

    fun addFourNewCards(fourCards: Array<Card>) {
        columns[0].push(fourCards[0])
        columns[1].push(fourCards[1])
        columns[2].push(fourCards[2])
        columns[3].push(fourCards[3])
    }

    fun cardsLeft(): Int {
        var count = 0
        for (col in 0..3) {
            count += columns[col].size
        }
        return count
    }

    private fun maxColumnSize(): Int {
        var maxSize = columns[0].size
        for (i in 1..3) {
            maxSize = maxOf(maxSize, columns[i].size)
        }
        return maxSize
    }


    fun peekTopRow(): Array<Card?> {
        return arrayOf(columns[0].peek(), columns[1].peek(), columns[2].peek(), columns[3].peek())
    }

    override fun toString(): String {
        var result = ""

        val maxSize = maxColumnSize()

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

        // move one card
        if (emptyCol != -1 && columnWithMoreThanOneCard != -1) {
            val card = columns[columnWithMoreThanOneCard].pop()
            if (card != null) {
                columns[emptyCol].push(card)
                return true
            }
        }
        return false
    }

    fun removeLowestInSuit(oneColumn: Int, otherColumn: Int): Card? {
        val oneCard: Card? = columns[oneColumn].peek()
        val otherCard: Card? = columns[otherColumn].peek()

        var result: Card? = null

        if (oneCard != null && otherCard != null && oneCard.suit == otherCard.suit) {
            result = when {
                oneCard < otherCard -> columns[oneColumn].pop()
                oneCard > otherCard -> columns[otherColumn].pop()
                else -> throw SameCardException(oneCard, otherCard)
            }
        }

        return result
    }
}

class IdiotsDelight(board: Board) {

    private val board = board

    constructor () : this(Board())

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


    fun addFourNewCards(fourCards: Array<Card>) {
        board.addFourNewCards(fourCards)
    }

    override fun toString(): String {
        return board.toString()
    }


    fun play(printSteps: Boolean = false): Int {

        val deck = Deck()
        deck.shuffle()

        while (deck.size() > 0) {
            addFourNewCards(deck.getFourCards())
            if (printSteps) println(deck)
            do {
                removeAllLowerInSuites(printSteps)
                if (printSteps) println(deck)
            } while (board.moveOneCardToEmptySpace())
        }

        return board.cardsLeft()
    }

    fun removeAllLowerInSuites(printSteps: Boolean = false) {
        while (!allDifferentSuits(board.peekTopRow())) {
            for (col1 in 0..2) {
                for (col2 in col1 + 1..3) {
                    val result = board.removeLowestInSuit(col1, col2)
                    if (result != null && printSteps) {
                        println(this.toString() + '\n')
                    }
                }
            }
        }
    }


}
