package com.mirage.view.scene.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Объект без анимации, заданный статичной текстурой
 */
public class StaticTexture extends ObjectDrawer{
    private Texture texture;

    public StaticTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x, y);
    }
}
