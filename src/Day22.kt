fun main() {

    fun parseInput(input: List<String>): List<Long> {
        return input.map { it.toLong() }
    }

    fun part1(input: List<String>): Long {
        return parseInput(input).sumOf {
            var x = it
            for(i in 0 until 2000) {
                x = (x.xor(x.shl(6))).and(0xffffff)
                x = (x.xor(x.shr(5))).and(0xffffff)
                x = (x.xor(x.shl(11))).and(0xffffff)
            }
            x
        }
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<List<Int>, Int>()
        parseInput(input).forEachIndexed { index, it ->
            var x = it
            val sequenceMap = mutableMapOf<List<Int>, Int>()
            val sequence = mutableListOf<Int>()
            val digitSequence = mutableListOf<Int>()
            val xSequence = mutableListOf<Long>()
            var previousValue = x.mod(10)
            for(i in 0 until 2000) {
                x = (x.xor(x.shl(6))).and(0xffffff)
                x = (x.xor(x.shr(5))).and(0xffffff)
                x = (x.xor(x.shl(11))).and(0xffffff)
                val digit = x.mod(10)
                sequence.add(digit - previousValue)
                digitSequence.add(digit)
                xSequence.add(x)
                previousValue = digit
                if(sequence.size > 3) {
                    val storedSequence = sequence.toList()
                    val currentValue = sequenceMap.getOrDefault(storedSequence, 0)
                    if(currentValue == 0) {
                        sequenceMap.set(storedSequence, digit)
                    }
                    sequence.removeFirst()
                    digitSequence.removeFirst()
                    xSequence.removeFirst()
                }
            }
            sequenceMap.forEach { key, value ->
                map[key] = map.getOrDefault(key, 0) + value
            }
        }
        val highestValues = map.values.sortedDescending()
        return highestValues.first()
    }


    val testInput = readInput("Day22_test")
    check(part1(testInput) == 37327623L)
    check(part2(listOf("1", "2", "3", "2024")) == 23)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
