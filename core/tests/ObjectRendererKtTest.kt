import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.position
import com.mirage.model.extensions.rectangle
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