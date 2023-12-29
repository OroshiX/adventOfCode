package advent.days

import advent.Position
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun `isOnPerimeter works correctly`() {
        val polygon = Polygon(
            Position(0, 0) to rgb("#000000"),
            Position(0, 1) to rgb("#000000"),
            Position(1, 1) to rgb("#000000"),
            Position(1, 0) to rgb("#000000"),
        )
        withClue("0,0 is on perimeter") {
            polygon.isOnPerimeter(0, 0) shouldBe true
        }
        withClue("0,1 is on perimeter") {
            polygon.isOnPerimeter(0, 1) shouldBe true
        }
        withClue("1,1 is on perimeter") {
            polygon.isOnPerimeter(1, 1) shouldBe true
        }
        withClue("1,0 is on perimeter") {
            polygon.isOnPerimeter(1, 0) shouldBe true
        }
        withClue("0,2 is not on perimeter") {
            polygon.isOnPerimeter(0, 2) shouldBe false
        }
        withClue("2,0 is not on perimeter") {
            polygon.isOnPerimeter(2, 0) shouldBe false
        }
    }

    @Test
    fun `isInside works correctly`() {
        val polygon = Polygon(
            Position(0, 0) to rgb("#000000"),
            Position(0, 1) to rgb("#000000"),
            Position(1, 1) to rgb("#000000"),
            Position(1, 0) to rgb("#000000"),
        )
        withClue("0.5,0.5 is inside") {
            polygon.isInside(0.5f, 0.5f) shouldBe true
        }
        withClue("0.5,1.5 is not inside") {
            polygon.isInside(0.5f, 1.5f) shouldBe false
        }
        withClue("1.5,0.5 is not inside") {
            polygon.isInside(1.5f, 0.5f) shouldBe false
        }
        withClue("1.5,1.5 is not inside") {
            polygon.isInside(1.5f, 1.5f) shouldBe false
        }
        withClue("-0.5,0.5 is not inside") {
            polygon.isInside(-0.5f, 0.5f) shouldBe false
        }
        withClue("0.5,-0.5 is not inside") {
            polygon.isInside(0.5f, -0.5f) shouldBe false
        }
    }

    @Test
    fun `getPointsInsidePolygon works correctly`() {
        val polygon = Polygon(
            Position(0, 0) to rgb("#000000"),
            Position(0, 1) to rgb("#000000"),
            Position(1, 1) to rgb("#000000"),
            Position(1, 0) to rgb("#000000"),
        )
        polygon.getPointsInsidePolygon() shouldBe listOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1),
        )

        val polygon2 = Polygon(
            Position(0, 0) to rgb("#000000"),
            Position(0, 2) to rgb("#000000"),
            Position(2, 2) to rgb("#000000"),
            Position(2, 0) to rgb("#000000"),
        )
        polygon2.getPointsInsidePolygon() shouldBe listOf(
            Position(0, 0),
            Position(0, 1),
            Position(0, 2),
            Position(1, 0),
            Position(1, 1),
            Position(1, 2),
            Position(2, 0),
            Position(2, 1),
            Position(2, 2),
        )
        // +------+
        // |...++.|
        // |.++||.|
        // +-+||+-+
        //    ++
        val polygon3 = listOf(
            Position(-2, -3),
            Position(-2, 4),
            Position(1, 4),
            Position(1, 2),
            Position(-1, 2),
            Position(-1, 1),
            Position(2, 1),
            Position(2, 0),
            Position(0, 0),
            Position(0, -1),
            Position(1, -1),
            Position(1, -3),
        ).map { it to rgb("#000000") }.let { Polygon(it) }
        polygon3.getPointsInsidePolygon() shouldContainAll listOf(
            Position(-2, -3),
            Position(-2, -2),
            Position(-2, -1),
            Position(-2, 0),
            Position(-2, 1),
            Position(-2, 2),
            Position(-2, 3),
            Position(-2, 4),
            Position(-1, -3),
            Position(-1, -2),
            Position(-1, -1),
            Position(-1, 0),
            Position(-1, 1),
            Position(-1, 2),
            Position(-1, 3),
            Position(-1, 4),
            Position(0, -3),
            Position(0, -2),
            Position(0, -1),
            Position(0, 0),
            Position(0, 1),
            Position(0, 2),
            Position(0, 3),
            Position(0, 4),
            Position(1, -3),
            Position(1, -2),
            Position(1, -1),
            Position(1, 0),
            Position(1, 1),
            Position(1, 2),
            Position(1, 3),
            Position(1, 4),
            Position(2, 0),
            Position(2, 1),
        )
        polygon3.getPointsInsidePolygon() shouldNotContain Position(2, -3)
    }

}