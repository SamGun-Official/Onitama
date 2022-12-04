package com.example.onitama

data class Coordinate (
    var x: Int? = null,
    var y: Int? = null,
) {
    companion object {
        val RED_CROWN: Coordinate = Coordinate(2, 0)
        val BLUE_CROWN: Coordinate = Coordinate(2, 4)
    }

    fun validateMove(dx: Int, dy: Int, color: Int): Boolean {
        var newX = x!!
        var newY = y!!
        newX += dx * -1 * color
        newY += dy * color
        if(!checkBoundaries(newX, newY)) {
            return false
        }
        setCoordinate(newX, newY)
        return true
    }

    private fun checkBoundaries(x: Int, y: Int): Boolean {
        return x in 0..4 && y in 0..4
    }

    fun setCoordinate(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun getX(): Int {
        return x!!
    }

    fun getY(): Int {
        return y!!
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    fun compare(c: Coordinate): Int {
        return (c.getX() - x!!) * (c.getX() - x!!) + (c.getY() - y!!) * (c.getY() - y!!)
    }

    fun equals(c: Coordinate): Boolean {
        return compare(c) == 0
    }
}
