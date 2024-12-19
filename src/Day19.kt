fun main() {

    fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
        return input[0].split(", ") to input.subList(2, input.size)
    }


    fun part1(input: List<String>): Int {
        val (towels, patterns) = parseInput(input)
        fun canCreatePattern(pattern: String): Boolean {
            if (pattern.isEmpty()) return true
            for(index in towels.indices) {
                if (pattern.startsWith(towels[index]) && canCreatePattern(pattern.substring(towels[index].length))) {
                    return true
                }
            }
            return false
        }
        return patterns.filter { pattern ->
            val x= canCreatePattern(pattern)
            println("$pattern $x")
            x
        }.count()
    }

    fun part2(input: List<String>): Long {
        val cache = mutableMapOf<String, Long>()
        val (towels, patterns) = parseInput(input)
        fun createPatterns(pattern: String): Long {
            if (pattern.isEmpty()) return 1
            if (cache.containsKey(pattern)) return cache[pattern]!!
            val count = towels.sumOf { towel ->
                if (pattern.startsWith(towel)) {
                    createPatterns(pattern.substring(towel.length))
                } else 0
            }
            cache[pattern] = count
            return count
        }
        val result = patterns.sumOf { pattern ->
            println(pattern)
            val x= createPatterns(pattern)
            println(x)
            x
        }
        return result
    }


    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
