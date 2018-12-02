import com.badlogic.gdx.math.Rectangle;
import com.mirage.model.ModelFacade;
import com.mirage.view.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTest {
    @Test
    public void test() {
        System.out.println("Test!");
        View view = new View();
        assertEquals(new Rectangle(0, 0, 128, 64), view.calculateTileSize(1920, 1080));
        assertEquals(new Rectangle(0, 0, 64, 32), view.calculateTileSize(960, 540));
        assertEquals(new Rectangle(0, 0, 106, 53), view.calculateTileSize(1600, 900));
        assertEquals(new Rectangle(0, 0, 36, 18), view.calculateTileSize(480, 320));
    }
}
