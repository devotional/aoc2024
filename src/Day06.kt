import kotlin.math.cos
import kotlin.math.sin

fun main() {

    fun getChar(board: Array<CharArray>, rowIndex: Int, columnIndex: Int): Char {
        if (rowIndex < 0 || rowIndex >= board.size || columnIndex < 0 || columnIndex >= board[0].size) {
            return ' '
        }
        return board[rowIndex][columnIndex]
    }

    fun getNextX(x: Int, direction: Int) = x - cos(direction * Math.PI / 2).toInt()

    fun getNextY(y: Int, direction: Int) = y + sin(direction * Math.PI / 2).toInt()

    fun printBoard(board: Array<CharArray>) {
        for (row in board) {
            println(row)
        }
    }

    data class Guard(val board: Array<CharArray>, val x: Int, val y: Int, val direction: Int) {
        fun walk(): Guard {
            var nextDirection = direction
            while (true) {
                val nextX = getNextX(x, nextDirection)
                val nextY = getNextY(y, nextDirection)
                val cell = getChar(board, nextX, nextY)
                if (cell == '#' || cell == 'O') {
                    nextDirection = (nextDirection + 1) % 4
                } else {
                    return Guard(board, nextX, nextY, nextDirection)
                }
            }
        }

        fun back(): Guard {
            val backDirection = (direction + 2) % 4
            val nextX = getNextX(x, backDirection)
            val nextY = getNextY(y, backDirection)
            return Guard(board, nextX, nextY, direction)
        }

        fun get(): Char {
            return getChar(board, x, y)
        }

        fun set(value: Char) {
            board[x][y] = value
        }

        fun clone(): Guard {
            return Guard(board.map { it.copyOf() }.toTypedArray(), x, y, direction)
        }

        override fun toString(): String {
            return "Guard($x, $y, $direction)"
        }

    }

    val DIRECTIONS = arrayOf('^', '>', 'v', '<')

    fun parseInput(input: List<String>): Guard {
        val board = Array(input.size) { CharArray(input[0].length) }
        var guardX = 0
        var guardY = 0
        var guardDirection = 0

        for (i in input.indices) {
            for (j in input[i].indices) {
                val cell = input[i][j]
                if (cell == '^') {
                    guardX = i
                    guardY = j
                    guardDirection = 0
                }
                board[i][j] = cell
            }
        }
        return Guard(board, guardX, guardY, guardDirection)
    }

    fun part1(input: List<String>): Int {
        var guard = parseInput(input)
        var steps = 1
        while (guard.get() != ' ') {
            guard = guard.walk()
            if (guard.get() == '.') {
                steps++
                guard.set(DIRECTIONS[guard.direction])
            }
        }
        return steps
    }

    fun part2(input: List<String>): Int {
        var guard = parseInput(input)
        var loops = 0
        while (guard.get() != ' ') {
            guard = guard.walk()
            if (guard.get() == '.') {
                var simulatedGuard = guard.clone()
                simulatedGuard.set('O')
                simulatedGuard = simulatedGuard.back()
                do {
                    simulatedGuard = simulatedGuard.walk()
                    val cell = simulatedGuard.get()
                    if (cell == DIRECTIONS[simulatedGuard.direction]) {
                        loops++
                        println("loop $loops at ${simulatedGuard.x} ${simulatedGuard.y}")
                        break
                    } else if(cell == ' ') {
                        break
                    }
                    if (simulatedGuard.get() == '.') {
                        simulatedGuard.set(DIRECTIONS[simulatedGuard.direction])
                    }
                } while (true)
            }
            if (guard.get() == '.') {
                guard.set(DIRECTIONS[guard.direction])
            }
        }
        return loops
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
