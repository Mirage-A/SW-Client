import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point
import com.mirage.view.Log
import com.mirage.view.game.calculateTileSize
import com.mirage.view.game.calculateViewportSize
import com.mirage.view.game.getScenePointFromVirtualScreen
import com.mirage.view.game.getVirtualScreenPointFromScene
import com.mirage.view.screens.GameScreen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ScreenSizeTest {
    @Test
    fun testScreenScaling() {
        assertEquals(Rectangle(0f, 0f, 128f, 64f),
                calculateTileSize(1920f, 1080f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 64f, 32f),
                calculateTileSize(960f, 540f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 108f, 54f),
                calculateTileSize(1600f, 900f))
        assertEquals(Rectangle(0f, 0f, 1896f, 1066f),
                calculateViewportSize(1600f, 900f))

        assertEquals(Rectangle(0f, 0f, 36f, 18f),
                calculateTileSize(480f, 320f))
        assertEquals(Rectangle(0f, 0f, 1706f, 1138f),
                calculateViewportSize(480f, 320f))
    }

    @Test
    fun testBasisSwitching() {
        assertEquals(Point(0f, GameScreen.TILE_HEIGHT / 2),
                getVirtualScreenPointFromScene(Point(0f, 0f)))
        Log.d(Point(0f, 0f))
        Log.d(getVirtualScreenPointFromScene(Point(0f, 0f)))
        Log.d(getScenePointFromVirtualScreen(getVirtualScreenPointFromScene(Point(0f, 0f))))
        assertEquals(Point(0f, 0f), getScenePointFromVirtualScreen(getVirtualScreenPointFromScene(Point(0f, 0f))))
        Log.d(Point(0f, 0f))
        Log.d(getScenePointFromVirtualScreen(Point(0f, 0f)))
        Log.d(getVirtualScreenPointFromScene(getScenePointFromVirtualScreen(Point(0f, 0f))))
        assertEquals(Point(0f, 0f), getVirtualScreenPointFromScene(getScenePointFromVirtualScreen(Point(0f, 0f))))
    }
}
