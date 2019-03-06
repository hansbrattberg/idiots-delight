package idioten

import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class TestDeck() {

    @test
    fun lowestInSuite() {
        val cards: Array<Card> = arrayOf(Card(Suits.Clubs, Rank.Two), Card(Suits.Clubs, Rank.Three))

        assertEquals(Card(Suits.Clubs, Rank.Two), Table().lowestCard(cards, Suits.Clubs))
        assertNull(Table().lowestCard(cards, Suits.Spades))
    }


    @test
    fun testRemoveLowestCardBothNull() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf()
        val cardColumn2: MutableList<Card> = mutableListOf()

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(0, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardOneNull() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf()

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardTwoSpades() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Two))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))

        assertEquals(Card(Suits.Spades, Rank.Two), table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(0, cardColumn1.size)
        assertEquals(1, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardTwoSpadesReverseOrder() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Two))

        assertEquals(Card(Suits.Spades, Rank.Two), table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(0, cardColumn2.size)
    }

    @test
    fun testRemoveLowestCardDifferentSuit() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Spades, Rank.Three))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))

        assertEquals(null, table.removeLowestInSuit(cardColumn1, cardColumn2))
        assertEquals(1, cardColumn1.size)
        assertEquals(1, cardColumn2.size)
    }


    @test(expected = SameCardException::class)
    fun testRemoveLowestCardSameCards() {
        val table = Table()

        val cardColumn1: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))
        val cardColumn2: MutableList<Card> = mutableListOf(Card(Suits.Hearts, Rank.Two))

        table.removeLowestInSuit(cardColumn1, cardColumn2)

    }

    @test
    fun restRemoveInOneSuit() {
        val table = Table()

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
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit2() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Spades, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.play()

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
    fun restRemoveInOneSuit3() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                null,
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit4() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Diamonds, Rank.Two)
            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit5() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Diamonds, Rank.Two),
                Card(Suits.Hearts, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit6() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Hearts, Rank.Two),
                Card(Suits.Clubs, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                null,
                Card(Suits.Clubs, Rank.Two)

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit7() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Diamonds, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }

    @test
    fun restRemoveInOneSuit8() {
        val table = Table()

        table.addFourNewCards(
            arrayOf(
                Card(Suits.Hearts, Rank.Four),
                Card(Suits.Hearts, Rank.Three),
                Card(Suits.Clubs, Rank.Two),
                Card(Suits.Hearts, Rank.Two)
            )
        )

        table.play()

        assertTrue(
            arrayOf<Card?>(
                Card(Suits.Hearts, Rank.Four),
                null,
                Card(Suits.Clubs, Rank.Two),
                null

            ) contentEquals table.peekTopRow(),
            message = Arrays.toString(table.peekTopRow())
        )
    }


    // while not allDifferentSuits
    // loop over Suits
    // Compare column 1 with 2, 3, 4 and remove lowestInSuit
    // Compare column 2 with 3, 4
    // Compare column 3 with 4

    @test
    fun testAllDifferentSuits() {
        val table = Table()

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

}
