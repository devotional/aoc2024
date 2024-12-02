import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(" +".toRegex()).map {
                it.toInt()
            }
        }
    }

    fun checkReport(report: List<Int>): Boolean {
        var allIncreasing = true
        var allDecreasing = true
        for(i in 0 until report.size - 1) {
            if (allIncreasing && report[i] >= report[i + 1]) {
                allIncreasing = false
            }
            if (allDecreasing && report[i] <= report[i + 1]) {
                allDecreasing = false
            }
            if(!allIncreasing && !allDecreasing) {
                return false
            }
            val difference = abs(report[i] - report[i + 1])
            if (difference < 1 || difference > 3) {
                return false
            }
        }
        return true
    }

    fun checkReportWithDampener(report: List<Int>): Boolean {
        if(checkReport(report)) {
            return true
        }
        for(index in report.indices) {
            if(checkReport(report.filterIndexed { i, _ -> i != index })) {
                return true
            }
        }
        return false
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).filter {
            checkReport(it)
        }.count()
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).filter {
            checkReportWithDampener(it)
        }.count()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
