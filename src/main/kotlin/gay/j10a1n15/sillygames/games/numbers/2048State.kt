package gay.j10a1n15.sillygames.games.numbers

import gay.j10a1n15.sillygames.rpc.RpcInfo
import gay.j10a1n15.sillygames.rpc.RpcProvider
import gay.j10a1n15.sillygames.utils.SillyUtils.pad

class TwentyFortyEightState: RpcProvider {

    private val rpc = RpcInfo("2048", "Score: 0 | Highest Tile: 0", System.currentTimeMillis())

    private val board = Array(4) { Array(4) { Tile(0) } }

    var won = false
        private set
    var lost = false
        private set
    var score = 0
        private set
    var endingTime = 0L
        private set

    init {
        random()
        random()
    }

    private fun random() {
        if (board.all { row -> row.all { it.value != 0 } }) return

        val x = (0..3).random()
        val y = (0..3).random()
        if (board[x][y].value == 0) {
            board[x][y].value = 2
        } else {
            random()
        }
    }

    private fun merge(dx: Int, dy: Int, simulate: Boolean = false): Boolean {
        var merged = false
        val xRange = if (dx == 1) 3 downTo 1 else if (dx == -1) 0..2 else 0..3
        val yRange = if (dy == 1) 3 downTo 1 else if (dy == -1) 0..2 else 0..3

        board.forEach { row -> row.forEach { it.merged = false } }

        for (x in xRange) {
            for (y in yRange) {
                val current = board[x][y]
                val next = board[x - dx][y - dy]
                if (current.value == next.value && !current.merged && !next.merged) {
                    if (simulate) return true
                    current.value *= 2
                    current.merged = true
                    next.value = 0
                    score += current.value
                    merged = true
                }
            }
        }
        return merged
    }


    private fun move(dx: Int, dy: Int, simulate: Boolean = false): Boolean {
        var moved = false
        when {
            dx == 0 -> {
                val unfiltered = (0..3).map { x -> (0..3).map { y -> board[x][y] } }
                val columns = unfiltered.map { it.filter { it.value != 0 } }
                for (x in 0..3) {
                    val missing = 4 - columns[x].size
                    val merged = columns[x].pad(missing, dy == -1) { Tile(0) }
                    moved = moved or (unfiltered[x] != merged)
                    if (simulate && unfiltered[x] != merged) return true
                    for (y in 0..3) {
                        board[x][y] = merged[y]
                    }
                }
            }
            dy == 0 -> {
                val unfiltered = (0..3).map { y -> (0..3).map { x -> board[x][y] } }
                val rows = unfiltered.map { it.filter { it.value != 0 } }
                for (y in 0..3) {
                    val missing = 4 - rows[y].size
                    val merged = rows[y].pad(missing, dx == -1) { Tile(0) }
                    moved = moved or (unfiltered[y] != merged)
                    if (simulate && unfiltered[y] != merged) return true
                    for (x in 0..3) {
                        board[x][y] = merged[x]
                    }
                }
            }
        }
        return moved
    }

    fun input(dx: Int, dy: Int) {
        if (won || lost) return
        var successful = false
        successful = successful or move(dx, dy)
        successful = successful or merge(-dx, -dy)
        successful = successful or move(dx, dy)
        if (successful) random()

        rpc.secondLine = "Score: $score | Highest Tile: ${board.flatMap { it.map { it.value }.toList() }.maxOrNull()}"

        if (board.any { row -> row.any { it.value == 2048 } }) {
            won = true
            endingTime = System.currentTimeMillis()
            rpc.end = endingTime
        } else {
            val canMerge = merge(1, 0, true) || merge(-1, 0, true) || merge(0, 1, true) || merge(0, -1, true)
            val canMove = move(1, 0, true) || move(-1, 0, true) || move(0, 1, true) || move(0, -1, true)
            if (!canMerge && !canMove) {
                lost = true
                endingTime = System.currentTimeMillis()
                rpc.end = endingTime
            }
        }
    }

    fun get(x: Int, y: Int) = board[x][y].value

    override fun getRpcInfo() = rpc
}

private data class Tile(var value: Int, var merged: Boolean = false)
