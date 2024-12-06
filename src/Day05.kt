fun main() {

    fun listValue(list: List<Int>): Int {
        return list[(list.size - 1) / 2]
    }

    fun findFailingIndex(input: List<Int>, rules: Map<Int, Set<Int>>): Int {
        var index = 1
        while (index < input.size) {
            val rule = rules.getOrDefault(input[index], emptySet())
            for (previousIndex in 0 until index) {
                if (rule.contains(input[previousIndex])) {
                    return index
                }
            }
            index++
        }
        return -1
    }

    fun doPart(input: List<String>, calculation: (List<Int>, Map<Int, Set<Int>>) -> Int): Int {
        var index = 0
        val rules = mutableMapOf<Int, MutableSet<Int>>()
        while (input[index].isNotBlank()) {
            val parts = input[index].split("|")
            rules.computeIfAbsent(parts[0].toInt(), { mutableSetOf() }).add(parts[1].trim().toInt())
            index++
        }
        var sum = 0
        index++
        while (index < input.size) {
            sum += calculation(input[index].split(",").map { it.toInt() }, rules)
            index++
        }
        return sum
    }

    fun part1(input: List<String>): Int {

        fun calculateOrderedValue(input: List<Int>, rules: Map<Int, Set<Int>>): Int {
            if (findFailingIndex(input, rules) < 0) {
                return listValue(input)
            }
            return 0
        }

        return doPart(input, ::calculateOrderedValue)

    }

    fun part2(input: List<String>): Int {

        fun findOrderedValue(input: List<Int>, rules: Map<Int, Set<Int>>): Int {
            val failedIndex = findFailingIndex(input, rules)
            if (failedIndex > -1) {
                val failedValue = input[failedIndex]
                val subList = input.subList(0, failedIndex).toMutableList()
                for (i in input.indices) {
                    subList.add(i, failedValue)
                    val failingIndex = findFailingIndex(subList, rules)
                    if (failingIndex < 0) {
                        subList.addAll(input.subList(failedIndex + 1, input.size))
                        return findOrderedValue(subList, rules)
                    }
                    subList.removeAt(i)
                }
                throw Exception("Failed to find a solution")
            }
            return listValue(input)
        }

        var index = 0
        val rules = mutableMapOf<Int, MutableSet<Int>>()
        while (input[index].isNotBlank()) {
            val parts = input[index].split("|")
            rules.computeIfAbsent(parts[0].toInt(), { mutableSetOf() }).add(parts[1].trim().toInt())
            index++
        }
        var sum = 0
        index++
        while (index < input.size) {
            val inputList = input[index].split(",").map { it.toInt() }
            if (findFailingIndex(inputList, rules) > -1) {
                sum += findOrderedValue(inputList, rules)
            }
            index++
        }
        return sum
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
