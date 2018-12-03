package com.mirage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mirage.model.ModelFacade;
import com.mirage.model.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class View {
    /**
     * Эталонный размер экрана
     * Все изображения рисуются под этот размер
     * Для других экранов размеры изображений подгоняются так,
     * чтобы различие с эталонным размером экрана было минимально,
     * но при этом чтобы изображения не сплющивались и не растягивались,
     * т.е. отношение ширины и высоты всех изображений сохранялось.
     */
    private final int DEFAULT_SCREEN_WIDTH = 1920;
    private final int DEFAULT_SCREEN_HEIGHT = 1080;
    /**
     * Размер одного тайла на виртуальном экране
     */
    private final int TILE_WIDTH = 128;
    private final int TILE_HEIGHT = 64;

    /**
     * Список текстур, используемых на данной сцене (карте)
     */
    protected List<Texture> tileTextures;

    private Texture dropImage;
    private Texture bucketImage;
    private SpriteBatch batch;
    public OrthographicCamera camera;
    private ModelFacade model;
    /**
     * Размеры виртуального экрана
     */
    protected int scrW;
    protected int scrH;


    public View(ModelFacade model) {
        this.model = model;
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("android/assets/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("android/assets/bucket.png"));


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
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
     * Метод, который должен вызываться при изменении параметров реального экрана.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     */
    public void setScreenSize(int realWidth, int realHeight) {
        Rectangle viewportSize = calculateViewportSize(realWidth, realHeight);
        scrW = (int) viewportSize.width;
        scrH = (int) viewportSize.height;
        camera.setToOrtho(false, scrW, scrH);
    }

    /**
     * Вычисляет оптимальный размер виртуального экрана так,
     * чтобы размер тайлов после скейла под экран был равен вычисленному
     * с помощью calculateTileSize,
     * при этом гарантируется, что и ширина, и высота виртуального экрана обязательно четные.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Оптимальный размер виртуального экрана
     */
    protected Rectangle calculateViewportSize(int realWidth, int realHeight) {
        Rectangle tileSize = calculateTileSize(realWidth, realHeight);
        int width = evenRound(realWidth / tileSize.width * TILE_WIDTH);
        int height = evenRound(realHeight / tileSize.height * TILE_HEIGHT);
        Log.i("Размеры виртуального экрана: " + width + " x " + height + " px");
        return new Rectangle(0, 0, width, height);
    }

    /**
     * Вычисляет размер одного тайла на экране в зависимости от параметров экрана.
     * Размеры тайла подбираются таким образом, чтобы площадь (в тайлах)
     * симметрической разности эталонного и полученного обзоров была минимальна.
     * Учитывается, что отношение ширины и высоты тайла равно 2,
     * при этом гарантируется, что и ширина, и высота обязательно четные.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Оптимальный размер одного тайла (хранится как ширина и высота прямоугольника)
     */
    protected Rectangle calculateTileSize(int realWidth, int realHeight) {
        // Используем тернарный поиск по высоте тайла
        float L = 0f;
        float R = 512f;
        while (R - L > 0.01f) {
            float mid1 = L + (R - L) / 3;
            float mid2 = L + (R - L) * 2 / 3;
            float diff1 = visionsSymmetricDifference(mid1, realWidth, realHeight);
            float diff2 = visionsSymmetricDifference(mid2, realWidth, realHeight);
            if (diff1 < diff2) {
                R = mid2;
            }
            else {
                L = mid1;
            }
        }
        int tileH = evenRound(L);
        Rectangle res = new Rectangle(0, 0, 2 * tileH, tileH);

        Log.i("Размеры экрана: " + realWidth + " x " + realHeight + " px");
        Log.i("Размеры тайла: " + res.width + " x " + res.height + " px");
        Log.i("Кол-во тайлов на экране: " + realWidth/res.width + " x " + realHeight/res.height);

        return res;
    }

    /**
     * Вспомогательная функция, необходимая для тернарного поиска в calculateTileSize.
     * По данной высоте тайла вычисляет площадь симметрической разности эталонного и
     * возможного при данной высоте обзора.
     * @param tileH Высота тайла
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Площадь симметрической разности обзоров
     */
    private float visionsSymmetricDifference(float tileH, int realWidth, int realHeight) {
        float w = realWidth / (2 * tileH);
        float h = realHeight / tileH;
        float dw = DEFAULT_SCREEN_WIDTH / (float) TILE_WIDTH;
        float dh = DEFAULT_SCREEN_HEIGHT / (float) TILE_HEIGHT;

        if (w <= dw && h <= dh) {
            return dw * dh - w * h;
        }
        if (dw <= w && dh <= h) {
            return w * h - dw * dh;
        }
        return Math.abs(w - dw) * Math.min(h, dh) +
                Math.abs(h - dh) * Math.min(w, dw);
    }

    /**
     * Вспомогательная функция, необходимая в calculateTileSize.
     * Округляет число a до ближайшего четного целого числа.
     * Если a - целое нечетное, то округление вверх.
     * @param a Число
     * @return Ближайшее четное целое число
     */
    private int evenRound(float a) {
        int res = Math.round(a);
        if (res % 2 == 0) {
            return res;
        }
        if (a < res) {
            return res - 1;
        }
        return res + 1;
    }

    /**
     * Загружает текстуры тайлов, используемых в данной сцене
     * //TODO
     */
    protected void loadTileTextures(Scene scene) {
        tileTextures = new ArrayList<Texture>();
        tileTextures.add(new Texture(Gdx.files.internal("android/assets/tiles/0000.png")));
    }

    /**
     * Пустой protected конструктор, нужен только для тестов
     */
    protected View() {}
}
