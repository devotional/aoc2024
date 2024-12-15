fun main() {

    data class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int) {
        fun move(steps: Int = 1, xSize: Int = 0, ySize: Int = 0) {
            x += vx * steps
            if (xSize > 0) x = x.mod(xSize)
            y += vy * steps
            if (ySize > 0) y = y.mod(ySize)
        }
    }

    fun parseInput(input: List<String>) =
        input.map {
            val parts = it.split(" ")
            val p = parts[0].substring(2).split(",")
            val v = parts[1].substring(2).split(",")
            Robot(p[0].toInt(), p[1].toInt(), v[0].toInt(), v[1].toInt())
        }

    fun part1(input: List<String>, xSize: Int, ySize: Int): Long {
        val quadrantX = (xSize - 1) / 2
        val quadrantY = (ySize - 1) / 2
        val robots = parseInput(input)
        val quadrantCount = mutableMapOf<Int, Long>()
        robots.map {
            it.move(100, xSize, ySize)
            if (it.x < quadrantX && it.y < quadrantY) {
                1
            } else if (it.x < quadrantX && it.y > quadrantY) {
                2
            } else if (it.x > quadrantX && it.y < quadrantY) {
                3
            } else if (it.x > quadrantX && it.y > quadrantY) {
                4
            } else {
                0
            }
        }.filter { it != 0 }
            .forEach { quadrantCount.put(it, quadrantCount.getOrDefault(it, 0) + 1) }
        return quadrantCount[1]!! * quadrantCount[2]!! * quadrantCount[3]!! * quadrantCount[4]!!
    }

    fun part2(input: List<String>, xSize: Int, ySize: Int) {
        val quadrantY = (ySize - 1) / 2
        fun printMap(robots: List<Robot>, xSize: Int, ySize: Int) {
            val map = Array(xSize) { CharArray(ySize) { '.' } }
            robots.forEach {
                map[it.x][it.y] = '#'
            }
            map.forEach { println(it.joinToString("")) }
        }

        fun isChristmasTree(robots: List<Robot>): Boolean {
            val middleRobots = robots.filter { it.y == quadrantY }.map { it.x }.toSet().sorted()
            var count = 0
            middleRobots.forEachIndexed { index, it ->
                if (index > 1 && middleRobots[index - 1] == it - 1) {
                    count++
                    if (count > 10) return true
                } else {
                    count = 0
                }
            }
            return false
        }

        val robots = parseInput(input)
        var time = 1
        while (true) {
            robots.forEach {
                it.move(1, xSize, ySize)
            }
            if (isChristmasTree(robots)) {
                println(time)
                printMap(robots, xSize, ySize)
                break
            }
            time++
        }
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, 7, 11) == 12L)

    val input = readInput("Day14")
    part1(input, 101, 103).println()
    part2(input, 101, 103)

}
