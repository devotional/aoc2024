fun main() {

    data class Block(val position: Long, val size: Long, val id: Long)

    val EMPTY_BLOCK = -1L

    fun parseDisk(input: String): MutableList<Long>{
        var lastId = -1L
        return input.chunked(2)
            .map {
                lastId++
                val file = 1.rangeTo(it[0] - '0').map { lastId }
                if(it.length == 1) file else file + 1.rangeTo(it[1] - '0').map { EMPTY_BLOCK }
            }.flatten().toMutableList()
    }

    fun checksum(disk: List<Long>) =
        disk.mapIndexed { index, id -> if(id != EMPTY_BLOCK) id * index.toLong() else 0L }.sum()

    fun part1raw(input: List<String>): Long {
        val disk = parseDisk(input[0])
        var forward = 0
        var backward = disk.size - 1
        while (forward < backward) {
            while(disk[forward] != EMPTY_BLOCK) {
                forward++
            }
            while(disk[backward] == EMPTY_BLOCK) {
                backward--
            }
            if(forward >= backward) break
            disk[forward] = disk[backward]
            disk[backward] = EMPTY_BLOCK
        }
        return checksum(disk)
    }

    fun part2raw(input: List<String>): Long {
        val disk = parseDisk(input[0])
        var backward = disk.size - 1
        var lastId = disk.last()
        while (lastId > 0) {
            while(disk[backward] != lastId) {
                backward--
            }
            val endpoint = backward
            while(disk[backward] == lastId) {
                backward--
            }
            val fileLength = endpoint - backward
            var forward = 0
            while(forward < backward && disk.subList(forward, forward+fileLength).any { it != EMPTY_BLOCK }) {
                forward++
            }
            if(forward < backward) {
                disk.subList(forward, forward+fileLength).replaceAll { lastId }
                disk.subList(backward+1, endpoint+1).replaceAll { EMPTY_BLOCK }
            }
            lastId--
        }
        return checksum(disk)
    }

    fun parseDiskBlocks(input: String): MutableList<Block> {
        var lastId = -1L
        val blocks = mutableListOf<Block>()
        var position = 0L
        input.chunked(2).forEach {
            lastId++
            val fileLength = (it[0] - '0').toLong()
            blocks.add(Block(
                position,
                fileLength,
                lastId))
            position += fileLength
            if(it.length > 1) {
                val freeLength = (it[1] - '0').toLong()
                blocks.add(Block(
                    position,
                    freeLength,
                    EMPTY_BLOCK))
                position += freeLength
            }
        }
        return blocks
    }

    fun checksum(blocks: List<Block>) =
        blocks.map {
            var sum = 0L
            if(it.id != EMPTY_BLOCK) {
                for (i in 0..it.size - 1) {
                    sum += (it.position + i) * it.id
                }
            }
            sum
        }.sum()

    fun part1(input: List<String>): Long {
        val blocks = parseDiskBlocks(input[0])
        var backward = blocks.size - 1
        var forward = 0
        while (backward >= forward) {
            val fileBlock = blocks[backward]
            var fileSize = fileBlock.size
            if (fileBlock.id > EMPTY_BLOCK) {
                while (fileSize > 0 && forward < backward) {
                    while (blocks[forward].id != EMPTY_BLOCK) {
                        forward++
                    }
                    val freeBlock = blocks[forward]
                    if (freeBlock.size <= fileSize) {
                        blocks[forward] = Block(freeBlock.position, freeBlock.size, fileBlock.id)
                        fileSize -= freeBlock.size
                    } else {
                        blocks.add(
                            forward + 1,
                            Block(freeBlock.position + fileSize, freeBlock.size - fileSize, EMPTY_BLOCK)
                        )
                        backward++
                        blocks[forward] = Block(freeBlock.position, fileSize, fileBlock.id)
                        fileSize = 0
                    }
                    forward++
                }
                if(fileSize == 0L) {
                    blocks[backward] = Block(fileBlock.position, fileBlock.size, EMPTY_BLOCK)
                } else {
                    blocks[backward] = Block(fileBlock.position, fileSize, fileBlock.id)
                }
            }
            backward--
        }
        return checksum(blocks)
    }

    fun part2(input: List<String>): Long {
        val blocks = parseDiskBlocks(input[0])
        var backward = blocks.size - 1
        var nextId = blocks.last().id
        while (nextId > 0) {
            val fileBlock = blocks[backward]
            val fileSize = fileBlock.size
            if (fileBlock.id == nextId) {
                for(forward in 0..backward) {
                    if (blocks[forward].id == EMPTY_BLOCK) {
                        if(blocks[forward].size == fileSize) {
                            blocks[backward] = Block(fileBlock.position, fileBlock.size, EMPTY_BLOCK)
                            blocks[forward] = Block(blocks[forward].position, fileSize, fileBlock.id)
                            break;
                        } else if(blocks[forward].size > fileSize) {
                            blocks[backward] = Block(fileBlock.position, fileBlock.size, EMPTY_BLOCK)
                            blocks.add(
                                forward + 1,
                                Block(blocks[forward].position + fileSize, blocks[forward].size - fileSize, EMPTY_BLOCK)
                            )
                            blocks[forward] = Block(blocks[forward].position, fileSize, fileBlock.id)
                            backward++
                            break;
                        }
                    }
                }
                nextId--
            }
            backward--
        }
        return checksum(blocks)
    }

    val testInput = readInput("Day09_test")
    check(part1raw(testInput) == 1928L)
    check(part1(testInput) == 1928L)
    check(part2raw(testInput) == 2858L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1raw(input).println()
    part1(input).println()
    part2raw(input).println()
    part2(input).println()
}
