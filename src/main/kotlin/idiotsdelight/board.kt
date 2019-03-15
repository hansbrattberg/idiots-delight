package idiotsdelight

import cards.*
import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor


class SameCardException(private val card1: Card, private val card2: Card) : Throwable() {

    override fun toString(): String {
        return card1.toString() + card2.toString()
    }
}

class NoEmptyColumnSlotException(private val board: Board) : Throwable() {
    override fun toString(): String {
        return board.toString()
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

    fun cardsCount(): Int {
        return columns[0].size + columns[1].size + columns[2].size + columns[3].size
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

    private fun maxColumnSize(): Int {
        var maxSize = columns[0].size
        for (i in 1..3) {
            maxSize = maxOf(maxSize, columns[i].size)
        }
        return maxSize
    }


    fun peekTopCardsInEachColumn(): Array<Card?> {
        return arrayOf(columns[0].peek(), columns[1].peek(), columns[2].peek(), columns[3].peek())
    }


    fun hasEmptyColumn(): Boolean {
        var result = false
        for (c in 0..3) {
            if (columns[c].isEmpty()) {
                result = true
            }
        }
        return result
    }


    private fun getFirstEmptyColumn(): Int {
        var emptyCol = -1
        for (c in 0..3) {
            if (columns[c].isEmpty()) {
                emptyCol = c
            }
        }
        return emptyCol
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

    fun getBoardVariantsMovingCardToEmptySlot(): List<Board> {
        if (!this.hasEmptyColumn()) {
            throw NoEmptyColumnSlotException(this)
        }

        val result = mutableListOf<Board>()

        val columnsWithMoreThanOneCard = getColumnsWithMoreThanOneCard()
        for (col in columnsWithMoreThanOneCard) {
            val clone = this.clone()
            val emptyCol = clone.getFirstEmptyColumn()
            val cardToMove = clone.columns[col].pop()
            if (cardToMove != null) {
                clone.columns[emptyCol].push(cardToMove)
            }
            result.add(clone)
        }

        return result
    }

    fun getColumnsWithMoreThanOneCard(): Array<Int> {
        val result: MutableList<Int> = mutableListOf()
        for (col in 0..3) {
            if (columns[col].size > 1) {
                result.add(col)
            }
        }
        return result.toTypedArray()
    }

    private fun hasColumnWithMoreThanOneCard(): Boolean {
        for (col in 0..3) {
            if (columns[col].size > 1) {
                return true
            }
        }
        return false
    }

    fun canMoveCardtoEmptySlot() = hasEmptyColumn() && hasColumnWithMoreThanOneCard()

    fun allTopCardsInDifferentSuits(): Boolean {
        var heartCount = 0
        var spadesCount = 0
        var clubsCount = 0
        var diamondsCount = 0
        for (col in 0..3) {
            val card = columns[col].peek()
            if (card != null && card.suit == Suits.Hearts) {
                heartCount++
            }
            if (card != null && card.suit == Suits.Spades) {
                spadesCount++
            }
            if (card != null && card.suit == Suits.Clubs) {
                clubsCount++
            }
            if (card != null && card.suit == Suits.Diamonds) {
                diamondsCount++
            }
        }
        return heartCount <= 1 && spadesCount <= 1 && clubsCount <= 1 && diamondsCount <= 1
    }

    override fun toString(): String {
        var result = ""

        val maxSize = maxColumnSize()

        for (row in 0 until maxSize) {
            for (col in 0..3) {
                val card = columns[col].getOrNull(row)

                result += card?.toString() ?: if (row == 0) "──" else "  "
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

    fun toColoredString(): String {
        var result = ""

        val maxSize = maxColumnSize()

        for (row in 0 until maxSize) {
            for (col in 0..3) {
                val card = columns[col].getOrNull(row)

                result += card?.toColoredString() ?: if (row == 0) Kolor.foreground("──", Color.GREEN) else "  "
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
}


class IdiotsDelight(
    private var board: Board,
    val interactiveMode: Boolean,
    private val printSteps: Boolean = false
) {

    override fun toString(): String {
        return board.toString()
    }

    private fun addFourNewCards(fourCards: Array<Card>) {
        board.addFourNewCards(fourCards)
        printSteps(board, printSteps, interactiveMode)
    }


    fun play(): Int {

        val deck = Deck()
        deck.shuffle()

        while (deck.size() > 0) {
            addFourNewCards(deck.getFourCards())
            board = removeAllPossibleCards(board, printSteps, interactiveMode)
        }

        return board.cardsCount()
    }
}

private fun printSteps(board: Board, printSteps: Boolean, interactiveMode: Boolean) {
    if (printSteps) {
        println(board.toColoredString())
        println()
    }
    if (interactiveMode) {
        readLine()
    }
}

fun removeAllLowerCardsInAllSuites(
    board: Board,
    printSteps: Boolean = false,
    interactiveMode: Boolean = false
) {
    while (canRemoveCardsInSameSuite(board)) {
        for (col1 in 0..2) {
            for (col2 in col1 + 1..3) {
                val result = board.removeLowestInSuit(col1, col2)
                if (result != null) {
                    printSteps(board, printSteps, interactiveMode)
                }

            }
        }
    }
}

private fun canRemoveCardsInSameSuite(result: Board) = !result.allTopCardsInDifferentSuits()

fun moveCardToEmptySlot(board: Board, printSteps: Boolean = false, interactiveMode: Boolean = false): Board {

    if (!board.canMoveCardtoEmptySlot()) {
        return board
    }

    val boardVariants = board.getBoardVariantsMovingCardToEmptySlot()
    var bestBoardMoveIndex = 0  // first board, if only one, that is the one!

    val results = mutableListOf<Board>()
    boardVariants.forEach { b ->
        val element = removeAllPossibleCards(b.clone(), false, false)
        results.add(element)
    }

    var lowestNumberOfCards = Integer.MAX_VALUE
    for (boardCount in 0 until results.size) {
        if (results[boardCount].cardsCount() < lowestNumberOfCards) {
            bestBoardMoveIndex = boardCount
            lowestNumberOfCards = results[boardCount].cardsCount()
        }
    }

    printSteps(boardVariants[bestBoardMoveIndex], printSteps, interactiveMode)
    return boardVariants[bestBoardMoveIndex]
}

private fun removeAllPossibleCards(
    board: Board,
    printSteps: Boolean,
    interactiveMode: Boolean
): Board {
    var b = board
    while (b.canMoveCardtoEmptySlot() || canRemoveCardsInSameSuite(b)) {
        removeAllLowerCardsInAllSuites(b, printSteps, interactiveMode)
        b = moveCardToEmptySlot(b, printSteps, interactiveMode)
    }
    return b
}