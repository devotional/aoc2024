fun main() {

    val cache = mutableMapOf<Pair<Long, Int>, Long>()

    fun part1(list: List<Long>, iterations: Int): Long {

        fun cacheResult(key: Pair<Long, Int>, result: Long): Long {
            cache.put(key, result)
            return result
        }

        fun blink1(x: Long, n: Int): Long {
            if(n == 0) return 1
            val key = Pair(x, n)
            if(cache.containsKey(key)) return cache[key]!!
            if(x == 0L) {
                return cacheResult(key, blink1(1, n - 1))
            }
            val s = x.toString()
            if(s.length % 2 == 0) {
                return cacheResult(key,
                    blink1(s.substring(0, s.length / 2).toLong(), n - 1) +
                            blink1(s.substring(s.length / 2).toLong(), n - 1))
            }
            return cacheResult(key, blink1(x * 2024L, n - 1))
        }

        return list.sumOf { blink1(it, iterations) }
    }

    check(part1(listOf(125L, 17L), 25) == 55312L)

    val input = listOf(0L,7L, 198844L, 5687836L, 58L, 2478L, 25475L, 894L)
    part1(input, 25).println()
    part1(input, 75).println()

}
