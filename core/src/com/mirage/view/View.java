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
    static final float DEFAULT_SCREEN_WIDTH = 1920;
    static final float DEFAULT_SCREEN_HEIGHT = 1080;
    /**
     * Размер одного тайла на виртуальном экране
     */
    public static final float TILE_WIDTH = 128;
    public static final float TILE_HEIGHT = 64;

    /**
     * Отступы в пикселях осей координат виртуального экрана от углов тайловой сетки
     * (запас для фона, если игрок подошёл к краю сцены).
     */
    public static final float X_MARGIN = 1500;
    public static final float Y_MARGIN = 1000;

    /**
     * Список текстур, используемых на данной сцене (карте)
     */
    private List<Texture> tileTextures;

    private Texture dropImage;
    private Texture bucketImage;
    private SpriteBatch batch;
    public OrthographicCamera camera;
    private ModelFacade model;
    /**
     * Размеры виртуального экрана
     */
    private float scrW;
    private float scrH;


    public View(ModelFacade model) {
        this.model = model;
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("android/assets/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("android/assets/bucket.png"));
        //TODO получить сцену из модели
        loadTileTextures(new Scene());

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        batch = new SpriteBatch();

    }

    /**
     * Реальные размеры экрана при последнем вызове render
     * Если они изменились, то автоматически вызывается setScreenSize
     */
    private float lastRealScreenWidth = 0;
    private float lastRealScreenHeight = 0;
    public void render(Rectangle bucket, Array<Rectangle> raindrops) {
        if (lastRealScreenWidth != Gdx.graphics.getWidth() || lastRealScreenHeight != Gdx.graphics.getHeight()) {
            setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        //TODO получить сцену из модели
        Scene scene = new Scene();
        //TODO Вычислить положение экрана из положения персонажа в сцене
        float scrX = X_MARGIN;
        float scrY = Y_MARGIN;

        Gdx.gl.glClearColor(0, 1f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //TODO отрисовка
        drawTiles(scrX, scrY, scene);

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
    private void setScreenSize(float realWidth, float realHeight) {
        Rectangle viewportSize = ScreenSizeCalculator.calculateViewportSize(realWidth, realHeight);
        scrW = viewportSize.width;
        scrH = viewportSize.height;
        camera.setToOrtho(false, scrW, scrH);
        lastRealScreenWidth = realWidth;
        lastRealScreenHeight = realHeight;
    }

    /**
     * Загружает текстуры тайлов, используемых в данной сцене
     * //TODO
     */
    private void loadTileTextures(Scene scene) {
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
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                //batch.draw(tileTextures.get(0), 0, 0);
                Point scenePoint = new Point(i + 0.5f, j + 0.5f);
                Point virtualScreenPoint = BasisSwitcher.getVirtualScreenPoint(scenePoint, scene);
                Point cameraPoint = BasisSwitcher.getViewportPointFromVirtualScreen(virtualScreenPoint, scrX, scrY);
                batch.draw(tileTextures.get(tileMatrix[i][j]),
                        cameraPoint.getX() - TILE_WIDTH / 2, cameraPoint.getY() - TILE_HEIGHT / 2);
            }
        }

    }

}
