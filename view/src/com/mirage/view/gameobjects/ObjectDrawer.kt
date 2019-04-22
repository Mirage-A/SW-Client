package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Графическое представление объекта. В зависимости от выбора реализации этого интерфейса
 * объект может быть статичным или анимированным (скелетно или покадрово).
 */
interface ObjectDrawer {
    fun draw(batch: SpriteBatch, x: Float, y: Float)

    /**
     * Позволяет сделать представление прозрачным/непрозрачным.
     * [isOpaque] true, если непрозрачно, false, если прозрачно
     */
    fun setOpaque(isOpaque: Boolean) {}
}
