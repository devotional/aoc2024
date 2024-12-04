fun main() {

    fun parseInput(input: List<String>): Array<CharArray> {
        val board = Array(input.size) { CharArray(input[0].length) }
        for (i in input.indices) {
            for (j in input[i].indices) {
                board[i][j] = input[i][j]
            }
        }
        return board
    }

    fun getChar(board: Array<CharArray>, rowIndex: Int, columnIndex: Int): Char {
        if (rowIndex < 0 || rowIndex >= board.size || columnIndex < 0 || columnIndex >= board[0].size) {
            return ' '
        }
        return board[rowIndex][columnIndex]
    }

    fun checkAllDirections(board: Array<CharArray>, rowIndex: Int, columnIndex: Int): Int {
        val directions = arrayOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1), Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )
        val xmas = "XMAS"
        return directions.map { direction ->
            var row = rowIndex + direction.first
            var column = columnIndex + direction.second
            var xmasIndex = 1;
            while (xmasIndex < xmas.length && getChar(board, row, column) == xmas[xmasIndex]) {
                xmasIndex++
                row += direction.first
                column += direction.second
            }
            if (xmasIndex == xmas.length) 1 else 0
        }.sum()
    }

    fun part1(input: List<String>): Int {
        val board = parseInput(input)
        return board.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                if (cell == 'X') {
                    checkAllDirections(board, rowIndex, columnIndex)
                } else 0
            }.sum()
        }.sum()
    }

    fun checkAllDirections2(board: Array<CharArray>, rowIndex: Int, columnIndex: Int): Int {
        val directions = arrayOf(
            Pair(-1, -1), Pair(-1, 1),
            Pair(1, -1), Pair(1, 1)
        )
        for (direction in directions) {
            if (getChar(board, rowIndex + direction.first, columnIndex + direction.second) != 'M') {
                continue
            }
            if (getChar(board, rowIndex - direction.first, columnIndex - direction.second) != 'S') {
                continue
            }
            val c1 = getChar(board, rowIndex + direction.first, columnIndex - direction.second)
            val c2 = getChar(board, rowIndex - direction.first, columnIndex + direction.second)
            if (c1 == 'S' && c2 == 'M' || c1 == 'M' && c2 == 'S') {
                return 1
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val board = parseInput(input)
        return board.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                if (cell == 'A') {
                    checkAllDirections2(board, rowIndex, columnIndex)
                } else 0
            }.sum()
        }.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
