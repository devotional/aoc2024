import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

data class Point2D(val x: Long, val y: Long): Comparable<Point2D>, Graph.Vertex  {

    constructor(x: Int, y: Int): this(x.toLong(), y.toLong())

    override fun compareTo(other: Point2D): Int {
        return if (x == other.x) {
            y.compareTo(other.y)
        } else {
            x.compareTo(other.x)
        }
    }

    enum class DistanceType {
        EUCLIDIAN, MANHATTAN, CHEBYCHEV
    }

    operator fun unaryMinus(): Point2D {
        return Point2D(-x, -y)
    }

    operator fun plus(other: Point2D): Point2D {
        return translate(other)
    }

    operator fun minus(other: Point2D): Point2D {
        return translate(-other)
    }

    operator fun times(scalar: Long): Point2D {
        return Point2D(x * scalar, y * scalar)
    }

    operator fun div(scalar: Long): Point2D {
        return Point2D(x / scalar, y / scalar)
    }

    fun distance(other: Point2D, type: DistanceType = DistanceType.MANHATTAN) =
        distance(other.x, other.y, type)

    fun distance(x: Long, y: Long, type: DistanceType = DistanceType.MANHATTAN) =
        when (type) {
            DistanceType.EUCLIDIAN -> distanceEuclidian(x, y).toLong()
            DistanceType.MANHATTAN -> distanceManhattan(x, y)
            DistanceType.CHEBYCHEV -> distanceChebychev(x, y)
        }

    fun distanceEuclidian(x: Long, y: Long): Double {
        val xDiff = this.x - x
        val yDiff = this.y - y
        return kotlin.math.sqrt((xDiff * xDiff + yDiff * yDiff).toDouble())
    }

    fun distanceManhattan(x: Long, y: Long) = abs(this.x - x) + abs(this.y - y)

    fun distanceChebychev(x: Long, y: Long) = max(abs(this.x - x), abs(this.y - y))

    fun translate(x: Long, y: Long)= Point2D(this.x + x, this.y + y)

    fun translate(point: Point2D) = translate(point.x, point.y)

    fun scale(x: Long, y: Long) = Point2D(this.x * x, this.y * y)

    fun scale(point: Point2D) = scale(point.x, point.y)

    fun rotate(angle: Double): Point2D {
        val x = x * cos(angle) - y * sin(angle)
        val y = x * sin(angle) + y * cos(angle)
        return Point2D(x.toLong(), y.toLong())
    }

    fun rotate(point: Point2D, angle: Double) = translate(-point.x, -point.y).rotate(angle).translate(point)

    fun rotate90() = Point2D(-y, x)

    fun rotate90(point: Point2D) = translate(-point.x, -point.y).rotate90().translate(point)

    fun rotate180() = Point2D(-x, -y)

    fun rotate180(point: Point2D) = translate(-point.x, -point.y).rotate180().translate(point)

    fun rotate270() = Point2D(y, -x)

    fun rotate270(point: Point2D) = translate(-point.x, -point.y).rotate270().translate(point)

    fun mirrorX() = Point2D(-x, y)

    fun mirrorX(point: Point2D) = translate(-point.x, -point.y).mirrorX().translate(point)

    fun mirrorY() = Point2D(x, -y)

    fun mirrorY(point: Point2D) = translate(-point.x, -point.y).mirrorY().translate(point)

    fun mirrorXY() = Point2D(y, x)

    fun mirrorXY(point: Point2D) = translate(-point.x, -point.y).mirrorXY().translate(point)

    fun neighbours() = listOf(
            Point2D(x - 1, y - 1),
            Point2D(x, y - 1),
            Point2D(x + 1, y - 1),
            Point2D(x - 1, y),
            Point2D(x + 1, y),
            Point2D(x - 1, y + 1),
            Point2D(x, y + 1),
            Point2D(x + 1, y + 1)
        )

    fun isAdjacent(other: Point2D) = abs(other.x - x) <= 1 && abs(other.y - y) <= 1

    fun toList() = listOf(x, y)

    override fun toString() = "($x,$y)"

}