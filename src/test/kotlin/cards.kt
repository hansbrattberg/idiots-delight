import idioten.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCardStack {

    @Test
    fun testAllMethods() {
        val stackOfCards: MutableList<Card> = mutableListOf()
        val card = Card(Suits.Spades, Rank.Two)

        assertTrue(stackOfCards.isEmpty())
        stackOfCards.push(card)

        assertTrue(!stackOfCards.isEmpty())
        assertEquals(1, stackOfCards.size)
        assertEquals(Card(Suits.Spades, Rank.Two), stackOfCards.peek())

        val topElement = stackOfCards.pop()
        assertTrue(stackOfCards.isEmpty())
        assertEquals(Card(Suits.Spades, Rank.Two), topElement)
    }
}

class TestDeckOfCards(){

    @Test
    fun rankToString() {
        assertEquals("2", Rank.Two.toString())
        assertEquals("5", Rank.Five.toString())
        assertEquals("X", Rank.Ten.toString())
        assertEquals("A", Rank.Ace.toString())
        assertEquals("J", Rank.Jack.toString())
        assertEquals("K", Rank.King.toString())
    }


    @Test
    fun testCardCompare() {
        assertTrue(Card(Suits.Spades, Rank.Two) < Card(Suits.Spades, Rank.Three))
        assertEquals(0, Card(Suits.Spades, Rank.Two).compareTo(Card(Suits.Spades, Rank.Two)))
        assertTrue(Card(Suits.Spades, Rank.Three) > Card(Suits.Spades, Rank.Two))
    }

    @Test
    fun check52cards() {
        val deck = Deck()
        assertEquals(52, deck.size(), "Wrong # of cards!")
    }

    @Test
    fun get4Cards() {
        val deck = Deck()
        val cards = deck.getFourCards()
        assertEquals(4, cards.size)
        assertEquals(48, deck.size())
    }

}