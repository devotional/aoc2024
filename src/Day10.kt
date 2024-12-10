fun main() {

    fun parseInput(input: List<String>) =
        Array(input.size) { row ->
            IntArray(input[0].length) {
                column -> input[row][column] - '0'
            }
        }

    fun getHeight(board: Array<IntArray>, row: Int, column: Int): Int {
        if (row in board.indices && column in board[0].indices) {
            return board[row][column]
        } else {
            return -1
        }
    }

    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun doPart(input: List<String>, initTrail: () -> Unit, score: (Int, Int) -> Int): Int {
        val board = parseInput(input)

        fun explore(row: Int, column: Int): Int {
            val nextHeight = getHeight(board, row, column) + 1
            if(nextHeight == 10) {
                return score(row, column)
            }
            return directions.map {
                if(getHeight(board, row + it.first, column + it.second) == nextHeight) {
                    explore(row + it.first, column + it.second)
                } else 0
            }.sum()
        }

        return board.mapIndexed { row, ints ->
            ints.mapIndexed { column, i ->
                if (i == 0) {
                    initTrail()
                    explore(row, column)
                } else {
                    0
                }
            }.sum()
        }.sum()

    }

    fun part1(input: List<String>): Int {
        val seenTargets = mutableSetOf<Pair<Int, Int>>()
        return doPart(input,
            { seenTargets.clear() },
            { row, column -> if(seenTargets.add(Pair(row, column))) 1 else 0 }
        )
    }

    fun part2(input: List<String>): Int {
        return doPart(input, { }, { _, _ -> 1 })
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()

}
