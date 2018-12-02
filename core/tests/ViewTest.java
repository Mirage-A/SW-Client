import com.badlogic.gdx.math.Rectangle;
import com.mirage.view.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTest extends View {
    @Test
    public void test() {
        System.out.println("Test!");
        scrW = 1920;
        scrH = 1080;
        assertEquals(new Rectangle(0, 0, 128, 64), calculateTileSize());
        scrW = 960;
        scrH = 540;
        assertEquals(new Rectangle(0, 0, 64, 32), calculateTileSize());
        scrW = 1600;
        scrH = 900;
        assertEquals(new Rectangle(0, 0, 106, 53), calculateTileSize());
        scrW = 480;
        scrH = 320;
        assertEquals(new Rectangle(0, 0, 38, 19), calculateTileSize());
    }
}
