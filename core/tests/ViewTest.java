import com.badlogic.gdx.math.Rectangle;
import com.mirage.model.scene.Point;
import com.mirage.model.scene.Scene;
import com.mirage.view.BasisSwitcher;
import com.mirage.view.Log;
import com.mirage.view.ScreenSizeCalculator;
import com.mirage.view.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {
    @Test
    void testScreenScaling() {
        assertEquals(new Rectangle(0, 0, 128, 64),
                ScreenSizeCalculator.calculateTileSize(1920, 1080));
        assertEquals(new Rectangle(0, 0, 1920, 1080),
                ScreenSizeCalculator.calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 64, 32),
                ScreenSizeCalculator.calculateTileSize(960, 540));
        assertEquals(new Rectangle(0, 0, 1920, 1080),
                ScreenSizeCalculator.calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 108, 54),
                ScreenSizeCalculator.calculateTileSize(1600, 900));
        assertEquals(new Rectangle(0, 0, 1896, 1066),
                ScreenSizeCalculator.calculateViewportSize(1600, 900));

        assertEquals(new Rectangle(0, 0, 36, 18),
                ScreenSizeCalculator.calculateTileSize(480, 320));
        assertEquals(new Rectangle(0, 0, 1706, 1138),
                ScreenSizeCalculator.calculateViewportSize(480, 320));
    }

    @Test
    void testBasisSwitching() {
        Scene scene = new Scene();
        assertEquals(new Point(View.X_MARGIN, View.Y_MARGIN + scene.getWidth() * View.TILE_HEIGHT / 2),
                BasisSwitcher.getVirtualScreenPoint(new Point(0, 0), scene));
        Log.d(new Point(0, 0));
        Log.d(BasisSwitcher.getVirtualScreenPoint(new Point(0, 0), scene));
        Log.d(BasisSwitcher.getScenePoint(BasisSwitcher.getVirtualScreenPoint(new Point(0, 0), scene), scene));
        assertEquals(new Point(0, 0), BasisSwitcher.getScenePoint(BasisSwitcher.getVirtualScreenPoint(new Point(0, 0), scene), scene));
        Log.d(new Point(0, 0));
        Log.d(BasisSwitcher.getScenePoint(new Point(0, 0), scene));
        Log.d(BasisSwitcher.getVirtualScreenPoint(BasisSwitcher.getScenePoint(new Point(0, 0), scene), scene));
        assertEquals(new Point(0, 0), BasisSwitcher.getVirtualScreenPoint(BasisSwitcher.getScenePoint(new Point(0, 0), scene), scene));
    }
}
