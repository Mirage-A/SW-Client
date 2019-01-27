import com.mirage.model.datastructures.DisjointSetUnion
import com.mirage.model.scene.Point
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

internal class DSUTest {
    @Test
    fun testFloats() {
        val dsu = DisjointSetUnion<Float>()
        assertEquals(0f, dsu.findRoot(0f))
        dsu.makeSet(0f)
        dsu.makeSet(1f)
        dsu.makeSet(2f)
        assertEquals(0f, dsu.findRoot(0f))
        assertEquals(1f, dsu.findRoot(1f))
        assertEquals(2f, dsu.findRoot(2f))
        dsu.unite(-1f, 0f)
        dsu.unite(0f, 1f)
        assertEquals(0f, dsu.findRoot(0f))
        assertEquals(0f, dsu.findRoot(1f))
        dsu.unite(1f, 2f)
        assertEquals(0f, dsu.findRoot(2f))
    }

    @Test
    fun testPoints() {
        val dsu = DisjointSetUnion<Point>()
        assertEquals(Point(0f ,0f), dsu.findRoot(Point(0f,0f)))
        dsu.makeSet(Point(0f, 0f))
        dsu.makeSet(Point(1f, 1f))
        dsu.makeSet(Point(2f, 2f))
        assertEquals(Point(0f ,0f), dsu.findRoot(Point(0f, 0f)))
        assertEquals(Point(1f ,1f), dsu.findRoot(Point(1f, 1f)))
        assertEquals(Point(2f ,2f), dsu.findRoot(Point(2f, 2f)))
        dsu.unite(Point(-1f, -1f), Point(0f, 0f))
        dsu.unite(Point(0f ,0f), Point(1f, 1f))
        assertEquals(Point(0f ,0f), dsu.findRoot(Point(0f ,0f)))
        assertEquals(Point(0f ,0f), dsu.findRoot(Point(1f, 1f)))
        dsu.unite(Point(1f, 1f), Point(2f,2f))
        assertEquals(Point(0f ,0f), dsu.findRoot(Point(2f, 2f)))
    }
}
