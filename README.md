Idiots delight
======================

This is a simulation program to figure out how often the card solitary game called Idiots delight ends with a possitive outcome.

This is the rules of the game (taken from https://en.wikipedia.org/wiki/Aces_Up)

Deal four cards in a row face up.
* If there are two or more cards of the same suit, discard all but the highest-ranked card of that suit. Aces rank high.
* Repeat step 2 until there are no more pairs of cards with the same suit.
* Whenever there are any empty spaces, you may choose the top card of another pile to be put into the empty space. After you do this, go to Step 2.
* When there are no more cards to move or remove, deal out the next four cards from the deck face-up onto each pile.
* Repeat Step 2, using only the visible, or top, cards on each of the four piles.
* When the last four cards have been dealt out and any moves made, the game is over. The fewer cards left in the tableau, the better. To win is to have only the four aces left.
* When the game ends, the number of discarded cards is your score. The maximum score (and thus the score necessary to win) is 48, which means all cards have been discarded except for the four aces, thus the name of the game.

Run one game
----
 ./gradlew run --console=plain

Programming language
-----
It's programmed in Kotlin 1.3.


