import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        input.forEach { line ->
            val (v1, v2) = line.split(" +".toRegex()).map {
                it.toInt()
            }
            list1.add(v1)
            list2.add(v2)
        }
        return Pair(list1, list2)
    }

    fun part1(input: List<String>): Int {
        val (list1, list2) = parseInput(input)
        val sortedlist2 = list2.sorted()
        return list1.sorted().mapIndexed { index, i ->
            abs(i - sortedlist2[index])
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val (list1, list2) = parseInput(input)
        val occurrences = mutableMapOf<Int, Int>()
        list2.map {
            occurrences[it] = occurrences.getOrDefault(it, 0) + 1
        }
        return list1.sumOf {
            occurrences[it]?.times(it) ?: 0
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
