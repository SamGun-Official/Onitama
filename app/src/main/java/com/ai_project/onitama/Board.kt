package com.ai_project.onitama

class Board {
    private val board = Array(5) {
        arrayOfNulls<Piece>(5)
    }

    private var boardCard: Card

    private var curPlayer: Player
    private var offPlayer: Player

    private lateinit var aiCurPlayer: Player
    private lateinit var aiOffPlayer: Player

    private var containsBlueKing = true
    private var containsRedKing = true

    private var blueWin = false
    private var redWin = false

    private var aiCoord: Array<Coordinate> = arrayOf()
    private var aiCard: Card? = null

    constructor(b: Board) {
        boardCard = Card(b.getBoardCard())

        for(i in 0..4) {
            for(j in 0..4) {
                val newPiece = b.getPiece(j, i)
                if(newPiece != null) {
                    board[i][j] = Piece(newPiece.coordinate, newPiece.color, newPiece.isKing)
                }
            }
        }

        curPlayer = Player(b.getCurrentPlayer())
        offPlayer = Player(b.getOffPlayer())
    }

    constructor(inbetween: Card, starter: Player, off: Player) {
        curPlayer = starter
        offPlayer = off
        boardCard = inbetween
        for (i in 0..4) {
            board[0][i] = Piece(Coordinate(i, 0), Piece.COLOR_RED, i == 2)
            board[4][i] = Piece(Coordinate(i, 4), Piece.COLOR_BLUE, i == 2)
        }
    }

    fun setBoardCard(card: Card) {
        boardCard = card
    }

    fun getBoardCard(): Card {
        return boardCard
    }

    fun getCurrentPlayer(): Player {
        return curPlayer
    }

    fun getOffPlayer(): Player {
        return offPlayer
    }

    fun setCurrentPlayer(player: Player) {
        this.curPlayer = player
    }

    fun setOffPlayer(player: Player) {
        this.offPlayer = player
    }

    fun getAiCard() : Card {
        return aiCard!!
    }

    fun switchPlayer() {
        val temp = curPlayer
        curPlayer = Player(offPlayer)
        offPlayer = Player(temp)
    }

    fun isComputerTurn(): Boolean {
        return curPlayer.isComputer()
    }

    fun computerTurn() {
        if (isComputerTurn()) {
            alphabeta()
            println("Size: ${aiCoord.size}")
            play(aiCoord[0], aiCoord[1])
            println(
                curPlayer.getColorString().toString() + " (" + aiCoord[0] + "; " + aiCoord[1] + ")" + "\t move uses card " + aiCard
            )
//            swapCard(aiCard!!)
        }
    }

    private fun alphabeta() {
        aiCoord = emptyArray()
        aiCard = null
        aiCurPlayer = Player(curPlayer)
        aiOffPlayer = Player(offPlayer)
        val temp = Board(boardCard, aiCurPlayer, aiOffPlayer)
        aiCurPlayer.setBoard(temp)
        aiOffPlayer.setBoard(temp)
        println(
            "Selected move score: " + alphabeta(
                this,
                aiCurPlayer.getDifficulty(),
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                true
            )
        )
    }

    fun getKingOfColor(color: Int): Piece? {
        for (p in getPiecesOfColor(color)) if (p != null && p.isKing) return p
        return null
    }

    fun getPieceCountByColor(color: Int): Int {
        var count = 0
        for (p in getPiecesOfColor(color)) if (p != null) ++count
        return count
    }

