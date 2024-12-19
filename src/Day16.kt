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

    val seenEdges = mutableSetOf<Edge>()

    fun moveCost(edge: Edge): Double {
        val bonusCost = if (seenEdges.contains(edge)) 0.1 else 0.0
        val directionCost = if (edge.a.direction == edge.b.direction) 0.0 else 1000.0
        return edge.a.point.distanceManhattan(edge.b.point.x, edge.b.point.y).toDouble() +
                directionCost +
                bonusCost
    }

    class AStar(myEdges: List<Edge>) : AlgorithmAStar<Vertex, Edge>(myEdges) {
        override fun costToMoveThrough(edge: Edge) = moveCost(edge)

        override fun createEdge(from: Vertex, to: Vertex): Edge {
            return Edge(from, to)
        }
    }

    val directions = listOf(Point2D(1, 0), Point2D(0, 1), Point2D(-1, 0), Point2D(0, -1))

    fun findEdges(board: Array<CharArray>): MutableList<Edge> {
        fun getChar(p: Point2D): Char {
            return board[p.x.toInt()][p.y.toInt()]
        }

        val edges = mutableListOf<Edge>()
        board.forEachIndexed() { x, row ->
            row.forEachIndexed() { y, c ->
                if (c == '.' || c == 'S') {
                    val p = Point2D(x, y)
                    directions.forEachIndexed { directionIndex, direction ->
                        var next = p + direction
                        val exitDirectionIndex = (directionIndex + 1) % 4
                        val exitDirection = directions[exitDirectionIndex]
                        if (getChar(p + exitDirection) == '.') {
                            edges.add(Edge(Vertex(p, directionIndex), Vertex(p, exitDirectionIndex)))
                        }
                        if (getChar(p - exitDirection) == '.') {
                            edges.add(Edge(Vertex(p, directionIndex), Vertex(p, (exitDirectionIndex + 2) % 4)))
                        }
                        while (getChar(next) == '.' && getChar(next + exitDirection) == '#' && getChar(next - exitDirection) == '#') {
                            next += direction
                        }
                        if (getChar(next) == '.' || getChar(next) == 'E') {
                            val edge = Edge(Vertex(p, directionIndex), Vertex(next, directionIndex))
                            edges.add(edge)
                        }
                    }
                }
            }
        }
        return edges
    }

    fun part1(input: List<String>, finalDirection: Int): Int {
        val board = input.map { it.toCharArray() }.toTypedArray()
        fun findCoordinates(c: Char): Point2D {
            val x = board.indices.first { x -> board[x].any { it == c } }
            val y = board[x].indices.first { y -> board[x][y] == c }
            return Point2D(x.toLong(), y.toLong())
        }

        val edges = findEdges(board)
        val start = Vertex(findCoordinates('S'), 1)
        val end = Vertex(findCoordinates('E'), finalDirection)
        val graph = AStar(edges)
        val result = graph.findPath(start, end)
        return result.second.toInt()
    }

    fun printBoard(board: Array<CharArray>) {
        board.forEach { println(it.joinToString("")) }
    }

    fun part2(input: List<String>, minimalCost: Int, finalDirection: Int): Int {
        val board = input.map { it.toCharArray() }.toTypedArray()
        fun findCoordinates(c: Char): Point2D {
            val x = board.indices.first { x -> board[x].any { it == c } }
            val y = board[x].indices.first { y -> board[x][y] == c }
            return Point2D(x.toLong(), y.toLong())
        }

        val edges = findEdges(board)
        val start = Vertex(findCoordinates('S'), 1)
        val end = Vertex(findCoordinates('E'), finalDirection)
        val cache = mutableMapOf<Vertex, Int>()
        fun explore(path: List<Vertex>, vertex: Vertex, cost: Int): Boolean {
            if (cost > minimalCost) {
                return false
            }
            if (vertex.point == end.point) {
                return true
            }
            if (path.contains(vertex)) {
                return false
            }
            if (cache[vertex] != null && cache[vertex]!! < cost) {
                return false
            }
            cache[vertex] = cost
            val newPath = path + vertex
            return edges.filter { it.a.point == vertex.point && it.a.direction == vertex.direction }.filter {
                if (explore(newPath, it.b, moveCost(it).toInt() + cost)) {
                    var p = it.a.point
                    val d = directions[it.a.direction]
                    while (p != it.b.point) {
                        board[p.x.toInt()][p.y.toInt()] = 'O'
                        p += d
                    }
                    true
                } else false
            }.isNotEmpty()
        }
        explore(listOf(), start, 0)
        return board.sumOf {
            it.count { c -> c == 'O' }
        } + 1
    }

    val testInput = readInput("Day16_test")
    val part1TestAnswer = part1(testInput, 2)
    check(part1TestAnswer == 7036)
    check(part2(testInput, part1TestAnswer, 2) == 45)

    val input = readInput("Day16")
    val part1Answer = part1(input, 1)
    println(part1Answer)
    part2(input, part1Answer, 1).println()

}
