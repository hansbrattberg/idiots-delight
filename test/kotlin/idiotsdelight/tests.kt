package idiotsdelight

import java.util.*

import cards.Card
import cards.Rank
import cards.Suits

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class BoardTests {

    class N00StartKtTest {
        @Test
        fun testOk() {
            assertTrue(true)
        }
    }

    lateinit var board: Board

    @BeforeEach
    fun initBoard() {
        board = Board()
    }

    @Test
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

    @Test
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

    @Test
    fun testRemoveLowestCardBothNull() {
        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(0, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @Test
    fun testRemoveLowestCardOneNull() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))

        assertEquals(null, board.removeLowestInSuit(1, 2))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }


    @Test
    fun testRemoveLowestCardTwoSpades() {
        board.addCard(0, Card(Rank.Two, Suits.Spades))
        board.addCard(1, Card(Rank.Three, Suits.Spades))

        assertEquals(Card(Rank.Two, Suits.Spades), board.removeLowestInSuit(0, 1))
        assertEquals(0, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }

    @Test
    fun testRemoveLowestCardTwoSpadesReverseOrder() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))
        board.addCard(1, Card(Rank.Two, Suits.Spades))

        assertEquals(Card(Rank.Two, Suits.Spades), board.removeLowestInSuit(0, 1))
        assertEquals(1, board.cardsCount(0))
        assertEquals(0, board.cardsCount(1))
    }

    @Test
    fun testRemoveLowestCardDifferentSuit() {
        board.addCard(0, Card(Rank.Three, Suits.Spades))
        board.addCard(1, Card(Rank.Three, Suits.Diamonds))

        assertEquals(null, board.removeLowestInSuit(0, 1))

        assertEquals(1, board.cardsCount(0))
        assertEquals(1, board.cardsCount(1))
    }


    @Test
    fun testRemoveLowestCardSameCards() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Two, Suits.Hearts))

        Assertions.assertThrows(SameCardException::class.java, {
            board.removeLowestInSuit(0, 1)
        })
    }

    @Test
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
            ) contentEquals board.peekTopCardsInEachColumn()
        )
    }

    @Test
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

    @Test
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

    @Test
    fun testCardCount() {
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(1, Card(Rank.Four, Suits.Hearts))
        board.addCard(2, Card(Rank.Four, Suits.Clubs))
        board.addCard(3, Card(Rank.Four, Suits.Diamonds))

        assertEquals(4, board.cardsCount())
    }

    @Test
    fun testAllDifferentSuitsAllEmptyColumns() {

        assertTrue(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsTwoHeartsAndTwoEmptyCols() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Hearts))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsFourHearts() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Hearts))
        board.addCard(2, Card(Rank.Four, Suits.Hearts))
        board.addCard(3, Card(Rank.Five, Suits.Hearts))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsOneHeartsOneSpadesTwoEmpty() {
        board.addCard(0, Card(Rank.Two, Suits.Hearts))
        board.addCard(1, Card(Rank.Three, Suits.Spades))
        assertTrue(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsTwoSpadesTwoEmpty() {
        board.addCard(2, Card(Rank.Two, Suits.Spades))
        board.addCard(3, Card(Rank.Three, Suits.Spades))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsTwoClubsTwoEmpty() {
        board.addCard(2, Card(Rank.Two, Suits.Clubs))
        board.addCard(3, Card(Rank.Three, Suits.Clubs))
        assertFalse(board.allTopCardsInDifferentSuits())
    }

    @Test
    fun testAllTopCardsInDifferentSuitsDiamondsClubsTwoEmpty() {
        board.addCard(1, Card(Rank.Two, Suits.Diamonds))
        board.addCard(2, Card(Rank.Three, Suits.Diamonds))
        assertFalse(board.allTopCardsInDifferentSuits())
    }
}

class TestIdiotsDelight {


    @Test
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
            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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
            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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
            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn()
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

    @Test
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

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }


    @Test
    fun testMultiplePossibleBoards() {
        val board = Board()
        board.addCard(0, Card(Rank.Five, Suits.Clubs))
        board.addCard(0, Card(Rank.Four, Suits.Clubs))
        board.addCard(1, Card(Rank.Four, Suits.Spades))
        board.addCard(1, Card(Rank.Five, Suits.Diamonds))
        board.addCard(2, Card(Rank.Five, Suits.Hearts))

        println(board)
        println()
        val result = moveCardToEmptySlot(board)
        println(result)

        assertEquals(4, result.cardsCount())

    }

    @Test
    fun testMultiplePossibleBoards2() {
        val board = Board()
        board.addCard(0, Card(Rank.Four, Suits.Spades))
        board.addCard(0, Card(Rank.Five, Suits.Diamonds))
        board.addCard(1, Card(Rank.Five, Suits.Clubs))
        board.addCard(1, Card(Rank.Four, Suits.Clubs))
        board.addCard(2, Card(Rank.Five, Suits.Hearts))

        println(board)
        println()
        val result = moveCardToEmptySlot(board)
        println(result)

        assertEquals(4, result.cardsCount())

    }

    @Test
    fun testGetBoardVariants(){
        val board = Board()
        board.addCard(1, Card(Rank.King, Suits.Hearts))
        board.addCard(1, Card(Rank.Six, Suits.Clubs))
        board.addCard(2, Card(Rank.King, Suits.Spades))
        board.addCard(2, Card(Rank.Two, Suits.Diamonds))
        board.addCard(3, Card(Rank.Six, Suits.Diamonds))
        board.addCard(3, Card(Rank.Four, Suits.Hearts))

        println(board)
        println()

        val variants = board.getBoardVariantsMovingCardToEmptySlot()

        assertEquals(3, variants.size)
        println( variants[0] )
        println()

        println( variants[1] )
        println()

        println( variants[2] )
        println()

        assertTrue(
            arrayOf(
                Card(Rank.Ace, Suits.Hearts),
                null,
                null,
                null

            ) contentEquals board.peekTopCardsInEachColumn(),
            Arrays.toString(board.peekTopCardsInEachColumn())
        )
    }

}