    private fun alphabeta(node: Board, depth: Int, alpha: Int, beta: Int, maximize: Boolean): Int {
        var alpha = alpha
        var beta = beta
        if (node.checkWin()) {
            return if (node.getWinner()!!.getColor() == aiCurPlayer.getColor()) {
                100 + depth
            } else {
                -(100 + depth)
            }
        }
        if (depth <= 0) {
            val curCount = node.getPieceCountByColor(aiCurPlayer.getColor())
            val offCount = node.getPieceCountByColor(aiOffPlayer.getColor())
            val curCloseness: Int = node.getKingOfColor(aiCurPlayer.getColor())!!.coordinate.compare(
                if (aiCurPlayer.getColor() == Piece.COLOR_RED) Coordinate.BLUE_CROWN else Coordinate.RED_CROWN
            )
            val offCloseness: Int = node.getKingOfColor(aiOffPlayer.getColor())!!.coordinate.compare(
                if (aiOffPlayer.getColor() == Piece.COLOR_RED) Coordinate.BLUE_CROWN else Coordinate.RED_CROWN
            )
            return curCount - curCloseness - (offCount - offCloseness)
        }
        if (maximize) {
            var v = Int.MIN_VALUE
            for (c in node.getCardsOfPlayerColor(aiCurPlayer.getColor())) {
                for (p in node.getPiecesOfColor(aiCurPlayer.getColor())) {
                    if (p == null) continue
                    for (move in c.getMoves()) {
                        val child = Board(node)
                        val from = Coordinate(p.coordinate.x, p.coordinate.y)
                        val to = Coordinate(from.x, from.y)
                        if (!to.validateMove(move[0], move[1], aiCurPlayer.getColor())) continue
                        if (child.getPiece(from) == null) continue
                        if (child.getPiece(to) != null && child.getPiece(from)!!.color == child.getPiece(to)!!.color) continue
                        child.play(from, to)
                        child.swapCard(c)
                        child.switchPlayer()

                        //printBoard(child.getBoard())
                        val retVal = alphabeta(child, depth - 1, alpha, beta, false)

                        if (v < retVal) {
                            v = retVal
                            if (alpha < v) {
                                if (depth == aiCurPlayer.getDifficulty()) {
                                    println("===============================================")
                                    printBoard(child.getBoard())
                                    println("Difficulty: ${aiCurPlayer.getDifficulty()} | Depth: $depth")
                                    println("===============================================")
                                    aiCoord = arrayOf(from, to)
                                    aiCard = c
                                }
                                alpha = v
                                if (beta <= alpha) {
                                    return v
                                }
                            }
                        }
                    }
                }
            }
            return v
        } else {
            var v = Int.MAX_VALUE
            for (c in node.getCardsOfPlayerColor(aiOffPlayer.getColor())) {
                if (c == null) continue
                for (p in node.getPiecesOfColor(aiOffPlayer.getColor())) {
                    if (p == null) continue
                    for (move in c.getMoves()) {
                        if (move == null) continue
                        val child = Board(node)
                        val from = Coordinate(p.coordinate.x, p.coordinate.y)
                        val to = Coordinate(from.x, from.y)
                        if (!to.validateMove(move[0], move[1], aiOffPlayer.getColor()) || from == to) continue
                        if (child.getPiece(from) == null) continue
                        if (child.getPiece(to) != null && child.getPiece(from)!!.color == child.getPiece(to)!!.color) continue
                        child.play(from, to)
                        child.swapCard(c)
                        child.switchPlayer()

                        //printBoard(child.getBoard())
                        val retVal = alphabeta(child, depth - 1, alpha, beta, true)

                        if (v > retVal) {
                            v = retVal
                            if (beta > v) {
                                beta = v
                                if (beta <= alpha) {
                                    return v
                                }
                            }
                        }
                    }
                }
            }
            return v
        }
    }

    fun getBoard(): Array<Array<Piece?>> {
        return board
    }

    fun getCardsOfPlayerColor(color: Int): Array<Card> {
        return getPlayerOfColor(color).getCards()
    }

    fun getPiece(x: Int, y: Int): Piece? {
        return board[y][x]
    }

    fun getPiecesOfPlayer(p: Player): Array<Piece?> {
        return getPiecesOfColor(p.getColor())
    }

