package com.mirage.view.scene.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class SpriteAnimation extends Animation{

    @Override
    protected void draw(SpriteBatch batch, float x, float y, long timePassed) {
        batch.draw(loadSprite(timePassed), x, y);
    }

    protected abstract Texture loadSprite(long timePassed);
}
