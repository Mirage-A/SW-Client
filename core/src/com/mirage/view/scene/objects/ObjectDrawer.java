package com.mirage.view.scene.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ObjectDrawer {
    public abstract void draw(SpriteBatch batch, float x, float y);

    public long getStartTime() {
        return System.currentTimeMillis();
    }
}
