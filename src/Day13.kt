import java.math.BigDecimal

fun main() {

    data class XY(val x: BigDecimal, val y: BigDecimal)

    fun parseXY(s: String, offset: BigDecimal): XY {
        val matchResult = "X[+=]([0-9]*), Y[+=]([0-9]*)".toRegex().find(s)
        return XY(
            BigDecimal(matchResult!!.groups[1]!!.value) + offset,
            BigDecimal(matchResult.groups[2]!!.value) + offset
        )
    }

    fun parseInput(input: List<String>, offset: BigDecimal) =
        input.chunked(4).map {
            listOf(parseXY(it[0], BigDecimal.ZERO), parseXY(it[1], BigDecimal.ZERO), parseXY(it[2], offset))
        }

    fun doPart(input: List<String>, offset: BigDecimal): Long {

        fun calculateB2(target: XY, a: XY, b: XY) =
             (a.x * target.y - ((target.x * a.y))) / (b.y * a.x - (b.x * a.y))

        val parsedInput = parseInput(input, offset)

        return parsedInput.sumOf {
            val bCount = calculateB2(it[2], it[0], it[1])
            val aCount = ((it[2].x - bCount * it[1].x) / it[0].x)
            if ( aCount >= BigDecimal.ZERO && bCount >= BigDecimal.ZERO &&
                aCount * it[0].x + bCount * it[1].x == it[2].x && aCount * it[0].y + bCount * it[1].y == it[2].y
            ) {
                (aCount + aCount + aCount + bCount).toLong()
            } else {
                0
            }
        }
    }


    fun part1(input: List<String>): Long = doPart(input, BigDecimal.ZERO)

    fun part2(input: List<String>): Long = doPart(input, BigDecimal("10000000000000"))

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()

}
