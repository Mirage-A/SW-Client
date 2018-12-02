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
    private Texture dropImage;
    private Texture bucketImage;
    private SpriteBatch batch;
    public OrthographicCamera camera;
    private ModelFacade model;

    /**
     * Вычисляет размер одного тайла на экране в зависимости от параметров экрана
     * Размеры тайла подбираются таким образом, чтобы сумма
     * (tilesInWidth - defaultTilesInHeight) ^ 2 + (tilesInHeight - defaultTilesInHeight) ^ 2
     * была минимальной (tilesInHeight - кол-во тайлов в высоту, т.е scrH / tileH),
     * тогда разность обзора на данном экране и на стандартном экране минимальна.
     * Учитывается, что отношение ширины и высоты тайла равно 2
     * //TODO Можно доделать так, чтобы минимизировать лишние площади, а не длины
     * //TODO (чтобы площадь симметрической разности двух прямоугольников была минимальна)
     * @param scrW Ширина экрана
     * @param scrH Высота экрана
     * @return Оптимальный размер одного тайла (хранится как ширина и высота прямоугольника)
     */
    public Rectangle calculateTileSize(int scrW, int scrH) {
        float tileH = Math.round(2 * (scrW * scrW + 4 * scrH * scrH) / (float) (15 * (4 * scrW + 9 * scrH)));
        Rectangle res = new Rectangle(0, 0, 2 * tileH, tileH);
        System.out.println("Размеры экрана: " + scrW + " x " + scrH + " px");
        System.out.println("Размеры тайла: " + res.width + " x " + res.height + " px");
        System.out.println("Кол-во тайлов на экране: " + scrW/res.width + " x " + scrH/res.height);
        return res;
    }

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

    public View() {}


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
}