    fun getPiecesOfColor(color: Int): Array<Piece?> {
        var index = 0
        val pieces = arrayOfNulls<Piece>(5)
        for (i in 0..24) {
            if(getPiece(i % 5, i / 5) != null && getPiece(i % 5, i / 5)!!.color == color) {
                val tempPiece = getPiece(i % 5, i / 5)!!
                pieces[index++] = Piece(tempPiece.coordinate, tempPiece.color, tempPiece.isKing)
            }
        }
        return pieces
    }

    fun getPiece(c: Coordinate): Piece? {
        return getPiece(c.getX(), c.getY())
    }

    fun exchange(c: Card): Card {
        val temp = boardCard
        boardCard = c
        return temp
    }

    fun getWinner(): Player? {
        if (blueWin || !containsRedKing) return getPlayerOfColor(Piece.COLOR_BLUE)
        return if (redWin || !containsBlueKing) getPlayerOfColor(Piece.COLOR_RED) else null
    }

    fun checkWin(): Boolean {
        var p = getPiece(Coordinate.RED_CROWN)
        if (p != null && p.isKing && p.color == Piece.COLOR_BLUE) return true.also { blueWin = it }
        p = getPiece(Coordinate.BLUE_CROWN)
        if (p != null && p.isKing && p.color == Piece.COLOR_RED) return true.also { redWin = it }
        containsBlueKing = getKingOfColor(Piece.COLOR_BLUE) != null
        containsRedKing = getKingOfColor(Piece.COLOR_RED) != null
        return !(containsBlueKing && containsRedKing)
    }

    fun play(from: Coordinate, to: Coordinate) {
        if (getPiece(to) != null && getPiece(to)!!.isKing && getPiece(to)!!.color != getPiece(from)!!.color) {
            if (getPiece(to)!!.color == Piece.COLOR_BLUE) {
                containsBlueKing = false
            } else {
                containsRedKing = false
            }
        }
        val tempPiece = getPiece(from)!!
        board[to.getY()][to.getX()] = Piece(to, tempPiece.color, tempPiece.isKing)
        board[from.getY()][from.getX()] = null
        //printBoard()
    }

    fun printBoard() {
        for (i in 0..4) {
            for (j in 0..4) System.out.printf(
                "|%2s ",
                if (board[i][j] == null) "" else if (board[i][j]!!.color == Piece.COLOR_RED) "r" else "b"
            )
            println("|")
        }
    }

    fun printBoard(child: Array<Array<Piece?>>) {
        for (i in 0..4) {
            for (j in 0..4) System.out.printf(
                "|%2s ",
                if (child[i][j] == null) "" else if (child[i][j]!!.color == Piece.COLOR_RED) "r" else "b"
            )
            println("|")
        }
    }

    fun getPlayerOfColor(color: Int): Player {
        return if (curPlayer.getColor() == color) curPlayer else offPlayer
    }

    fun swapCard(handCard: Card) {
        for (i in 0..1) {
            if (curPlayer.getCards().get(i).equals(handCard)) {
                curPlayer.setCard(i, boardCard)
                boardCard = Card(handCard)
                break
            }
            if (offPlayer.getCards().get(i).equals(handCard)) {
                offPlayer.setCard(i, boardCard)
                boardCard = Card(handCard)
                break
            }
        }
    }

    fun copy(): Board {
        return Board(this)
    }

    override fun toString(): String {
        return """
             Current Player:
             $curPlayer
             Off Player:
             $offPlayer
             BoardCard:$boardCard
             Board:
             ${getBoardString()}
             """.trimIndent()
    }

    private fun getBoardString(): String {
        var ret = ""
        for (i in 0..24) ret += "|" + i % 5 + (if (getPiece(
                i % 5,
                i / 5
            ) == null
        ) " " else if (getPiece(
                i % 5,
                i / 5
            )!!.color == Piece.COLOR_RED
        ) "r" else "b") + i / 5 + if (i % 5 == 4) "|\n" else ""
        return ret
    }
}
