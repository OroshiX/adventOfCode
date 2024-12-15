package advent

data class Position(val i: Int, val j: Int) {
    fun isInBounds(nbLines: Int, nbCols: Int): Boolean {
        return i in 0 until nbLines && j in 0 until nbCols
    }
}
