package com.example.onitama

data class Piece (
    var coordinate: Coordinate,
    var color: Int,
    var isKing: Boolean,
) {
    companion object {
        const val COLOR_RED: Int = 1
        const val COLOR_BLUE: Int = -1
    }

    fun getColorString(): String {
        return if(color == COLOR_RED) {
            "Red"
        } else {
            "Blue"
        }
    }
}
