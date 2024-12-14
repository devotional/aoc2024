import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>) =
        Array(input.size) { row ->
            CharArray(input[0].length) { column ->
                input[row][column]
            }
        }

    fun getCell(map: Array<CharArray>, x: Int, y: Int): Char {
        if (x < 0 || x >= map.size || y < 0 || y >= map[0].size) {
            return ' '
        }
        return map[x][y]
    }

    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun part1(input: List<String>): Int {
        val map = parseInput(input)
        fun doCell(cell: Char, x: Int, y: Int): Pair<Int, Int> {
            map[x][y] = '0'
            var sides = directions.map {
                val nextCell = getCell(map, x + it.first, y + it.second)
                if (nextCell != cell && nextCell != '0') {
                    1
                } else 0
            }.sum()
            var count = 1
            directions.map {
                val nextCell = getCell(map, x + it.first, y + it.second)
                if (nextCell == cell) {
                    val (nextSides, nextCount) = doCell(cell, x + it.first, y + it.second)
                    sides += nextSides
                    count += nextCount
                } else 0
            }
            map[x][y] = ' '
            return Pair(sides, count)
        }
        return map.mapIndexed() { x, row ->
            row.mapIndexed { y, cell ->
                if (cell != ' ') {
                    val (sides, count) = doCell(cell, x, y)
                    sides * count
                } else 0
            }.sum()
        }.sum()
    }

    val UNKNOWN = -1000000

    fun getRegionCell(map: Array<IntArray>, x: Int, y: Int): Int {
        if (x < 0 || x >= map.size || y < 0 || y >= map[0].size) {
            return UNKNOWN
        }
        return map[x][y]
    }

    fun part2(input: List<String>): Int {
        var regionId = 1
        val map = parseInput(input)
        val regionMap = Array(input.size) { IntArray(input[0].length) { UNKNOWN } }
        fun detectRegion(regionId: Int, x: Int, y: Int) {
            regionMap[x][y] = regionId
            val cell = getCell(map, x, y)
            directions.forEach {
                val nextX = x + it.first
                val nextY = y + it.second
                if (getRegionCell(regionMap, nextX, nextY) == UNKNOWN && getCell(map, nextX, nextY) == cell) {
                    detectRegion(regionId, nextX, nextY)
                }
            }
        }

        fun detectRegions() {
            map.mapIndexed { x, row ->
                row.mapIndexed { y, cell ->
                    if (getRegionCell(regionMap, x, y) == UNKNOWN) {
                        detectRegion(regionId, x, y)
                        regionId++
                    }
                }
            }
        }

        val checkDirections = listOf(Pair(0, -1), Pair(0, -1), Pair(-1, 0), Pair(-1, 0))

        fun countSides(cell: Int, x: Int, y: Int): Int {
            return directions.mapIndexed { index, it ->
                val sideX = x + it.first
                val sideY = y + it.second
                if(getRegionCell(regionMap, sideX, sideY) != cell) {
                    val prevX = x + checkDirections[index].first
                    val prevY = y + checkDirections[index].second
                    if(getRegionCell(regionMap, prevX, prevY) == cell && getRegionCell(regionMap, prevX+it.first, prevY+it.second) != cell) {
                        0
                    } else {
                        1
                    }
                } else 0
            }.sum()
        }

        detectRegions()
        val regionCount = mutableMapOf<Int, Pair<Int, Int>>()
        regionMap.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell > 0) {
                    val previous = regionCount.computeIfAbsent(cell, { Pair(0, 0)})
                    regionCount.put(cell, Pair(previous.first + 1, previous.second + countSides(cell, x, y)))
                }
            }
        }
        return regionCount.values.sumOf {
            it.first * it.second
        }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 140)
    check(part2(testInput) == 80)

    val testInput2 = readInput("Day12_test2")
    check(part2(testInput2) == 368)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()

}
