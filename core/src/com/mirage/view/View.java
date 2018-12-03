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

import com.mirage.model.scene.Point;
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
    protected final float DEFAULT_SCREEN_WIDTH = 1920;
    protected final float DEFAULT_SCREEN_HEIGHT = 1080;
    /**
     * Размер одного тайла на виртуальном экране
     */
    protected final float TILE_WIDTH = 128;
    protected final float TILE_HEIGHT = 64;

    /**
     * Отступы в пикселях осей координат виртуального экрана от углов тайловой сетки
     * (запас для фона, если игрок подошёл к краю сцены).
     */
    protected final float X_MARGIN = 1500;
    protected final float Y_MARGIN = 1000;

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
    protected float scrW;
    protected float scrH;


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
        //TODO Получить положение экрана из модели
        int scrX = 0;
        int scrY = 0;

        Gdx.gl.glClearColor(0, 1f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //TODO отрисовка

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
    public void setScreenSize(float realWidth, float realHeight) {
        Rectangle viewportSize = calculateViewportSize(realWidth, realHeight);
        scrW = (int) viewportSize.width;
        scrH = (int) viewportSize.height;
        camera.setToOrtho(false, scrW, scrH);
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
     * Отрисовывает тайлы, попадающие в обзор
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @param scene Текущая сцена
     */
    private void drawTiles(float scrX, float scrY, Scene scene) {
        //TODO загружать тайлы из сцены
        int[][] tileMatrix = new int[5][5];
        for (int i = 0; i < 5; ++i)
            for (int j = 0; j < 5; ++j)
                tileMatrix[i][j] = 0;


    }

    /**
     * Переводит координаты точки в базисе сцены в базис виртуального экрана
     * @param modelPoint Точка в базисе сцены
     * @param scene Сцена
     * @return Точка в базисе виртуального экрана
     */
    protected Point getVirtualScreenPoint(Point modelPoint, Scene scene) {
        float x = TILE_WIDTH / 2 * modelPoint.getX() + TILE_WIDTH / 2 * modelPoint.getY() +
                X_MARGIN;
        float y = - TILE_HEIGHT / 2 * modelPoint.getX() + TILE_HEIGHT / 2 * modelPoint.getY() +
                Y_MARGIN + scene.getWidth() * TILE_HEIGHT / 2;
        return new Point(x, y);
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис сцены
     * @param virtialScreenPoint Точка в базисе виртуального экрана
     * @param scene Сцена
     * @return Точка в базисе сцены
     */
    protected Point getScenePoint(Point virtialScreenPoint, Scene scene) {
        float x = virtialScreenPoint.getX() / TILE_WIDTH - virtialScreenPoint.getY() / TILE_HEIGHT -
                X_MARGIN / TILE_WIDTH + Y_MARGIN / TILE_HEIGHT + scene.getWidth() / 2;
        float y = virtialScreenPoint.getX() / TILE_WIDTH + virtialScreenPoint.getY() / TILE_HEIGHT -
                X_MARGIN / TILE_WIDTH - Y_MARGIN / TILE_HEIGHT - scene.getWidth() / 2;
        return new Point(x, y);
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
    protected Rectangle calculateViewportSize(float realWidth, float realHeight) {
        Rectangle tileSize = calculateTileSize(realWidth, realHeight);
        float width = evenRound(realWidth / tileSize.width * TILE_WIDTH);
        float height = evenRound(realHeight / tileSize.height * TILE_HEIGHT);
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
    protected Rectangle calculateTileSize(float realWidth, float realHeight) {
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
    private float visionsSymmetricDifference(float tileH, float realWidth, float realHeight) {
        float w = realWidth / (2 * tileH);
        float h = realHeight / tileH;
        float dw = DEFAULT_SCREEN_WIDTH / TILE_WIDTH;
        float dh = DEFAULT_SCREEN_HEIGHT / TILE_HEIGHT;

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
     * Пустой protected конструктор, нужен только для тестов
     */
    protected View() {}
}
