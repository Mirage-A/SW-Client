package com.mirage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mirage.model.ModelFacade;
import com.mirage.model.scene.Scene;

import com.mirage.model.scene.Point;
import com.mirage.model.scene.objects.SceneObject;
import com.mirage.model.scene.objects.entities.Entity;
import com.mirage.model.scene.objects.entities.Player;
import com.mirage.view.scene.objects.ObjectDrawer;
import com.mirage.view.scene.objects.humanoid.AnimatedTexture;
import com.mirage.view.animation.BodyAction;
import com.mirage.view.scene.objects.humanoid.HumanoidDrawer;
import com.mirage.view.animation.LegsAction;
import com.mirage.view.animation.MoveDirection;
import com.mirage.view.scene.objects.humanoid.StaticTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Разница y - координаты между координатами игрока и координатами центра экрана
     * (точка под игроком находится на DELTA_CENTER_Y пикселей ниже центра экрана).
     */
    public static final float DELTA_CENTER_Y = 64;

    /**
     * Список текстур, используемых на данной сцене (карте)
     */
    private List<Texture> tileTextures;

    /**
     * Словарь, где по объекту сцены мы получаем его визуальное представление
     */
    private Map<SceneObject, ObjectDrawer> objectDrawers;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    private ModelFacade model;
    /**
     * Размеры виртуального экрана
     */
    private float scrW;
    private float scrH;

    /**
     * Отображение FPS
     */
    public boolean showFPS = true;
    BitmapFont fpsFont = new BitmapFont();


    public View(ModelFacade model) {
        this.model = model;
        loadTileTextures(model.getScene());
        loadObjectDrawers(model.getScene());

        camera = new OrthographicCamera();
        batch = new SpriteBatch();

    }

    /**
     * Реальные размеры экрана при последнем вызове render
     * Если они изменились, то автоматически вызывается setScreenSize
     */
    private float lastRealScreenWidth = 0;
    private float lastRealScreenHeight = 0;

    /**
     * Отрисовка экрана
     */
    public void render() {
        if (lastRealScreenWidth != Gdx.graphics.getWidth() || lastRealScreenHeight != Gdx.graphics.getHeight()) {
            setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        final Scene scene = model.getScene();
        Point playerPosOnVirtualScreen = BasisSwitcher.getVirtualScreenPoint(model.getPlayerPosition(), scene);
        //TODO Вычислить положение экрана из положения персонажа в сцене
        float scrX = playerPosOnVirtualScreen.getX() - scrW / 2;
        float scrY = playerPosOnVirtualScreen.getY() - scrH / 2 + DELTA_CENTER_Y;

        Gdx.gl.glClearColor(0.25f, 0.25f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //TODO отрисовка
        drawTiles(scrX, scrY, scene);

        drawObjects(scrX, scrY, scene);


        if (showFPS) {
            fpsFont.draw(batch, "" + Gdx.graphics.getFramesPerSecond() + " FPS", 6, 20);
        }

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        for (Texture t : tileTextures) {
            t.dispose();
        }
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
     * Интервал времени, который должен пройти с последней смены направления движения,
     * чтобы изменение отобразилось
     * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
     */
    private final long MOVE_DIRECTION_UPDATE_EPS_TIME = 50L;
    /**
     * Отрисовывает все объекты сцены
     * @param scene Сцена
     */
    private void drawObjects(float scrX, float scrY, final Scene scene) {
        ArrayList<SceneObject> sceneObjects;

        synchronized (scene.getObjects()) {
            sceneObjects = new ArrayList<SceneObject>(scene.getObjects());
        }

        Collections.sort(sceneObjects, new Comparator<SceneObject>() {
            @Override
            public int compare(SceneObject obj1, SceneObject obj2) {
                return -Float.compare(BasisSwitcher.getVirtualScreenPoint(obj1.getPosition(), scene).getY(),
                        BasisSwitcher.getVirtualScreenPoint(obj2.getPosition(), scene).getY());
            }
        });

        for (SceneObject object : sceneObjects) {
            ObjectDrawer drawer = objectDrawers.get(object);
            //TODO Направление движения может влиять не только на HumanoidDrawer
            if (object instanceof Entity && drawer instanceof HumanoidDrawer) {
                HumanoidDrawer hDrawer = ((HumanoidDrawer) drawer);
                MoveDirection updatedMoveDirection = MoveDirection.fromMoveAngle(((Entity) object).getMoveAngle());
                if (updatedMoveDirection != hDrawer.getBufferedMoveDirection()) {
                    hDrawer.setLastMoveDirectionUpdateTime(System.currentTimeMillis());
                    hDrawer.setBufferedMoveDirection(updatedMoveDirection);
                }
                else if (System.currentTimeMillis() - hDrawer.getLastMoveDirectionUpdateTime() > MOVE_DIRECTION_UPDATE_EPS_TIME) {
                    hDrawer.setMoveDirection(hDrawer.getBufferedMoveDirection());
                }

                if (((Entity) object).isMoving()) {
                    hDrawer.setBodyAction(BodyAction.RUNNING);
                    hDrawer.setLegsAction(LegsAction.RUNNING);
                }
                else {
                    hDrawer.setBodyAction(BodyAction.IDLE);
                    hDrawer.setLegsAction(LegsAction.IDLE);
                }
            }
            drawer.draw(batch, BasisSwitcher.getViewportPointFromScene(model.getPlayerPosition(), scene, scrX, scrY).getX(),
                    BasisSwitcher.getViewportPointFromScene(model.getPlayerPosition(), scene, scrX, scrY).getY());
        }

    }

    /**
     * Загружает текстуры тайлов, используемых в данной сцене
     * //TODO
     */
    private void loadTileTextures(Scene scene) {
        tileTextures = new ArrayList<Texture>();
        tileTextures.add(TextureLoader.load("tiles/0000.png"));
    }

    /**
     * Загружает objectDrawers для объектов сцены
     */
    private void loadObjectDrawers(Scene scene) {
        objectDrawers = new HashMap<SceneObject, ObjectDrawer>();
        for(SceneObject object : scene.getObjects()) {
           addObjectDrawer(object);
        }
    }

    /**
     * Добавляет objectDrawer данного объекта сцены в словарь
     * //TODO
     */
    private void addObjectDrawer(SceneObject object) {
        if (object instanceof Player) {
            Player player = (Player) object;
            objectDrawers.put(player, new HumanoidDrawer(loadPlayerTexturesMap(player), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(player.getMoveAngle()), player.getWeaponType()));
        }
    }

    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private Map<String, AnimatedTexture> loadPlayerTexturesMap(Player player) {
        Map<String, AnimatedTexture> texturesMap = new HashMap<String, AnimatedTexture>();
        for (MoveDirection md : MoveDirection.values()) {
            texturesMap.put("head" + md.toString(), new StaticTexture(TextureLoader.load("equipment/head/0000" + md.toString() + ".png")));
        }
        texturesMap.put("body", new StaticTexture(TextureLoader.load("equipment/body/0000.png")));
        texturesMap.put("handtop", new StaticTexture(TextureLoader.load("equipment/handtop/0000.png")));
        texturesMap.put("handbottom", new StaticTexture(TextureLoader.load("equipment/handbottom/0000.png")));
        texturesMap.put("legtop", new StaticTexture(TextureLoader.load("equipment/legtop/0000.png")));
        texturesMap.put("legbottom", new StaticTexture(TextureLoader.load("equipment/legbottom/0000.png")));
        texturesMap.put("cloak", new StaticTexture(TextureLoader.load("equipment/cloak/0000.png")));
        texturesMap.put("neck", new StaticTexture(TextureLoader.load("equipment/neck/0000.png")));
        texturesMap.put("weapon1", new StaticTexture(TextureLoader.load("equipment/onehanded/0000.png")));
        texturesMap.put("weapon2", new StaticTexture(TextureLoader.load("equipment/onehanded/0000.png")));
        return texturesMap;
    }

    /**
     * Отрисовывает тайлы, попадающие в обзор
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @param scene Текущая сцена
     */
    private void drawTiles(float scrX, float scrY, Scene scene) {
        int[][] tileMatrix = scene.getTileMatrix();
        for (int i = 0; i < scene.getWidth(); ++i) {
            for (int j = 0; j < scene.getHeight(); ++j) {
                Point scenePoint = new Point(i + 0.5f, j + 0.5f);
                Point cameraPoint = BasisSwitcher.getViewportPointFromScene(scenePoint, scene, scrX, scrY);
                batch.draw(tileTextures.get(tileMatrix[i][j]),
                        cameraPoint.getX() - TILE_WIDTH / 2, cameraPoint.getY() - TILE_HEIGHT / 2);
            }
        }
    }

}
