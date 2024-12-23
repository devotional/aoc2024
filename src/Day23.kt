fun main() {

    fun parseInput(input: List<String>): Map<String, Set<String>> {
        val result = mutableMapOf<String, Set<String>>()
        fun addConnection(a: String, b: String) {
            result[a] = result.getOrDefault(a, setOf()) + b
            result[b] = result.getOrDefault(b, setOf()) + a
        }
        input.forEach {
            val nodes = it.split("-")
            addConnection(nodes[0], nodes[1])
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val connections = parseInput(input)
        val result = mutableSetOf<String>()
        connections.forEach { (node, neighbours) ->
            neighbours.forEach { neighbour ->
                connections[neighbour]!!.intersect(neighbours).forEach { common ->
                    val trio = listOf(node,neighbour,common).sorted()
                    if(trio.any { it[0] == 't' }) {
                        result.add(trio.joinToString(","))
                    }
                }
            }
        }
        return result.size
    }

    fun part2(input: List<String>): String {
        val connections = parseInput(input)
        val groups = mutableMapOf<Set<String>, Int>()
        connections.forEach { (node, neighbours) ->
            neighbours.forEach { neighbour ->
                val common = connections[neighbour]!!.intersect(neighbours)
                if(common.isNotEmpty()) {
                    val group = setOf(node, neighbour) + common
                    groups[group] = groups.getOrDefault(group, 0) + 1
                }
            }
        }
        return groups.filter { it.key.size * (it.key.size-1) == it.value }
            .toList()
            .maxBy { it.second }
            .first
            .sorted()
            .joinToString(",")
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
