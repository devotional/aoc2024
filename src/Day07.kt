fun main() {

    fun addOrMultiply(currentValue: Long, targetValue: Long, parameters: List<Long>): Boolean {
        if (parameters.isEmpty()) {
            return currentValue == targetValue
        }
        val first = parameters[0]
        val rest = parameters.subList(1, parameters.size)
        if(addOrMultiply(currentValue + first, targetValue, rest)) return true
        return addOrMultiply(currentValue * first, targetValue, rest)
    }

    fun addOrMultiplyOrConcat(currentValue: Long, targetValue: Long, parameters: List<Long>): Boolean {
        if (parameters.isEmpty()) {
            return currentValue == targetValue
        }
        val first = parameters[0]
        val rest = parameters.subList(1, parameters.size)
        if(addOrMultiplyOrConcat(currentValue + first, targetValue, rest)) return true
        if(addOrMultiplyOrConcat(currentValue * first, targetValue, rest)) return true
        return addOrMultiplyOrConcat((currentValue.toString() + first.toString()).toLong(), targetValue, rest)
    }

    fun doPart(input: List<String>, formulaExists: (Long, Long, List<Long>) -> Boolean) =
        input
            .map { it.split(": ", " ").map { it.trim().toLong() } }
            .filter { formulaExists(it[1], it[0], it.subList(2, it.size)) }
            .sumOf { it[0] }

    fun part1(input: List<String>) = doPart(input, ::addOrMultiply)

    fun part2(input: List<String>) = doPart(input, ::addOrMultiplyOrConcat)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
