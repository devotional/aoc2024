fun main() {

    val moveDirections = mapOf(
        '^' to Pair(-1, 0),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1),
        '>' to Pair(0, 1)
    )

    fun part1(input: List<String>, inputMoves: List<String>): Int {
        val board = input.map { it.toCharArray() }.toTypedArray()
        var robotX = board.indices.first { x -> board[x].any { it == '@' } }
        var robotY = board[robotX].indices.first { y -> board[robotX][y] == '@' }
        inputMoves.joinToString("").forEach { move ->
            val direction = moveDirections[move]!!
            var nextX = robotX + direction.first
            var nextY = robotY + direction.second
            val firstX = nextX
            val firstY = nextY
            while (board[nextX][nextY] != '#' && board[nextX][nextY] != '.') {
                nextX += direction.first
                nextY += direction.second
            }
            if (board[nextX][nextY] == '.') {
                board[nextX][nextY] = 'O'
                board[robotX][robotY] = '.'
                robotX = firstX
                robotY = firstY
                board[robotX][robotY] = '@'
            }
        }
        return board.mapIndexed { x, row ->
            row.mapIndexed { y, cell -> if (cell == 'O') (x) * 100 + (y) else 0 }.sum()
        }.sum()
    }

    fun part2(input: List<String>, inputMoves: List<String>): Int {
        val board = input.map {
            it.map { c ->
                if (c == '@') listOf(c, '.')
                else if (c == 'O') listOf('[', ']')
                else listOf(c, c)
            }.flatten().toCharArray()
        }.toTypedArray()

        fun moveTo(x: Int, y: Int, direction: Pair<Int, Int>, c: Char, doMove: Boolean = true): Boolean {
            if (board[x][y] == '#') {
                return false
            } else if (board[x][y] == '.') {
                if(doMove) board[x][y] = c
                return true
            } else if (board[x][y] == '[') {
                if (direction.first == 0) {
                    if (moveTo(x, y + 2, direction, ']', doMove)) {
                        if(doMove) {
                            board[x][y+1] = board[x][y]
                            board[x][y] = c
                        }
                        return true
                    }
                } else {
                    if (moveTo(x + direction.first, y, direction, '[', false) &&
                        moveTo(x + direction.first, y + 1, direction, ']', doMove)) {
                        if(doMove) {
                            moveTo(x + direction.first, y, direction, '[')
                            board[x + direction.first][y + 1] = ']'
                            board[x][y] = c
                            board[x][y+1] = '.'
                        }
                        return true
                    }
                }
            } else if (board[x][y] == ']') {
                if (direction.first == 0) {
                    if(moveTo(x, y - 2, direction, '[', doMove)) {
                        if(doMove) {
                            board[x][y-1] = board[x][y]
                            board[x][y] = c
                        }
                        return true
                    }
                } else {
                    if (moveTo(x + direction.first, y - 1, direction, '[', false) &&
                        moveTo(x + direction.first, y, direction, ']', doMove)) {
                        if(doMove) {
                            moveTo(x + direction.first, y - 1, direction, '[')
                            board[x + direction.first][y] = ']'
                            board[x][y] = c
                            board[x][y-1] = '.'
                        }
                        return true
                    }
                }
            }
            return false
        }

        var robotX = board.indices.first { x -> board[x].any { it == '@' } }
        var robotY = board[robotX].indices.first { y -> board[robotX][y] == '@' }
        inputMoves.joinToString("").forEach { move ->
            val direction = moveDirections[move]!!
            val firstX = robotX + direction.first
            val firstY = robotY + direction.second
            if (moveTo(firstX, firstY, direction, '@')) {
                board[robotX][robotY] = '.'
                robotX = firstX
                robotY = firstY
            }
        }
        return board.mapIndexed { x, row ->
            row.mapIndexed { y, cell ->
                if (cell == '[') (x) * 100 + (y) else 0
            }.sum()
        }.sum()

    }

    val testSmallInput = readInput("Day15_small_test")
    val testSmallInputMoves = readInput("Day15_small_test_moves")
    check(part1(testSmallInput, testSmallInputMoves) == 2028)

    val testSmallInput2 = readInput("Day15_small_test_2")
    val testSmallInputMoves2 = readInput("Day15_small_test_2_moves")
    part2(testSmallInput2, testSmallInputMoves2)

    val testInput = readInput("Day15_test")
    val testInputMoves = readInput("Day15_test_moves")
    check(part1(testInput, testInputMoves) == 10092)
    check(part2(testInput, testInputMoves) == 9021)
    val input = readInput("Day15")
    val inputMoves = readInput("Day15_moves")
    part1(input, inputMoves).println()
    part2(input, inputMoves).println()

}
