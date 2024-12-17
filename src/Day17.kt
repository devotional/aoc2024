fun main() {

    data class Computer(
        var a: Long,
        var b: Long,
        var c: Long,
        var ip: Int,
        val program: List<Int>,
        val output: MutableList<Long> = mutableListOf()
    ) {
        fun comboValue(code: Int): Long {
            if (code <= 3) return code.toLong()
            when (code) {
                4 -> return a
                5 -> return b
                6 -> return c
                else -> throw IllegalArgumentException("Invalid combo code $code")
            }
        }

        fun comboValueInt(code: Int): Int {
            val longValue = comboValue(code)
            return longValue.toInt()
        }

        fun run() {
            while (ip < program.size) {
                val opcode = program[ip]
                val operand = program[ip + 1]
                ip += 2
                when (opcode) {
                    0 -> {
                        a = a.shr(comboValueInt(operand))
                    }

                    1 -> {
                        b = b.xor(operand.toLong())
                    }

                    2 -> {
                        b = comboValue(operand).and(7L)
                    }

                    3 -> {
                        if (a != 0L) {
                            ip = operand
                        }
                    }

                    4 -> {
                        b = b.xor(c)
                    }

                    5 -> {
                        output.add(comboValue(operand).and(7L))
                    }

                    6 -> {
                        b = a.shr(comboValueInt(operand))
                    }

                    7 -> {
                        c = a.shr(comboValueInt(operand))
                    }
                }
            }
        }

        fun result(): Long {
            return output.joinToString("").toLong()
        }
    }

    fun parseInput(input: List<String>): Computer {
        val program = input[4].split(" ")[1].split(",").map { it.toInt() }
        val computer = Computer(0, 0, 0, 0, program)
        computer.a = input[0].split(" ")[2].toLong()
        computer.b = input[1].split(" ")[2].toLong()
        computer.c = input[2].split(" ")[2].toLong()
        return computer
    }

    fun part1(input: List<String>): String {
        val computer = parseInput(input)
        computer.run()
        return computer.output.joinToString(",")
    }

    Computer(0, 0, 9, 0, listOf(2, 6)).apply {
        run()
        check(b == 1L)
    }

    Computer(10, 0, 0, 0, listOf(5, 0, 5, 1, 5, 4)).apply {
        run()
        check(result() == 12L)
    }

    Computer(2024, 0, 0, 0, listOf(0, 1, 5, 4, 3, 0)).apply {
        run()
        check(result() == 42567777310L)
        check(a == 0L)
    }

    Computer(0, 2024, 43690, 0, listOf(4, 0)).apply {
        run()
        check(b == 44354L)
    }

    Computer(0, 29, 0, 0, listOf(1, 7)).apply {
        run()
        check(b == 26L)
    }

    fun theProgram(aStart: Long) {
        val output = mutableListOf<Long>()
        var a = aStart
        var b: Long
        var c: Long
        while (a != 0L) {
            b = a.and(7L)
            b = b.xor(1L)
            c = a.shr(b.toInt())
            b = b.xor(5L)
            b = b.xor(c)
            output.add(b.and(7L))
            a = a.shr(3)
        }
        println(output.joinToString(","))
    }

    fun theProgram2(target: List<Int>): Long {
        var currentPrefix = 0
        var bestIndex = 0
        while (true) {
            var index = 0
            val currentA = (currentPrefix.toString(2) + "01001011011010110111010111101").toLong(2)
            var a = currentA
            var b: Long
            var c: Long
            while (a != 0L && index < target.size) {
                b = a.and(7L)
                b = b.xor(1L)
                c = a.shr(b.toInt())
                b = b.xor(5L)
                b = b.xor(c)
                val outResult = b.and(7L).toInt()
                if (outResult != target[index]) {
                    break
                } else index++
                a = a.shr(3)
            }
            if (index > bestIndex) {
                bestIndex = index
            }
            if (a == 0L && index == target.size) {
                return currentA
            }
            currentPrefix++
        }
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")

    theProgram(38610541L)

    val target = listOf(2, 4, 1, 1, 7, 5, 1, 5, 4, 3, 5, 5, 0, 3, 3, 0)
    println(theProgram2(target))

}
