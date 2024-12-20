import kotlin.math.abs

fun main() {

    val directions = listOf(Point2D(1, 0), Point2D(-1, 0), Point2D(0, 1), Point2D(0, -1))

    fun loadBoard(input: List<String>): Pair<CharBoard, List<Point2D>> {
        val board = CharBoard(input)
        var currentPosition = board.findCoordinates('S')[0]
        val end = board.findCoordinates('E')[0]
        val path = mutableListOf<Point2D>()
        while (currentPosition != end) {
            path.add(currentPosition)
            currentPosition = directions
                .map { currentPosition + it }
                .filter { !path.contains(it) }
                .first { ".E".contains(board[it]) }
        }
        path.add(end)
        return Pair(board, path)
    }

    fun part1(input: List<String>): Int {
        val (board, path) = loadBoard(input)
        val cheats = path.map { position ->
            directions.map {
                position + it
            }.filter {
                board[it] == '#' &&
                        it.x in 1 until board.rows - 1 &&
                        it.y in 1 until board.columns - 1
            }.map {
                val exitPosition = (it - position) * 2 + position
                val exitIndex = path.indexOf(exitPosition)
                exitIndex - path.indexOf(position) - 2
            }.filter {
                it > 0
            }
        }.flatten().sorted()

        return cheats.filter { it >= 100 }.count()
    }

    fun part2(input: List<String>): Int {
        val (board, path) = loadBoard(input)

        val distanceBoard = IntBoard(board.rows, board.columns) { x, y ->
            path.indexOf(Point2D(x.toLong(), y.toLong()))
        }

        distanceBoard.forEachIndexed { x, y, cell ->
            if (cell == -1) {
                val closest = directions
                    .map { Point2D(x.toLong(), y.toLong()) + it }
                    .filter {
                        it.x in 1 until distanceBoard.rows - 1 &&
                                it.y in 1 until distanceBoard.columns - 1 &&
                                distanceBoard[it] > -1
                    }.minOfOrNull { distanceBoard[it] }
                if (closest != null) {
                    distanceBoard[x, y] = closest + 1
                }
            }
        }

        return path.map {
            val originalDistance = distanceBoard[it]
            (-20..20).map { x ->
                (-20 + abs(x)..20 - abs(x)).map { y ->
                    val p = it + Point2D(x.toLong(), y.toLong())
                    val distance = distanceBoard[p]
                    if (p.x in 1 until distanceBoard.rows - 1 &&
                        p.y in 1 until distanceBoard.columns - 1 &&
                        distance > -1
                    ) {
                        val gain = originalDistance - (distance + abs(x) + abs(y))
                        if (gain >= 100 && path.contains(p)) 1 else 0
                    } else {
                        0
                    }
                }.sum()
            }.sum()
        }.sum()

    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
