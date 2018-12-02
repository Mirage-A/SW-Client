package com.mirage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mirage.model.ModelFacade;

public class View {
    // Эталонный размер экрана
    // Все изображения рисуются под этот размер
    // Для других экранов размеры изображений подгоняются так,
    // чтобы различие с эталонным размером экрана было минимально,
    // но при этом чтобы изображения не сплющивались и не растягивались,
    // т.е. отношение ширины и высоты всех изображений сохранялось.
    protected final int DEFAULT_SCREEN_WIDTH = 1920;
    protected final int DEFAULT_SCREEN_HEIGHT = 1080;
    protected final int DEFAULT_TILE_WIDTH = 128;
    protected final int DEFAULT_TILE_HEIGHT = 64;

    private Texture dropImage;
    private Texture bucketImage;
    private SpriteBatch batch;
    public OrthographicCamera camera;
    private ModelFacade model;
    protected int scrW, scrH; // Размер экрана


    public View(ModelFacade model) {
        this.model = model;
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("android/assets/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("android/assets/bucket.png"));


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        batch = new SpriteBatch();

    }


    public void render(Rectangle bucket, Array<Rectangle> raindrops) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for(Rectangle raindrop: raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();

    }

    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        batch.dispose();
    }


    /**
     * Вычисляет размер одного тайла на экране в зависимости от параметров экрана.
     * Размеры тайла подбираются таким образом, чтобы площадь (в тайлах)
     * симметрической разности эталонного и полученного обзоров была минимальна.
     * Учитывается, что отношение ширины и высоты тайла равно 2.
     * @return Оптимальный размер одного тайла (хранится как ширина и высота прямоугольника)
     */
    protected Rectangle calculateTileSize() {
        // Используем тернарный поиск по высоте тайла
        float L = 0f;
        float R = 512f;
        while (R - L > 0.01f) {
            float mid1 = L + (R - L) / 3;
            float mid2 = L + (R - L) * 2 / 3;
            float diff1 = visionsSymmetricDifference(mid1);
            float diff2 = visionsSymmetricDifference(mid2);
            if (diff1 < diff2) {
                R = mid2;
            }
            else {
                L = mid1;
            }
        }
        float tileH = Math.round(L);
        Rectangle res = new Rectangle(0, 0, 2 * tileH, tileH);

        Log.i("Размеры экрана: " + scrW + " x " + scrH + " px");
        Log.i("Размеры тайла: " + res.width + " x " + res.height + " px");
        Log.i("Кол-во тайлов на экране: " + scrW/res.width + " x " + scrH/res.height);

        return res;
    }

    /**
     * Вспомогательная функция, необходимая для тернарного поиска в calculateTileSize.
     * По данной высоте тайла вычисляет площадь симметрической разности эталонного и
     * возможного при данной высоте обзора.
     * @param tileH Высота тайла
     * @return Площадь симметрической разности обзоров
     */
    private float visionsSymmetricDifference(float tileH) {
        float w = scrW / (2 * tileH);
        float h = scrH / tileH;
        float dw = DEFAULT_SCREEN_WIDTH / (float) DEFAULT_TILE_WIDTH;
        float dh = DEFAULT_SCREEN_HEIGHT / (float) DEFAULT_TILE_HEIGHT;

        if (w <= dw && h <= dh) {
            return dw * dh - w * h;
        }
        if (dw <= w && dh <= h) {
            return w * h - dw * dh;
        }
        return Math.abs(w - dw) * Math.min(h, dh) +
                Math.abs(h - dh) * Math.min(w, dw);
    }

    protected View() {}
}
