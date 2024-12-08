data class Area2D(val point: Point2D, val width: Long, val height: Long) {

    constructor(x: Long = 0, y: Long = 0, width: Long = 0, height: Long = 0) : this(Point2D(x, y), width, height)

    fun contains(x: Long, y: Long) =
        x >= this.point.x &&
                x < this.point.x + this.width &&
                y >= this.point.y &&
                y < this.point.y + this.height

    fun contains(point: Point2D) = contains(point.x, point.y)

    fun intersects(other: Area2D) =
        contains(other.point) ||
                contains(other.point.x + other.width - 1, other.point.y) ||
                contains(other.point.x, other.point.y + other.height - 1) ||
                contains(other.point.x + other.width - 1, other.point.y + other.height - 1)

    fun intersection(other: Area2D): Area2D {
        val x = maxOf(this.point.x, other.point.x)
        val y = maxOf(this.point.y, other.point.y)
        val width = minOf(this.point.x + this.width, other.point.x + other.width) - x
        val height = minOf(this.point.y + this.height, other.point.y + other.height) - y
        return Area2D(x, y, width, height)
    }

    fun union(other: Area2D): Area2D {
        val x = minOf(this.point.x, other.point.x)
        val y = minOf(this.point.y, other.point.y)
        val width = maxOf(this.point.x + this.width, other.point.x + other.width) - x
        val height = maxOf(this.point.y + this.height, other.point.y + other.height) - y
        return Area2D(x, y, width, height)
    }

    fun translate(x: Long, y: Long) =
        Area2D(this.point.x + x, this.point.y + y, this.width, this.height)

    fun translate(point: Point2D) = translate(point.x, point.y)

    fun scale(x: Long, y: Long) =
        Area2D(this.point.x * x, this.point.y * y, this.width * x, this.height * y)

    fun scale(point: Point2D) = scale(point.x, point.y)

    fun center() = Point2D(point.x + width / 2, point.y + height / 2)

    fun points() =
        (point.x until point.x + width).flatMap { x ->
            (point.y until point.y + height).map { y ->
                Point2D(x, y)
            }
        }

    fun plot(points: Collection<Point2D>) {
        for (y in point.y until point.y + height) {
            for (x in point.x until point.x + width) {
                if(Point2D(x, y) in points) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println("")
        }
    }

    companion object {
        val EMPTY = Area2D()
    }

}