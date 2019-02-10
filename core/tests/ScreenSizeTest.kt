import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point
import com.mirage.model.scene.Scene
import com.mirage.view.BasisSwitcher
import com.mirage.view.Log
import com.mirage.view.ScreenSizeCalculator
import com.mirage.view.screens.View
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class ScreenSizeTest {
    @Test
    fun testScreenScaling() {
        assertEquals(Rectangle(0f, 0f, 128f, 64f),
                ScreenSizeCalculator.calculateTileSize(1920f, 1080f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                ScreenSizeCalculator.calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 64f, 32f),
                ScreenSizeCalculator.calculateTileSize(960f, 540f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                ScreenSizeCalculator.calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 108f, 54f),
                ScreenSizeCalculator.calculateTileSize(1600f, 900f))
        assertEquals(Rectangle(0f, 0f, 1896f, 1066f),
                ScreenSizeCalculator.calculateViewportSize(1600f, 900f))

        assertEquals(Rectangle(0f, 0f, 36f, 18f),
                ScreenSizeCalculator.calculateTileSize(480f, 320f))
        assertEquals(Rectangle(0f, 0f, 1706f, 1138f),
                ScreenSizeCalculator.calculateViewportSize(480f, 320f))
    }

    @Test
    fun testBasisSwitching() {
        val scene = Scene()
        assertEquals(Point(View.X_MARGIN, View.Y_MARGIN + scene.width * View.TILE_HEIGHT / 2),
                BasisSwitcher.getVirtualScreenPointFromScene(Point(0f, 0f), scene))
        Log.d(Point(0f, 0f))
        Log.d(BasisSwitcher.getVirtualScreenPointFromScene(Point(0f, 0f), scene))
        Log.d(BasisSwitcher.getScenePointFromVirtualScreen(BasisSwitcher.getVirtualScreenPointFromScene(Point(0f, 0f), scene), scene))
        assertEquals(Point(0f, 0f), BasisSwitcher.getScenePointFromVirtualScreen(BasisSwitcher.getVirtualScreenPointFromScene(Point(0f, 0f), scene), scene))
        Log.d(Point(0f, 0f))
        Log.d(BasisSwitcher.getScenePointFromVirtualScreen(Point(0f, 0f), scene))
        Log.d(BasisSwitcher.getVirtualScreenPointFromScene(BasisSwitcher.getScenePointFromVirtualScreen(Point(0f, 0f), scene), scene))
        assertEquals(Point(0f, 0f), BasisSwitcher.getVirtualScreenPointFromScene(BasisSwitcher.getScenePointFromVirtualScreen(Point(0f, 0f), scene), scene))
    }
}
