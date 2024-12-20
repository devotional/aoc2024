class IntBoard(val rows: Int, val columns: Int, init: (Int, Int) -> Int = { _, _ -> 0 }) {

    private val board = Array(rows) { x -> Array(columns) { y -> init(x, y) } }

    constructor(input: List<String>): this(input.size, input[0].length) {
        for (i in 0 until rows) {
            val values = input[i].split("[ ,]+".toRegex()).map { it.toInt() }
            for (j in 0 until columns) {
                board[i][j] = values[j]
            }
        }
    }

    fun drawFrame(value: Int = -1) {
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

    operator fun get(p: Point2D, default: Int): Int {
        return get(p.x.toInt(), p.y.toInt(), default)
    }

    operator fun get(x: Int, y: Int, default: Int): Int {
        return if (contains(x, y)) board[x][y] else default
    }

    operator fun get(p: Point2D): Int {
        return get(p.x.toInt(), p.y.toInt())
    }

    operator fun get(x: Int, y: Int): Int {
        return board[x][y]
    }

    operator fun set(p: Point2D, value: Int) {
        set(p.x.toInt(), p.y.toInt(), value)
    }

    operator fun set(x: Int, y: Int, value: Int) {
        if(contains(x, y)) board[x][y] = value
    }

    fun findCoordinates(c: Int) =
        board.mapIndexed { x, row ->
            row.mapIndexed { y, cell -> if (cell == c) Point2D(x.toLong(), y.toLong()) else null }
        }.flatten().filterNotNull()

    fun print() {
        for (row in board) {
            println(row.joinToString(","))
        }
    }

    fun forEachIndexed(action: (Int, Int, Int) -> Unit) {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                action(i, j, board[i][j])
            }
        }
    }

}