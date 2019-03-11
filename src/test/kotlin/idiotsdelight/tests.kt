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
    fun initBoard() {
        board = Board()
    }


    @test
    fun testCone() {
        board.addCard(0, Card(Rank.Nine, Suits.Diamonds))
        board.addCard(1, Card(Rank.Eight, Suits.Diamonds))
        val clonedBoard = board.clone()
        assertEquals(2, board.cardsCount())
        assertEquals(2, clonedBoard.cardsCount())

        board.removeLowestInSuit(0, 1)
        assertEquals(1, board.cardsCount())
        assertEquals(2, clonedBoard.cardsCount())
    }

    @test
    fun testCardsLeft() {
        assertEquals(0, board.cardsCount())

        board.addCard(0, Card(Rank.Nine, Suits.Diamonds))
        assertEquals(1, board.cardsCount())

        board.addFourNewCards(
            arrayOf(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts),
                Card(Rank.Five, Suits.Hearts)
            )
        )
        assertEquals(5, board.cardsCount())
    }

    @test
    fun testRemoveLowestCardBothNull() {
        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(0, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardOneNull() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))

        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }


    @test
    fun testRemoveLowestCardTwoSpades() {
        board.addCard(0, Card(Rank.Two, Suits.Spades))
        board.addCard(1, Card(Rank.Three, Suits.Spades))

        assertEquals(Card(Rank.Two, Suits.Spades), board.removeLowestInSuit(0, 1))
        assertEquals(0, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardTwoSpadesReverseOrder() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))
        board.addCard(1, Card(Rank.Two, Suits.Spades))

        assertEquals(Card(Rank.Two, Suits.Spades), board.removeLowestInSuit(0, 1))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @test
    fun testRemoveLowestCardDifferentSuit() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))
        board.addCard(1, Card(Rank.Three, Suits.Diamonds))

        assertEquals(null, board.removeLowestInSuit(0, 1))

        assertEquals(1, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }


    @test(expected = SameCardException::class)
    fun testRemoveLowestCardSameCards() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Two, Suits.Hearts))

        board.removeLowestInSuit(0, 1)

    }

    @test
    fun testRemoveInOneSuitDoNothing() {
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts),
                Card(Rank.Five, Suits.Hearts)
            )
        )

        assertTrue(
            arrayOf<Card?>(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts),
                Card(Rank.Five, Suits.Hearts)
            ) contentEquals board.peekTopRow()
        )
    }

    @test
    fun testGetAllPossibleMovesOneCardMovable() {
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(0, Card(Rank.Five, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Diamonds))
        board.addCard(2, Card(Rank.Two, Suits.Clubs))

        assertTrue(board.hasEmptyColumn())
        assertTrue(arrayOf(0) contentEquals board.getColumnsWithMoreThanOneCard())

        val boardVariants: List<Board> = board.getBoardVariantsMovingCardToEmptySlot()

        assertEquals(1, boardVariants.size)
    }

    @test
    fun testGetAllPossibleMovesTwoCardsMovableToOneSlot() {
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(0, Card(Rank.Five, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Diamonds))
        board.addCard(1, Card(Rank.Two, Suits.Clubs))
        board.addCard(2, Card(Rank.Two, Suits.Clubs))

        assertTrue(board.hasEmptyColumn())
        assertTrue(arrayOf(0, 1) contentEquals board.getColumnsWithMoreThanOneCard())

        val boardVariants: List<Board> = board.getBoardVariantsMovingCardToEmptySlot()

        assertEquals(2, boardVariants.size)
    }

    @test
    fun testCardCount() {
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(1, Card(Rank.Four, Suits.Hearts))
        board.addCard(2, Card(Rank.Four, Suits.Clubs))
        board.addCard(3, Card(Rank.Four, Suits.Diamonds))

        assertEquals(4, board.cardsCount())
    }

    @test
    fun testAllDifferentSuitsAllEmptyColumns() {

        assertTrue(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsTwoHeartsAndTwoEmptyCols() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Hearts))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsFourHearts() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Hearts))
        board.addCard(2, Card(Rank.Four, Suits.Hearts))
        board.addCard(3, Card(Rank.Five, Suits.Hearts))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsOneHeartsOneSpadesTwoEmpty() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Spades))
        assertTrue(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsTwoSpadesTwoEmpty() {
        board.addCard(2, Card(Rank.Two, Suits.Spades))
        board.addCard(3, Card(Rank.Three, Suits.Spades))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsTwoClubsTwoEmpty() {
        board.addCard(2, Card(Rank.Two, Suits.Clubs))
        board.addCard(3, Card(Rank.Three, Suits.Clubs))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @test
    fun testAllTopCardsInDifferentSuitsDiamondsClubsTwoEmpty() {
        board.addCard(1, Card(Rank.Two, Suits.Diamonds))
        board.addCard(2, Card(Rank.Three, Suits.Diamonds))
        assertFalse(board.allTopCardsInDifferentSuits())
    }
}

class TestIdiotsDelight {


    @test
    fun restRemoveInOneSuitAllDifferentSuits() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Two, Suits.Spades),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf<Card?>(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Two, Suits.Spades),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsLowestFirst() {

        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                null,
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHartsBiggestFirst() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                null,
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Diamonds)
            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsInTheMiddle() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Two, Suits.Diamonds),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Two, Suits.Diamonds),
                Card(Rank.Three, Suits.Hearts),
                null,
                Card(Rank.Two, Suits.Clubs)

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsColumn1and3() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Three, Suits.Diamonds),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Three, Suits.Diamonds),
                null,
                Card(Rank.Two, Suits.Clubs)

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsCoumn1and4() {

        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Three, Suits.Diamonds),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Hearts)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Three, Suits.Diamonds),
                Card(Rank.Two, Suits.Clubs),
                null

            ) contentEquals board.peekTopRow()
        )
    }

    @test
    fun restRemoveInOneSuitThreeHearts() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Four, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Clubs),
                Card(Rank.Two, Suits.Hearts)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Four, Suits.Hearts),
                null,
                Card(Rank.Two, Suits.Clubs),
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
                Card(Rank.Queen, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts)
            )
        )

        board.addFourNewCards(
            arrayOf(
                Card(Rank.Nine, Suits.Hearts),
                Card(Rank.Ten, Suits.Hearts),
                Card(Rank.Five, Suits.Hearts),
                Card(Rank.Six, Suits.Hearts)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Queen, Suits.Hearts),
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
                Card(Rank.Ace, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        assertTrue(
            arrayOf(
                Card(Rank.Ace, Suits.Hearts),
                null,
                null,
                null

            ) contentEquals board.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun moveCardIntoEmptyColumn() {
        val board = Board()
        board.addFourNewCards(
            arrayOf(
                Card(Rank.Ace, Suits.Hearts),
                Card(Rank.Two, Suits.Hearts),
                Card(Rank.Three, Suits.Hearts),
                Card(Rank.Four, Suits.Hearts)
            )
        )

        removeAllLowerCardsInAllSuites(board)

        board.addFourNewCards(
            arrayOf(
                Card(Rank.Nine, Suits.Hearts),
                Card(Rank.Eight, Suits.Hearts),
                Card(Rank.Seven, Suits.Hearts),
                Card(Rank.Six, Suits.Hearts)
            )
        )
        removeAllLowerCardsInAllSuites(board)
        val boardResult = moveCardToEmptySlot(board)

        assertTrue(
            arrayOf(
                Card(Rank.Ace, Suits.Hearts),
                null,
                null,
                Card(Rank.Nine, Suits.Hearts)
            ) contentEquals boardResult.peekTopRow(),
            message = Arrays.toString(board.peekTopRow())
        )
    }

    @test
    fun testMultiplePossibleBoards() {
        val board = Board()
        board.addCard(0, Card(Rank.Five, Suits.Clubs))
        board.addCard(0, Card(Rank.Four, Suits.Clubs))
        board.addCard(1, Card(Rank.Four, Suits.Spades))
        board.addCard(1, Card(Rank.Five, Suits.Diamonds))
        board.addCard(2, Card(Rank.Five, Suits.Hearts))

        val result = moveCardToEmptySlot(board)

        assertEquals(4, result.cardsCount())

    }

    @test
    fun testMultiplePossibleBoards2() {
        val board = Board()
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(0, Card(Rank.Five, Suits.Diamonds))
        board.addCard(1, Card(Rank.Five, Suits.Clubs))
        board.addCard(1, Card(Rank.Four, Suits.Clubs))
        board.addCard(2, Card(Rank.Five, Suits.Hearts))

        val result = moveCardToEmptySlot(board)

        assertEquals(4, result.cardsCount())

    }

}
