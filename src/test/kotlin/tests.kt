package idioten

import kotlin.test.assertEquals
import org.junit.Test as test

class TestDeck() {
    @test fun check52cards() {
        val deck = Deck()
        assertEquals(52, deck.size(), "Wrong # of cards!")
    }

    @test fun get4Cards() {
        val deck = Deck()
        val cards = deck.getFourCards()
        assertEquals( 4, cards.size)
        assertEquals(48, deck.size(), "Wrong # of cards!")
    }
}
