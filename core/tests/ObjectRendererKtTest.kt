import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.position
import com.mirage.model.extensions.rectangle
import com.mirage.view.game.isOpaque
import org.junit.jupiter.api.Test

internal class ObjectRendererKtTest {

    private fun createObject(rect: Rectangle, props: Map<String, Any?> = mapOf()) : MapObject =
        MapObject().apply {
            rectangle = rect
            for((key, value) in props) {
                properties.put(key, value)
            }
        }

    @Test
    fun renderObjects() {

    }

    @Test
    fun isOpaque() {
        val map = TiledMap()
        val layer = MapLayer()
        map.layers.add(layer)
        val bld = createObject(Rectangle(0f, 0f, 2f, 1f), mapOf("type" to "building", "tp-range" to 2f))
        val ent1 = createObject(Rectangle(-1f, -1f, 0.5f, 0.5f), mapOf("type" to "entity"))
        layer.objects.add(bld)
        layer.objects.add(ent1)
        assert(isOpaque(bld, map))
        ent1.position = Point(1.5f, 1.5f)
        assert(!isOpaque(bld, map))
        ent1.position = Point(0.5f, 0.5f)
        assert(!isOpaque(bld, map))
        ent1.position = Point(2f, 1f)
        assert(isOpaque(bld, map))
        ent1.position = Point(1.5f, 1f)
        assert(!isOpaque(bld, map))
        ent1.position = Point(-2f, 3f)
        assert(isOpaque(bld, map))
        ent1.position = Point(-1f, 2f)
        assert(!isOpaque(bld, map))
    }

    @Test
    fun depthSort() {
    }

    @Test
    fun compare() {
    }

    @Test
    fun compareDisjoint() {
    }

    @Test
    fun compareEntityAndBuilding() {
    }

    @Test
    fun compare1() {
    }
}