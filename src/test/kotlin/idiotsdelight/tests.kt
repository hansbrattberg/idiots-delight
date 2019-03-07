package idiotsdelight

import cards.Card
import cards.Rank
import cards.Suits

import java.util.*
import kotlin.test.*

import org.junit.Test as test

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
    fun testRemoveLowestCardBothNull() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf()
        val cardColumn2: MutableList<Card> = mutableListOf()

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(0, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardOneNull() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf()

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardTwoSpades() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Two))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))

        assertEquals(Card(Suits.Spades, Rank.Two), table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(0, cardColumn1.size)
        assertEquals(1, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardTwoSpadesReverseOrder() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Two))

        assertEquals(Card(Suits.Spades, Rank.Two), table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardDifferentSuit() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(1, cardColumn2.size)
    }


    @test(expected = SameCardException::class)
    fun testRemoveLowestCardSameCards() {
        val table = IdiotsDelight()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))

        table.removeLowestInSuit(cardColumn1, cardColumn2)

    }

    @test
    fun restRemoveInOneSuitDoNothing() {
        val table = IdiotsDelight()

        table.addFourNewCards(
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
            ) contentEquals table.peekTopRow()
        )
    }

    @test
    fun restRemoveInOneSuitAllDifferentSuits() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Spades, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Spades, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsLowestFirst() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                null,
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHartsBiggestFirst() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsInTheMiddle() {
        val table = IdiotsDelight()
        table.addFourNewCards(
            arrayOf(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsColumn1and3() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitTwoHeartsCoumn1and4() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals table.peekTopRow()
        )
    }

    @test
    fun restRemoveInOneSuitThreeHearts() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Four),
                null,
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitAllHearts() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Queen),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Four)
            )
        )

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

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuitAllHeartsAceBiggest() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Four)
            )
        )

        table.removeAllLowerInSuites()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                null,
                null,
                null

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun moveCardIntoEmptyColum() {
        val table = IdiotsDelight()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Four)
            )
        )
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

        table.moveOneCardToEmptySpace()

        assertTrue(
            arrayOf(
                Card(Suits.Hearts, Rank.Ace),
                null,
                null,
                Card(Suits.Hearts, Rank.Nine)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }
}
