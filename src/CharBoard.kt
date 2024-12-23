open class CharBoard(val rows: Int, val columns: Int, init: (Int, Int) -> Char = { _, _ -> '.' }) {

    private val board = Array(rows) { x -> Array(columns) { y -> init(x, y) } }

    constructor(input: List<String>): this(input.size, input[0].length) {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                board[i][j] = input[i][j]
            }
        }
    }

    fun drawFrame(value: Char = '#') {
        for (i in 0 until columns) {
            board[0][i] = value
            board[rows - 1][i] = value
        }
        for (i in 0 until rows) {
            board[i][0] = value
            board[i][columns - 1] = value
        }
    }

    fun contains(p: Point2D): Boolean {
        return contains(p.x.toInt(), p.y.toInt())
    }

    fun contains(x: Int, y: Int): Boolean {
        return x in 0 until rows && y in 0 until columns
    }

    operator fun get(p: Point2D, default: Char): Char {
        return get(p.x.toInt(), p.y.toInt(), default)
    }

    operator fun get(x: Int, y: Int, default: Char): Char {
        return if (contains(x, y)) board[x][y] else default
    }

    operator fun get(p: Point2D): Char {
        return get(p.x.toInt(), p.y.toInt())
    }

    operator fun get(x: Int, y: Int): Char {
        return board[x][y]
    }

    operator fun set(p: Point2D, value: Char) {
        set(p.x.toInt(), p.y.toInt(), value)
    }

    operator fun set(x: Int, y: Int, value: Char) {
        if(contains(x, y)) board[x][y] = value
    }

    fun findCoordinates(c: Char) =
        board.mapIndexed { x, row ->
            row.mapIndexed { y, cell -> if (cell == c) Point2D(x.toLong(), y.toLong()) else null }
        }.flatten().filterNotNull()

    fun print() {
        for (row in board) {
            println(row.joinToString(""))
        }
    }

    fun forEachIndexed(action: (Int, Int, Char) -> Unit) {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                action(i, j, board[i][j])
            }
        }
    }

    fun sumOf(predicate: (Char) -> Int): Int {
        return board.map { row -> row.map { cell -> predicate(cell) }.sum() }.sum()
    }

    fun sumOfIndexed(predicate: (Int, Int, Char) -> Int): Int {
        return board.mapIndexed { x, row -> row.mapIndexed { y, cell -> predicate(x, y, cell) }.sum() }.sum()
    }

}