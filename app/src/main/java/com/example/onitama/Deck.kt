package com.example.onitama

import java.util.*

class Deck {
    companion object {
        private var allCards: Array<Card?> = arrayOf()

        fun init() {
            allCards = Array(Card.getCardsSize()) {
                null
            }
            shuffle()
        }

        fun draw(): Array<Card> {
            return Arrays.copyOfRange(allCards, 0, 5)
        }

        fun shuffle() {
            Card.allCards.shuffle()
            for (i in 0..4) {
                allCards[i] = Card.allCards[i]
            }
        }
    }
}
