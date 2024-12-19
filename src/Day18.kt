fun main() {

    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    data class Vertex(val x: Int, val y: Int) : Graph.Vertex

    data class Edge(override val a: Vertex, override val b: Vertex) : Graph.Edge<Vertex> {
        override fun toString(): String {
            return "Edge($a, $b)"
        }
    }

    class AStar(edges: List<Edge>) : AlgorithmAStar<Vertex, Edge>(edges) {
        override fun costToMoveThrough(edge: Edge): Double {
            return 1.0
        }

        override fun createEdge(from: Vertex, to: Vertex): Edge {
            return Edge(from, to)
        }
    }

    fun parseInput(input: List<String>): List<Vertex> {
        return input.map { it.split(",").map { it.toInt() } }.map { Vertex(it[0], it[1]) }
    }

    fun findEdges(boardSize: Int, obstacles: List<Vertex>): List<Edge> {
        val edges = mutableListOf<Edge>()
        for (x in 0 until boardSize) {
            for (y in 0 until boardSize) {
                val p = Vertex(x, y)
                if (!obstacles.contains(p))
                    directions.forEach { direction ->
                        val d = Vertex(direction.first + x, direction.second + y)
                        if (d.x in 0 until boardSize && d.y in 0 until boardSize
                            && !obstacles.contains(d)
                        ) {
                            edges.add(Edge(p, d))
                        }
                    }
            }
        }
        return edges
    }

    fun printBoard(obstacles: List<Vertex>, path: List<Vertex>, boardSize: Int) {
        for (y in 0 until boardSize) {
            for (x in 0 until boardSize) {
                val p = Vertex(x, y)
                if (obstacles.contains(p)) {
                    print("#")
                } else if (path.contains(p)) {
                    print("O")
                } else {
                    print(".")
                }
            }
            println()
        }

    }

    fun part1(boardSize: Int, input: List<String>): Int {
        val obstacles = parseInput(input)
        printBoard(obstacles, listOf(), boardSize)
        val edges = findEdges(boardSize, obstacles)
        val algorithm = AStar(edges)
        val start = Vertex(0, 0)
        val end = Vertex(boardSize - 1, boardSize - 1)
        val (path, cost) = algorithm.findPath(start, end)
        printBoard(obstacles, path, boardSize)
        return cost.toInt() - 1
    }


    fun part2(boardSize: Int, input: List<String>, startSize: Int): Vertex {
        val allObstacles = parseInput(input)
        var edges = findEdges(boardSize, allObstacles.subList(0, startSize))
        var currentSize = startSize
        val start = Vertex(0, 0)
        val end = Vertex(boardSize - 1, boardSize - 1)
        try {
            while(true) {
                val newObstacle = allObstacles[currentSize]
                edges = edges.filter { edge -> edge.a != newObstacle && edge.b != newObstacle }
                AStar(edges).findPath(start, end)
                currentSize++
            }
        } catch(e: Exception) {
            return allObstacles[currentSize]
        }
    }

    val testInput = readInput("Day18_test")
    check(part1(7, testInput.subList(0, 12)) == 22)
    check(part2(7, testInput, 12) == Vertex(6,1))

    val input = readInput("Day18")
    part1(71, input.subList(0, 1024)).println()
    part2(71, input, 1024).println()
}
