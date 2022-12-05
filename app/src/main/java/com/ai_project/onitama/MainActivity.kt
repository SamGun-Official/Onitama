package com.ai_project.onitama

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

    private lateinit var cards: Array<Card>

    private var isCardSelected: Boolean = false
    private var isPieceSelected: Boolean = false

    private lateinit var selectedPiece: Piece
    private lateinit var selectedCard: Card
    private var idxCard = -1

    private var isLiveVersus = false
    private var currentColor: Int = Piece.COLOR_BLUE
    private var currentDifficulty: Int = Player.EASY

    private fun startState() {
        isCardSelected = false
        isPieceSelected = false
        idxCard = -1

        currentColor = Piece.COLOR_BLUE

        Deck.init()
        drawCards()
        board = firstToMove()
        refreshCard()

        if(isLiveVersus) {
            if(board.getBoardCard().getColor() == Piece.COLOR_RED) {
                currentColor = Piece.COLOR_RED
            }

            if(currentColor == Piece.COLOR_BLUE) {
                Toast.makeText(this, "First Turn: Blue", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "First Turn: Red", Toast.LENGTH_LONG).show()
            }
        }

        player1.setBoard(board)
        player2.setBoard(board)

        val curPlayer = board.getCurrentPlayer()
        val offPlayer = board.getOffPlayer()

        println("===================================")
        println("${curPlayer.getColorString()} (${curPlayer.isComputer()}): ${curPlayer.getCards()[0]} - ${curPlayer.getCards()[1]}")
        println("${offPlayer.getColorString()} (${offPlayer.isComputer()}): ${offPlayer.getCards()[0]} - ${offPlayer.getCards()[1]}")
        println(board.getBoardCard())

        if(!isLiveVersus) {
            if(board.getCurrentPlayer().getColor() == Piece.COLOR_BLUE) {
                Toast.makeText(this, "First Turn: Blue", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "First Turn: Red", Toast.LENGTH_LONG).show()
            }

            if(board.isComputerTurn()) {
                compTurn()
            }
        }

        redrawBoard()
    }

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

        println("=============================================")
        for(card in Card.allCards) {
            println(card)
        }
        println("=============================================")

        startState()

        card_1_red.setOnClickListener {
            println("CURRENT: $currentColor")
            if(isLiveVersus && currentColor == Piece.COLOR_RED) {
                redrawBoard()
                isPieceSelected = false
                isCardSelected = true
                val curPlayer = board.getCurrentPlayer()
                selectedCard = curPlayer.getCards()[0]
                idxCard = 0
                println("SELECTED: $selectedCard")
            }
        }
        card_2_red.setOnClickListener {
            println("CURRENT: $currentColor")
            if(isLiveVersus && currentColor == Piece.COLOR_RED) {
                redrawBoard()
                isPieceSelected = false
                isCardSelected = true
                val curPlayer = board.getCurrentPlayer()
                selectedCard = curPlayer.getCards()[1]
                idxCard = 1
                println("SELECTED: $selectedCard")
            }
        }
        card_1_blue.setOnClickListener {
            println("CURRENT: $currentColor")
            if(currentColor == Piece.COLOR_BLUE) {
                redrawBoard()
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
        }
        card_2_blue.setOnClickListener {
            println("CURRENT: $currentColor")
            if(currentColor == Piece.COLOR_BLUE) {
                redrawBoard()
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
        }

        for (tile in tiles) {
            tile.setOnClickListener {
                if(!board.checkWin()) {
                    val tag: Int = tile.tag.toString().toInt()
                    val x: Int = (tag - 1) % 5
                    val y: Int = (tag - 1) / 5
                    val tempBoard = board.getBoard()[y][x]
                    if(isCardSelected) {
                        if(tempBoard != null && tempBoard.color == currentColor) {
                            println("$x, $y")
                            if(!isPieceSelected) {
                                selectedPiece = tempBoard
                                isPieceSelected = true
                                val from: Coordinate = selectedPiece.coordinate
                                highlight(from)
                            } else {
                                if(selectedPiece.coordinate.x == x && selectedPiece.coordinate.y == y) {
                                    isPieceSelected = false
                                    redrawBoard()
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
                                    }
                                }
                                if(isValid) {
                                    play(from, to)
                                    redrawBoard()
                                } else {
                                    Toast.makeText(this, "Cannot move that way!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // ERROR
                            }
                        } else {
                            // ERROR
                        }
                    }
                } else {
                    checkWin()
                }
            }
        }
    }

    private fun highlight(from: Coordinate) {
        redrawBoard()
        for(pair in selectedCard.getMoves()) {
            val newX: Int = pair[0] * board.getCurrentPlayer().getColor() * -1 + from.getX()
            val newY: Int = pair[1] * board.getCurrentPlayer().getColor() + from.getY()
            if(newX in 0..4 && newY in 0..4) {
                if(board.getBoard()[newY][newX] == null || board.getBoard()[newY][newX]!!.color != currentColor) {
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
        player1 = Player(arrayOf(cards[0], cards[3]), Piece.COLOR_RED, currentDifficulty)
        player2 = Player(arrayOf(cards[1], cards[4]), Piece.COLOR_BLUE)
    }

    private fun redrawBoard() {
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
        if(!isLiveVersus) {
            if (board.isComputerTurn() && !board.checkWin()) {
                compTurn()
            }
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

    fun refreshCard() {
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
    }

    fun compTurn() {
        board.computerTurn()
        update()
        val cardss = board.getOffPlayer().getCards()
        // search card
        for (i in 0 until cardss.size) {
            if(board.getAiCard() == cardss[i]) {
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
            Toast.makeText(this, "Winner: " + board.getWinner()!!.getColorString(), Toast.LENGTH_LONG).show()
            // Force reset using button
        }
    }

    private fun updateBoard() {
        redrawBoard()
        refreshCard()
    }

    fun update() {
        switchPlayer()
        updateBoard()
    }

    private fun switchCard() {
        val curPlayer = board.getCurrentPlayer()
        val offPlayer = board.getOffPlayer()

        println("===================================")
        println("${curPlayer.getColorString()} (${curPlayer.isComputer()}): ${curPlayer.getCards()[0]} - ${curPlayer.getCards()[1]}")
        println("${offPlayer.getColorString()} (${offPlayer.isComputer()}): ${offPlayer.getCards()[0]} - ${offPlayer.getCards()[1]}")
        println(board.getBoardCard())

        // simpan di temp untuk punya player
        // idxCard untuk player : 0 berarti card yg idx ke-0, 1
        // idxCard untuk ai : 3,4
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

        //board.swapCard(Card.getCardByName(selectedCard.getName())!!)
        updateBoard()
    }

    private fun switchPlayer() {
        board.switchPlayer()
        if(isLiveVersus) {
            if(currentColor == Piece.COLOR_RED) {
                currentColor = Piece.COLOR_BLUE
            } else {
                currentColor = Piece.COLOR_RED
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_reset -> {
                startState()
            }
            R.id.menu_vs_bot -> {
                if(!isLiveVersus) {
                    Toast.makeText(this@MainActivity, "You're already battling an AI!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Changing mode to PLAYER VS COMPUTER!", Toast.LENGTH_SHORT).show()
                    isLiveVersus = false
                    startState()
                }
            }
            R.id.menu_vs_player -> {
                if(isLiveVersus) {
                    Toast.makeText(this@MainActivity, "You're already battling another PLAYER!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Changing mode to PLAYER VS PLAYER!", Toast.LENGTH_SHORT).show()
                    isLiveVersus = true
                    startState()
                }
            }
            R.id.difficulty_easy -> {
                if(isLiveVersus) {
                    Toast.makeText(this@MainActivity, "Change the mode to versus computer first then the difficulty!", Toast.LENGTH_SHORT).show()
                } else if(currentDifficulty == Player.EASY) {
                    Toast.makeText(this@MainActivity, "You're already battling EASY AI!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Changing difficulty to EASY!", Toast.LENGTH_SHORT).show()
                    currentDifficulty = Player.EASY
                    startState()
                }
            }
            R.id.difficulty_medium -> {
                if(isLiveVersus) {
                    Toast.makeText(this@MainActivity, "Change the mode to versus computer first then the difficulty!", Toast.LENGTH_SHORT).show()
                } else if(currentDifficulty == Player.MEDIUM) {
                    Toast.makeText(this@MainActivity, "You're already battling MEDIUM AI!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Changing difficulty to MEDIUM!", Toast.LENGTH_SHORT).show()
                    currentDifficulty = Player.MEDIUM
                    startState()
                }
            }
            R.id.difficulty_hard -> {
                if(isLiveVersus) {
                    Toast.makeText(this@MainActivity, "Change the mode to versus computer first then the difficulty!", Toast.LENGTH_SHORT).show()
                } else if(currentDifficulty == Player.HARD) {
                    Toast.makeText(this@MainActivity, "You're already battling HARD AI!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Changing difficulty to HARD!", Toast.LENGTH_SHORT).show()
                    currentDifficulty = Player.HARD
                    startState()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
