import com.badlogic.gdx.math.Rectangle;
import com.mirage.model.scene.Point;
import com.mirage.model.scene.Scene;
import com.mirage.view.Log;
import com.mirage.view.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest extends View {
    @Test
    void testScreenScaling() {
        assertEquals(new Rectangle(0, 0, 128, 64), calculateTileSize(1920, 1080));
        assertEquals(new Rectangle(0, 0, 1920, 1080), calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 64, 32), calculateTileSize(960, 540));
        assertEquals(new Rectangle(0, 0, 1920, 1080), calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 108, 54), calculateTileSize(1600, 900));
        assertEquals(new Rectangle(0, 0, 1896, 1066), calculateViewportSize(1600, 900));

        assertEquals(new Rectangle(0, 0, 36, 18), calculateTileSize(480, 320));
        assertEquals(new Rectangle(0, 0, 1706, 1138), calculateViewportSize(480, 320));
    }

    @Test
    void testBasisSwitching() {
        Scene scene = new Scene();
        assertEquals(new Point(X_MARGIN, Y_MARGIN + scene.getWidth() * TILE_HEIGHT / 2),
                getVirtualScreenPoint(new Point(0, 0), scene));
        Log.d(new Point(0, 0));
        Log.d(getVirtualScreenPoint(new Point(0, 0), scene));
        Log.d(getScenePoint(getVirtualScreenPoint(new Point(0, 0), scene), scene));
        assertEquals(new Point(0, 0), getScenePoint(getVirtualScreenPoint(new Point(0, 0), scene), scene));
        Log.d(new Point(0, 0));
        Log.d(getScenePoint(new Point(0, 0), scene));
        Log.d(getVirtualScreenPoint(getScenePoint(new Point(0, 0), scene), scene));
        assertEquals(new Point(0, 0), getVirtualScreenPoint(getScenePoint(new Point(0, 0), scene), scene));
    }
}
