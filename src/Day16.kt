fun main() {

    val directionNames = listOf("S", "E", "N", "W")
    data class Vertex(val point: Point2D, val direction: Int) : Graph.Vertex {
        override fun toString(): String {
            return "Vertex($point, ${directionNames[direction]})"
        }
    }

    data class Edge(override val a: Vertex, override val b: Vertex) : Graph.Edge<Vertex> {
        override fun toString(): String {
            return "Edge($a, $b)"
        }
    }

    class AStar(myEdges: List<Edge>): AlgorithmAStar<Vertex, Edge>(myEdges) {
        override fun costToMoveThrough(edge: Edge): Double {
            val directionChangeCost = if(edge.a.direction == edge.b.direction) 0 else 1000
            return (edge.a.point.distanceManhattan(edge.b.point.x, edge.b.point.y) + directionChangeCost).toDouble()
        }

        override fun createEdge(from: Vertex, to: Vertex): Edge {
            return Edge(from, to)
        }
    }
    val directions = listOf(Point2D(1, 0), Point2D(0, 1), Point2D(-1, 0), Point2D(0, -1))
    fun part1(input: List<String>, finalDirection: Int): Int {
        val edges = mutableListOf<Edge>()
        val board = input.map { it.toCharArray() }.toTypedArray()
        fun findCoordinates(c: Char): Point2D {
            val x = board.indices.first { x -> board[x].any { it == c } }
            val y = board[x].indices.first { y -> board[x][y] == c }
            return Point2D(x.toLong(), y.toLong())
        }

        fun getChar(p: Point2D): Char {
            return board[p.x.toInt()][p.y.toInt()]
        }

        board.forEachIndexed() { x, row ->
            row.forEachIndexed() { y, c ->
                if(c == '.' || c == 'S') {
                    val p = Point2D(x, y)
                    directions.forEachIndexed { directionIndex, direction ->
                        var next = p + direction
                        val existDirectionIndex = (directionIndex + 1) % 4
                        val exitDirection = directions[existDirectionIndex]
                        if(getChar(p + exitDirection) == '.') {
                            edges.add(Edge(Vertex(p, directionIndex), Vertex(p, existDirectionIndex)))
                        }
                        if(getChar(p - exitDirection) == '.') {
                            edges.add(Edge(Vertex(p, directionIndex), Vertex(p, (existDirectionIndex + 2) % 4)))
                        }
                        while (getChar(next) == '.' && getChar(next + exitDirection) == '#' && getChar(next - exitDirection) == '#') {
                            next += direction
                        }
                        if(getChar(next) == '.' || getChar(next) == 'E') {
                            edges.add(Edge(Vertex(p, directionIndex), Vertex(next, directionIndex)))
                        }
                    }
                }
            }
        }
        val start = Vertex(findCoordinates('S'), 1)
        val end = Vertex(findCoordinates('E'), finalDirection)
        val graph = AStar(edges)
        val result = graph.findPath(start, end)
        result.first.forEach {
            println(it)
        }
        return result.second.toInt()
    }

    fun part2(input: List<String>, minimalCost: Int): Int {
        val board = input.map { it.toCharArray() }.toTypedArray()
        fun findCoordinates(c: Char): Point2D {
            val x = board.indices.first { x -> board[x].any { it == c } }
            val y = board[x].indices.first { y -> board[x][y] == c }
            return Point2D(x.toLong(), y.toLong())
        }
        fun getChar(p: Point2D): Char {
            return board[p.x.toInt()][p.y.toInt()]
        }
        fun setChar(p: Point2D, c: Char) {
            board[p.x.toInt()][p.y.toInt()] = c
        }
        fun explore(p: Point2D, direction: Int, cost: Int): Boolean {
            val c = getChar(p)
            if(c == '#' || c == 'X' || cost > minimalCost) {
                return false
            }
            if(c == 'E') {
                return true
            }
            setChar(p, 'X')
            val foundShortest = directions.any {
                val directionCost = if(it == directions[direction]) 1 else 1001
                explore(p + it, direction, cost + directionCost)
            }
            setChar(p, if(foundShortest) 'O' else c)
            return foundShortest
        }
        val start = Vertex(findCoordinates('S'), 1)
        explore(start.point, 1, 0)
        return board.sumOf {
            it.count { c -> c == 'O' }
        }
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput, 2) == 7036)
    check(part2(testInput, 7036) == 64)

    val input = readInput("Day16")
    part1(input, 1).println()
    part2(input, 122492).println()

}
