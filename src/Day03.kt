 import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val pattern = "mul\\((\\d+),(\\d+)\\)".toRegex()
        return input.sumOf {
            pattern.findAll(it).sumOf { matchResult ->
                matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val pattern = "do(n't)?\\(\\)".toRegex()
        var enabled = true
        return input.sumOf {
            var position = 0
            pattern.findAll(it).sumOf { matchResult ->
                val ss = it.substring(position, matchResult.range.first)
                val result = if (enabled) part1(listOf(ss)) else 0
                position = matchResult.range.last + 1
                enabled = matchResult.value == "do()"
                result
            } + if (enabled) part1(listOf(it.substring(position))) else 0
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
