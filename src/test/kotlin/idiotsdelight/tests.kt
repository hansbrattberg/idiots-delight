package idiotsdelight

import cards.Card
import cards.Rank
import cards.Suits

import java.util.*
import kotlin.test.*

import org.junit.Test as test

class BoardTests {

    lateinit var board: Board

    @BeforeTest
    fun initBoard(){
        board = Board()
    }


    @test
    fun testCone(){
        board.addCard(0, Card(Suits.Diamonds, Rank.Nine))
        board.addCard(1, Card(Suits.Diamonds, Rank.Eight))
        val clonedBoard = board.clone()
        assertEquals(2, board.cardsLeft())
        assertEquals(2, clonedBoard.cardsLeft())

        board.removeLowestInSuit(0, 1)
        assertEquals(1, board.cardsLeft())
        assertEquals(2, clonedBoard.cardsLeft())
    }

    @test
    fun testCardsLeft() {
        assertEquals(0, board.cardsLeft())

        board.addCard(0, Card(Suits.Diamonds,Rank.Nine))
        assertEquals(1, board.cardsLeft())

        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Five)
            )
        )
        assertEquals(5, board.cardsLeft())
    }

    @test
    fun testRemoveLowestCardBothNull() {
        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(0, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardOneNull() {
        board.addCard(0, Card(Suits.Spades, Rank.Three))

        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }


    @test
    fun testRemoveLowestCardTwoSpades() {
        board.addCard(0, Card(Suits.Spades, Rank.Two))
        board.addCard(1, Card(Suits.Spades, Rank.Three))

        assertEquals(Card(Suits.Spades, Rank.Two), board.removeLowestInSuit(0, 1))
        assertEquals(0, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardTwoSpadesReverseOrder() {
        board.addCard(0, Card(Suits.Spades, Rank.Three))
        board.addCard(1, Card(Suits.Spades, Rank.Two))

        assertEquals(Card(Suits.Spades, Rank.Two), board.removeLowestInSuit(0, 1))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardDifferentSuit() {
        board.addCard(0, Card(Suits.Spades, Rank.Three))
        board.addCard(1, Card(Suits.Diamonds, Rank.Three))

        assertEquals(null, board.removeLowestInSuit(0, 1))

        assertEquals(1, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }


    @test(expected = SameCardException::class)
    fun testRemoveLowestCardSameCards() {
        board.addCard(0, Card(Suits.Hearts, Rank.Two))
        board.addCard(1, Card(Suits.Hearts, Rank.Two))

        board.removeLowestInSuit(0, 1)

    }

    @test
    fun testRemoveInOneSuitDoNothing() {
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Five)
            )
        )

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Five)
            ) contentEquals board.peekTopRow()
        )
    }
}

class TestIdiotsDelight {


    @test
    fun testAllDifferentSuits() {
        val table = IdiotsDelight()

        assertTrue(table.allDifferentSuits(arrayOf(null, null, null, null)))

        assertFalse(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Hearts, Rank.Two),
                    Card(Suits.Hearts, Rank.Three),
                    null,
                    null
                )
            )
        )

        assertFalse(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Hearts, Rank.Two),
                    Card(Suits.Hearts, Rank.Three),
                    Card(Suits.Hearts, Rank.Four),
                    Card(Suits.Hearts, Rank.Five)
                )
            )
        )

        assertTrue(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Hearts, Rank.Two),
                    Card(Suits.Spades, Rank.Three),
                    null,
                    null
                )
            )
        )


        assertFalse(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Spades, Rank.Two),
                    Card(Suits.Spades, Rank.Three),
                    null,
                    null
                )
            )
        )


        assertFalse(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Clubs, Rank.Two),
                    Card(Suits.Clubs, Rank.Three),
                    null,
                    null
                )
            )
        )


        assertFalse(
            table.allDifferentSuits(
                arrayOf(
                    Card(Suits.Diamonds, Rank.Two),
                    Card(Suits.Diamonds, Rank.Three),
                    null,
                    null
                )
            )
        )
    }


    @test
    fun restRemoveInOneSuitAllDifferentSuits() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Spades, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Spades, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsLowestFirst() {

        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                null,
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHartsBiggestFirst() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsInTheMiddle() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsColumn1and3() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsCoumn1and4() {

        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals board.peekTopRow()
        )
    }

    @test
    fun restRemoveInOneSuitThreeHearts() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )
        val table = IdiotsDelight(board)
        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Four),
                null,
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitAllHearts() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Queen),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Four)
            )
        )
        val table = IdiotsDelight(board)

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Nine),
                Card(Suits.Hearts, Rank.Ten),
                Card(Suits.Hearts, Rank.Five),
                Card(Suits.Hearts, Rank.Six)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Queen),
                null,
                null,
                null

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitAllHeartsAceBiggest() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Four)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                null,
                null,
                null

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun moveCardIntoEmptyColum() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Four)
            )
        )
        val table = IdiotsDelight(board)

        table.removeAllLowerInSuites()
        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Nine),
                Card(Suits.Hearts, Rank.Eight),
                Card(Suits.Hearts, Rank.Seven),
                Card(Suits.Hearts, Rank.Six)
            )
        )
        table.removeAllLowerInSuites()

        board.moveOneCardToEmptySpace()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                null,
                null,
                Card(Suits.Hearts, Rank.Nine)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

}
