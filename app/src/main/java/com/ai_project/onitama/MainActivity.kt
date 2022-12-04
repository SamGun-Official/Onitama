package com.ai_project.onitama

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var tiles: ArrayList<ImageButton>

    private lateinit var next_card: ImageButton
    private lateinit var next_card_enemy: ImageButton
    private lateinit var card_1_red: ImageButton
    private lateinit var card_2_red: ImageButton
    private lateinit var card_1_blue: ImageButton
    private lateinit var card_2_blue: ImageButton

    private lateinit var board: Board
    private lateinit var player1: Player
    private lateinit var player2: Player

//    private var gameBoard: Board? = null
//    private val colorRed: Color = Color(0xf5544e)
//    private val colorBlue: Color = Color(0x4e96f5)
//    private lateinit var nextCard: Card

    private lateinit var cards: Array<Card>

    private var isCardSelected: Boolean = false
    private var isPieceSelected: Boolean = false

    private lateinit var selectedPiece: Piece
    private lateinit var selectedCard: Card
    private var idxCard = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tiles = arrayListOf()
        tiles.add(findViewById(R.id.tile_0_0))
        tiles.add(findViewById(R.id.tile_1_0))
        tiles.add(findViewById(R.id.tile_2_0))
        tiles.add(findViewById(R.id.tile_3_0))
        tiles.add(findViewById(R.id.tile_4_0))
        tiles.add(findViewById(R.id.tile_0_1))
        tiles.add(findViewById(R.id.tile_1_1))
        tiles.add(findViewById(R.id.tile_2_1))
        tiles.add(findViewById(R.id.tile_3_1))
        tiles.add(findViewById(R.id.tile_4_1))
        tiles.add(findViewById(R.id.tile_0_2))
        tiles.add(findViewById(R.id.tile_1_2))
        tiles.add(findViewById(R.id.tile_2_2))
        tiles.add(findViewById(R.id.tile_3_2))
        tiles.add(findViewById(R.id.tile_4_2))
        tiles.add(findViewById(R.id.tile_0_3))
        tiles.add(findViewById(R.id.tile_1_3))
        tiles.add(findViewById(R.id.tile_2_3))
        tiles.add(findViewById(R.id.tile_3_3))
        tiles.add(findViewById(R.id.tile_4_3))
        tiles.add(findViewById(R.id.tile_0_4))
        tiles.add(findViewById(R.id.tile_1_4))
        tiles.add(findViewById(R.id.tile_2_4))
        tiles.add(findViewById(R.id.tile_3_4))
        tiles.add(findViewById(R.id.tile_4_4))

        next_card = findViewById(R.id.next_card)
        next_card_enemy = findViewById(R.id.next_card_enemy)
        card_1_red = findViewById(R.id.card_1_red)
        card_2_red = findViewById(R.id.card_2_red)
        card_1_blue = findViewById(R.id.card_1_blue)
        card_2_blue = findViewById(R.id.card_2_blue)

        Card.init()
        Deck.init()

        drawCards()

        board = firstToMove()

        card_1_blue.setOnClickListener {
            reRenderView()
            isPieceSelected = false
            isCardSelected = true
            val curPlayer = board.getCurrentPlayer()
            val ofFPlayer = board.getOffPlayer()
            if(!curPlayer.isComputer()) {
                selectedCard = curPlayer.getCards()[0]
                idxCard = 0
            } else {
                selectedCard = ofFPlayer.getCards()[0]
                idxCard = 3
            }
            println("SELECTED: $selectedCard")
        }
        card_2_blue.setOnClickListener {
            reRenderView()
            isPieceSelected = false
            isCardSelected = true
            val curPlayer = board.getCurrentPlayer()
            val ofFPlayer = board.getOffPlayer()
            if(!curPlayer.isComputer()) {
                selectedCard = curPlayer.getCards()[1]
                idxCard = 1
            } else {
                selectedCard = ofFPlayer.getCards()[1]
                idxCard = 4
            }
            println("SELECTED: $selectedCard")
        }

        initView()

        for (tile in tiles) {
            tile.setOnClickListener {
                val tag: Int = tile.tag.toString().toInt()
                val x: Int = (tag - 1) % 5
                val y: Int = (tag - 1) / 5
                val tempBoard = board.getBoard()[y][x]
                if(isCardSelected) {
                    if(tempBoard != null && tempBoard.color == Piece.COLOR_BLUE) {
                        println("$x, $y")
                        if(!isPieceSelected) {
                            selectedPiece = tempBoard
                            isPieceSelected = true
                            val from: Coordinate = selectedPiece.coordinate
                            highlight(from)
                        } else {
                            if(selectedPiece.coordinate.x == x && selectedPiece.coordinate.y == y) {
                                isPieceSelected = false
                                reRenderView()
                            } else {
                                selectedPiece = tempBoard
                                val from: Coordinate = selectedPiece.coordinate
                                highlight(from)
                            }
                        }
                    } else if(isPieceSelected) {
                        println("$x, $y")
                        val from: Coordinate = selectedPiece.coordinate
                        var to: Coordinate? = null
                        var esc = false
                        for(i in 0..4) {
                            for(j in 0..4) {
                                if(j == x && i == y) {
                                    to = Coordinate(x, y)
                                    esc = true
                                    break
                                }
                            }
                            if(esc) {
                                break
                            }
                        }
                        println(from)
                        println(to)
                        if(to != null) {
                            var isValid = false
                            for(pair in selectedCard.getMoves()) {
                                val newX: Int = pair[0] * board.getCurrentPlayer().getColor() * -1 + from.getX()
                                val newY: Int = pair[1] * board.getCurrentPlayer().getColor() + from.getY()
                                if(newX in 0..4 && newY in 0..4) {
                                    if(newX == to.getX() && newY == to.getY()) {
                                        isValid = true
                                        break
                                    }
//                                    if(board.getBoard()[newY][newX] == null || board.getBoard()[newY][newX]!!.color == Piece.COLOR_RED) {
//                                        tiles[newY * 5 + newX].setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.yellow)))
//                                    }
                                }
                            }
                            if(isValid) {
                                play(from, to)
                                reRenderView()
    //                            switchCard()
                            } else {
                                Toast.makeText(this, "GABOLEH JALAN SITU WOY", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // ERROR
                        }
                    } else {
                        // ERROR
                    }
                }
            }
        }

//        gameBoard = Board(board!!)
//        gameBoard!!.printBoard()

        player1.setBoard(board)
        player2.setBoard(board)

        println("Player 1: " + player1.getDifficulty().toString() + " - Player 2: " + player2.getDifficulty())

        if(board.isComputerTurn()) {
            compTurn()
        }
    }

    private fun highlight(from: Coordinate) {
//        if(selectedCard == null) {
//            println("Unhighlighted error: no card selected");
//            return
//        }
//
//        if(selectedPiece != null)
//            unhighlightMoves();
//
//        selectedPawn = c;
//        var from: Coordinate? = null
//
//        for(x in 0..4) {
//            for(y in 0..4) {
//                if(lblBoard[y][x] == selectedPawn) {
//                    from = new Coordinate(x,y);
//                    x=7;
//                    break;
//                }
//            }
//        }
//        if(from == null) {
//            System.out.println("Highlighted error: no pawn selected");
//            selectedPawn = null;
//            return;
//        }
        reRenderView()

        for(pair in selectedCard.getMoves()) {
            val newX: Int = pair[0] * board.getCurrentPlayer().getColor() * -1 + from.getX()
            val newY: Int = pair[1] * board.getCurrentPlayer().getColor() + from.getY()
            if(newX in 0..4 && newY in 0..4) {
                if(board.getBoard()[newY][newX] == null || board.getBoard()[newY][newX]!!.color == Piece.COLOR_RED) {
                    tiles[newY * 5 + newX].setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.yellow)))
                }
            }
        }
    }

    private fun firstToMove(): Board {
        return if(cards[2].getColor() == Piece.COLOR_RED) {
            Board(cards[2], player1, player2)
        } else {
            Board(cards[2], player2, player1)
        }
    }

    private fun drawCards() {
        cards = Deck.draw()
        player1 = Player(arrayOf(cards[0], cards[3]), Piece.COLOR_RED, Player.EASY)
        player2 = Player(arrayOf(cards[1], cards[4]), Piece.COLOR_BLUE)
    }

    private fun reRenderView() {
        for(k in 0 until tiles.size) {
            tiles[k].setImageResource(R.color.transparent)
            tiles[k].setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.light_yellow)))
            val tag: Int = tiles[k].tag.toString().toInt()
            val j: Int = (tag - 1) % 5
            val i: Int = (tag - 1) / 5
            if(board.getBoard()[i][j] != null) {
                if((board.getBoard()[i][j] as Piece).color == Piece.COLOR_RED) {
                    if((board.getBoard()[i][j] as Piece).isKing) {
                        tiles[k].setImageResource(R.drawable.piece_orange_king)
                    } else {
                        tiles[k].setImageResource(R.drawable.piece_orange_pawn)
                    }
                } else {
                    if((board.getBoard()[i][j] as Piece).isKing) {
                        tiles[k].setImageResource(R.drawable.piece_blue_king)
                    } else {
                        tiles[k].setImageResource(R.drawable.piece_blue_pawn)
                    }
                }
            }
        }
    }

    private fun play(from: Coordinate, to: Coordinate) {
        switchCard()
        move(from, to)
        checkWin()
        if (board.isComputerTurn() && !board.checkWin()) {
            compTurn()
        }
    }

    private fun move(from: Coordinate, to: Coordinate) {
        if (isCardSelected) {
            if (isPieceSelected) {
                isPieceSelected = false
            }
            isCardSelected = false
        }
        board.play(from, to)
        update()
        updateBoard()
    }

    fun initView() {
        next_card.setImageResource(board.getBoardCard().getDrawable())
        next_card_enemy.setImageResource(board.getBoardCard().getDrawable())
        if(board.getCurrentPlayer().isComputer()) {
            card_1_red.setImageResource(board.getCurrentPlayer().getCards()[0].getDrawable())
            card_2_red.setImageResource(board.getCurrentPlayer().getCards()[1].getDrawable())
        } else {
            card_1_blue.setImageResource(board.getCurrentPlayer().getCards()[0].getDrawable())
            card_2_blue.setImageResource(board.getCurrentPlayer().getCards()[1].getDrawable())
        }
        if(board.getOffPlayer().isComputer()) {
            card_1_red.setImageResource(board.getOffPlayer().getCards()[0].getDrawable())
            card_2_red.setImageResource(board.getOffPlayer().getCards()[1].getDrawable())
        } else {
            card_1_blue.setImageResource(board.getOffPlayer().getCards()[0].getDrawable())
            card_2_blue.setImageResource(board.getOffPlayer().getCards()[1].getDrawable())
        }
//        card_1_blue.setImageResource(player2.getCards()[0].getDrawable())
//        card_2_blue.setImageResource(player2.getCards()[1].getDrawable())
    }

    fun compTurn() {
        board.computerTurn()
        update()
        var cards = board.getOffPlayer().getCards()
        // search card
        for (i in 0 until cards.size) {
            if(board.getAiCard() == cards[i]) {
                idxCard = i + 3
            }
        }
        if (idxCard != -1) {
            switchCard()
        }
        checkWin()
        if (board.isComputerTurn() && !board.checkWin()) {
            try {
                Thread.sleep(500)
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            compTurn()
        }
    }

    private fun checkWin() {
        updateBoard()
        if (board.checkWin()) {
            Toast.makeText(this, "Winner: " + board.getWinner()!!.getColorString(), Toast.LENGTH_SHORT).show()
            try {
                Thread.sleep(1000)
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            } finally {
                resetGame()
            }
        }
    }

    private fun updateBoard() {
        reRenderView()
        initView()
//        board.printBoard()
//        for (y in 0..4) for (x in 0..4) if (gameBoard!!.getPiece(x, y) == null) {
//            lblBoard.get(y).get(x).setText("")
//            lblBoard.get(y).get(x).setBackground(Color.WHITE)
//        } else {
//            if (gameBoard.getPiece(x, y)!!.isKing) lblBoard.get(y).get(x)
//                .setText("K") else lblBoard.get(y).get(x).setText("P")
//            if (gameBoard.getPiece(x, y)!!.color == Board.red) lblBoard.get(y).get(x)
//                .setBackground(colorRed) else lblBoard.get(y).get(x).setBackground(colorBlue)
//        }
    }

    fun update() {
        //updatePlayer
        switchPlayer()

        //update Cards
//        if (lastCardHighlighted != null) switchCard() else updateCards()

        //update Board
        updateBoard()
    }

    private fun updateCards() {
//        var c: Array<Card> = board.getPlayerOfColor(Piece.COLOR_RED).getCards()
//        red1.set(CardGUI(c[0], Board.red))
//        red2.set(CardGUI(c[1], Board.red))
//        c = gameBoard.getPlayerOfColor(Board.blue).getCards()
//        blue1.set(CardGUI(c[0], Board.blue))
//        blue2.set(CardGUI(c[1], Board.blue))
//        tableCard.set(CardGUI(gameBoard.getBoardCard(), Board.blue))
    }

    private fun switchCard() {
//        if (selectedCard == null) {
//            println("ERROR SWITCHING CARDS!")
//        }
//        val temp = CardGUI(lastCardHighlighted as CardGUI?)
//        selectedCard = null
//        for(i in 0 until cards.size) {
//            println(cards[i].getName())
//        }
        println("===================================")
        val curPlayer = board.getCurrentPlayer()
        val offPlayer = board.getOffPlayer()

        println("${curPlayer.getColorString()} (${curPlayer.isComputer()}): ${curPlayer.getCards()[0]} - ${curPlayer.getCards()[1]}")
        println("${offPlayer.getColorString()} (${offPlayer.isComputer()}): ${offPlayer.getCards()[0]} - ${offPlayer.getCards()[1]}")
        println(board.getBoardCard())

        // simpan di temp untuk punya player
        //idxCard untuk player : 0 berarti card yg idx ke-0, 1
        //idxCard untuk ai : 3,4
        if(idxCard < 2) {
            //cardnya player
            var tempCard = curPlayer.getCards()[idxCard]
            curPlayer.setCard(idxCard, board.getBoardCard())
            board.setBoardCard(tempCard)
        }
        else {
            idxCard -= 3
            var tempCard = offPlayer.getCards()[idxCard]
            offPlayer.setCard(idxCard, board.getBoardCard())
            board.setBoardCard(tempCard)
        }
        idxCard = -1
        board.setCurrentPlayer(curPlayer)
        board.setOffPlayer(offPlayer)

//        if(curPlayer.isComputer()) {
////            cards[0] = curPlayer.getCards()[0]
////            cards[3] = curPlayer.getCards()[1]
//            player1.setCard(0, curPlayer.getCards()[0])
//            player1.setCard(1, curPlayer.getCards()[1])
//        } else {
////            cards[1] = curPlayer.getCards()[0]
////            cards[4] = curPlayer.getCards()[1]
//            player2.setCard(0, curPlayer.getCards()[0])
//            player2.setCard(1, curPlayer.getCards()[1])
//        }
//        if(offPlayer.isComputer()) {
////            cards[0] = offPlayer.getCards()[0]
////            cards[3] = offPlayer.getCards()[1]
//            player1.setCard(0, offPlayer.getCards()[0])
//            player1.setCard(1, offPlayer.getCards()[1])
//        } else {
////            cards[1] = offPlayer.getCards()[0]
////            cards[4] = offPlayer.getCards()[1]
//            player2.setCard(0, offPlayer.getCards()[0])
//            player2.setCard(1, offPlayer.getCards()[1])
//        }
//        cards[2] = selectedCard
//        for(i in 0 until cards.size) {
//            println(cards[i].getName())
//        }
//        board.swapCard(Card.getCardByName(selectedCard.getName())!!)
//        updateCards()
        updateBoard()
    }

    private fun switchPlayer() {
        board.switchPlayer()
//        curPlayer.setText(gameBoard.getCurrentPlayer().getColorString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_reset -> {
//                Deck.shuffle()
//                drawCards()
//                initView()
                resetGame()
            }
            R.id.menu_vs_player -> {
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun resetGame() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}
