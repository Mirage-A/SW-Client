package com.mirage.view.scene.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Графическое представление объекта. В зависимости от выбора реализации этого абстрактного класса
 * объект может быть статичным или анимированным (скелетно или покадрово).
 */
public abstract class ObjectDrawer {
    public abstract void draw(SpriteBatch batch, float x, float y);

    public long getStartTime() {
        return System.currentTimeMillis();
    }
}
