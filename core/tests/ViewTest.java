import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.mirage.view.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTest extends View {
    @Test
    public void test() {
        System.out.println("Test!");

        assertEquals(new Rectangle(0, 0, 128, 64), calculateTileSize(1920, 1080));
        assertEquals(new Rectangle(0, 0, 1920, 1080), calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 64, 32), calculateTileSize(960, 540));
        assertEquals(new Rectangle(0, 0, 1920, 1080), calculateViewportSize(1920, 1080));

        assertEquals(new Rectangle(0, 0, 108, 54), calculateTileSize(1600, 900));
        assertEquals(new Rectangle(0, 0, 1896, 1066), calculateViewportSize(1600, 900));

        assertEquals(new Rectangle(0, 0, 36, 18), calculateTileSize(480, 320));
        assertEquals(new Rectangle(0, 0, 1706, 1138), calculateViewportSize(480, 320));
    }
}
