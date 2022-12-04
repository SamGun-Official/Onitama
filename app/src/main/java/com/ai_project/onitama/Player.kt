package com.ai_project.onitama

import java.util.*

class Player {
    companion object {
        const val EASY = 3
        const val MEDIUM = 4
        const val HARD = 5
    }

    private var cards: Array<Card> = arrayOf()
    private var board: Board? = null
    private var color = 0
    private var isComputer = false
    private var difficulty = 0

    constructor(moves: Array<Card>, playerColor: Int, difficulty: Int = 0) {
        cards = moves
        color = playerColor
        this.difficulty = difficulty
        isComputer = difficulty != 0
    }

    constructor(p: Player) {
        cards = Arrays.copyOf(p.getCards(), 2)
        board = p.getBoard()
        color = p.getColor()
        isComputer = p.isComputer()
        difficulty = p.getDifficulty()
    }


    fun getDifficulty(): Int {
        return difficulty
    }

    fun setDifficulty(n: Int) {
        difficulty = n
    }

    fun isComputer(): Boolean {
        return isComputer
    }

    fun setBoard(b: Board) {
        board = if (board == null) {
            b
        } else {
            Board(b)
        }
    }

    fun getBoard(): Board? {
        return board
    }

    fun setCard(index: Int, card: Card) {
        cards[index] = Card(card)
    }

    fun getMove(): Array<Coordinate?>? {
        return if (isComputer) getMax() else null
    }

    fun getMax(): Array<Coordinate?>? {
        return arrayOf(Coordinate(0, 0), Coordinate(0, 0))
    }

    fun getCards(): Array<Card> {
        return cards
    }

    fun getColor(): Int {
        return color
    }

    override fun toString(): String {
        return """
             ${getColorString()}
             Cards: ${cards[0]}, ${cards[1]}
             Is Computer: $isComputer
             Difficulty: $difficulty
             """.trimIndent()
    }

    fun getColorString(): String {
        return if(color == Piece.COLOR_RED) {
            "Red"
        } else {
            "Blue"
        }
    }


//    lateinit var cards: ArrayList<Card>
//    private var board: Board? = null
//
//    var color: Int
//        get() {
//            return this.color
//        }
//        set(value) {
//            this.color = value
//        }
//
//    var isComputer: Boolean
//        get() {
//            return isComputer
//        }
//        set(value) {
//            this.isComputer = value
//        }
//
//    var difficulty: Int
//        get() {
//            return difficulty
//        }
//        set(value) {
//            this.difficulty = value
//        }
//
//    constructor(moves: ArrayList<Card>, playerColor: Int, difficulty: Int) {
//        Player(moves, playerColor)
//
//        this.isComputer = true
//        this.difficulty = difficulty
//    }
//
//    constructor(moves: ArrayList<Card>, playerColor: Int) {
//        this.cards = moves
//        this.color = playerColor
//        this.difficulty = 0
//        this.isComputer = false
//
//    }
//
//    constructor(p: Player) {
//        cards.clear()
//        cards.add(p.cards[0])
//        cards.add(p.cards[1])
//        this.board = p.board
//        this.color = p.color
//        this.isComputer = p.isComputer
//        this.difficulty = p.difficulty
//    }
//
//    public fun setBoard(b: Board){
//        if (board == null){
//            board = b
//        }
//        else{
//            board = Board(b)
//        }
//    }
//
//    public fun setCard(index: Int, card: Card){
//        cards[index] = Card(card)
//    }
//
//    public fun Coordinate(): ArrayList<Coordinate>? {
//        if (isComputer){
//            return getMax()
//        }
//        return null
//    }
//
//    public fun getMax(): ArrayList<Coordinate>{
//        return arrayListOf(Coordinate(0, 0), Coordinate(0, 0))
//    }
//
//    public fun getColorString(): String {
//        var col = "Blue"
//        if (color == Board.red){
//            col = "Red"
//        }
//        return col
//    }
}
