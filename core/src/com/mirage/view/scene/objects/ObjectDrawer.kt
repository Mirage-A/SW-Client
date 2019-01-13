package com.mirage.view.scene.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Графическое представление объекта. В зависимости от выбора реализации этого абстрактного класса
 * объект может быть статичным или анимированным (скелетно или покадрово).
 */
abstract class ObjectDrawer {
    abstract fun draw(batch: SpriteBatch, x: Float, y: Float)
}
