package com.example.onitama

class Card {
    private var moves: Array<IntArray> = arrayOf(intArrayOf())
    private var name: String = ""
    private var color: Int = 0
    private var drawable: Int = 0

    companion object {
        var allCards = ArrayList<Card>()

        fun init() {
            val tiger    = Card(arrayOf(intArrayOf( 0,  2), intArrayOf( 0, -1)                                        ), "Tiger"   , Piece.COLOR_BLUE, R.drawable.card_tiger)
            val dragon   = Card(arrayOf(intArrayOf(-2,  1), intArrayOf(-1, -1), intArrayOf( 1, -1), intArrayOf( 2,  1)), "Dragon"  , Piece.COLOR_RED , R.drawable.card_dragon)
            val frog     = Card(arrayOf(intArrayOf(-2,  0), intArrayOf(-1,  1), intArrayOf( 1, -1)                    ), "Frog"    , Piece.COLOR_RED , R.drawable.card_frog)
            val rabbit   = Card(arrayOf(intArrayOf( 1,  1), intArrayOf(-1, -1), intArrayOf( 2,  0)                    ), "Rabbit"  , Piece.COLOR_BLUE, R.drawable.card_rabbit)
            val crab     = Card(arrayOf(intArrayOf( 2,  0), intArrayOf(-2,  0), intArrayOf( 0,  1)                    ), "Crab"    , Piece.COLOR_BLUE, R.drawable.card_crab)
            val elephant = Card(arrayOf(intArrayOf( 1,  0), intArrayOf(-1,  0), intArrayOf( 1,  1), intArrayOf(-1,  1)), "Elephant", Piece.COLOR_RED , R.drawable.card_elephant)
            val goose    = Card(arrayOf(intArrayOf(-1,  1), intArrayOf( 1, -1), intArrayOf(-1,  0), intArrayOf( 1,  0)), "Goose"   , Piece.COLOR_BLUE, R.drawable.card_goose)
            val rooster  = Card(arrayOf(intArrayOf(-1, -1), intArrayOf( 1,  1), intArrayOf(-1,  0), intArrayOf( 1,  0)), "Rooster" , Piece.COLOR_RED , R.drawable.card_rooster)
            val monkey   = Card(arrayOf(intArrayOf( 1,  1), intArrayOf(-1, -1), intArrayOf( 1, -1), intArrayOf(-1,  1)), "Monkey"  , Piece.COLOR_BLUE, R.drawable.card_monkey)
            val mantis   = Card(arrayOf(intArrayOf( 0, -1), intArrayOf(-1,  1), intArrayOf( 1,  1)                    ), "Mantis"  , Piece.COLOR_RED , R.drawable.card_mantis)
            val horse    = Card(arrayOf(intArrayOf(-1,  0), intArrayOf( 0,  1), intArrayOf( 0, -1)                    ), "Horse"   , Piece.COLOR_RED , R.drawable.card_horse)
            val ox       = Card(arrayOf(intArrayOf( 1,  0), intArrayOf( 0,  1), intArrayOf( 0, -1)                    ), "Ox"      , Piece.COLOR_BLUE, R.drawable.card_ox)
            val crane    = Card(arrayOf(intArrayOf(-1, -1), intArrayOf( 1, -1), intArrayOf( 0,  1)                    ), "Crane"   , Piece.COLOR_BLUE, R.drawable.card_crane)
            val boar     = Card(arrayOf(intArrayOf(-1,  0), intArrayOf( 1,  0), intArrayOf( 0,  1)                    ), "Boar"    , Piece.COLOR_RED , R.drawable.card_boar)
            val eel      = Card(arrayOf(intArrayOf(-1,  1), intArrayOf(-1, -1), intArrayOf( 1,  0)                    ), "Eel"     , Piece.COLOR_BLUE, R.drawable.card_eel)
            val cobra    = Card(arrayOf(intArrayOf( 1,  1), intArrayOf( 1, -1), intArrayOf(-1,  0)                    ), "Cobra"   , Piece.COLOR_RED , R.drawable.card_cobra)
        }

        fun getCardsSize(): Int {
            return allCards.size
        }

        fun getCardByName(name: String): Card? {
            for(card in allCards) {
                if(card.name === name) {
                    return card
                }
            }
            return null
        }
    }

    constructor(moves: Array<IntArray>, name: String, color: Int, drawable: Int) {
        this.moves = moves
        this.name = name
        this.color = color
        this.drawable = drawable
        allCards.add(this)
    }

    constructor(card: Card) {
        moves = card.getMoves()
        name = card.getName()
        color = card.getColor()
        drawable = card.getDrawable()
    }

    fun getMoves(): Array<IntArray> {
        return moves
    }

    fun getName(): String {
        return name
    }

    fun getColor(): Int {
        return color
    }

    fun getDrawable(): Int {
        return drawable
    }

    fun getColorString(): String {
        return if(color == Piece.COLOR_RED) {
            "Red"
        } else {
            "Blue"
        }
    }

    override fun toString(): String {
        return name
    }

//    fun equals(card: Card): Boolean {
//        return card.getName() == name
//    }
}
