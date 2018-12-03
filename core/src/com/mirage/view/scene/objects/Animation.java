package com.mirage.view.scene.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Animation extends ObjectDrawer{
    private long startTime;

    @Override
    public void draw(SpriteBatch batch, float x, float y) {
        draw(batch, x, y, System.currentTimeMillis() - startTime);
    }

    public Animation() {
        startTime = System.currentTimeMillis();
    }

    public Animation(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    protected abstract void draw(SpriteBatch batch, float x, float y, long timePassed);
}
