fun main() {

    data class Antenna(val position: Point2D, val frequency: Char)

    var area = Area2D()

    fun parseInput(input: List<String>): List<Antenna> {
        val antennas = mutableListOf<Antenna>()
        area = Area2D(0, 0, input.size.toLong(), input[0].length.toLong())
        input.forEachIndexed { x, line ->
            line.forEachIndexed { y, cell ->
                if (cell != '.') {
                    antennas.add(Antenna(Point2D(x.toLong(), y.toLong()), cell))
                }
            }
        }
        return antennas
    }

    fun doPart(input: List<String>, singleAntinode: Boolean): Int {
        val antennas = parseInput(input)
        val antinodes = mutableSetOf<Point2D>()

        fun addAntinode(position: Point2D, diff: Point2D) {
            var antinode = position + diff
            while (area.contains(antinode)) {
                antinodes.add(antinode)
                if (singleAntinode) break
                antinode += diff
            }
        }

        antennas.forEach { antenna ->
            antennas
                .filter { it.frequency == antenna.frequency && it.position != antenna.position }
                .forEach { other ->
                    val diff = Point2D(
                        other.position.x - antenna.position.x,
                        other.position.y - antenna.position.y
                    )
                    addAntinode(antenna.position, -diff)
                    addAntinode(other.position, diff)
                }
        }

        if (!singleAntinode) antennas.forEach { antinodes.add(it.position) }
        return antinodes.size
    }

    fun part1(input: List<String>) = doPart(input, true)

    fun part2(input: List<String>) = doPart(input, false)

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
