package com.mirage.utils.virtualscreen

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Интерфейс, представляющий собой виртуальный экран и позволяющий отрисовывать на нём текстуры.
 * Должен использоваться только внутри потока отрисовки экрана.
 * Текстуры хранятся внутри реализации этого интерфейса - код, работающий с этим интерфейсом, хранит только названия текстур,
 * и не имеет прямого доступа к самим текстурам.
 * Название текстуры является путём к ней относительно папки drawable и не содержит разрешения .png,
 * т.е. путь текстура с названием NAME будет загружена из файла assets/drawable/NAME.png.
 * Центр виртуального экрана всегда находится в точке (0, 0).
 * Ось x направлена вправо, ось y направлена вверх.
 * При изменении размеров реального экрана следует вызвать resize(), чтобы обновить размеры виртуального экрана.
 */
interface VirtualScreen {

    /**
     * Размеры виртуального экрана
     */
    val width: Float
    val height: Float

    /**
     * Начать отрисовку кадра
     */
    fun begin()

    /**
     * Завершить отрисовку кадра
     */
    fun end()

    /**
     * Обновляет размеры виртуального экрана при изменении размеров реального экрана.
     * Размеры виртуального экрана отличаются от размеров реального, но сохраняют отношение ширины к высоте.
     */
    fun resize(newRealWidth: Int, newRealHeight: Int)

    /**
     * Загружает все текстуры с именами [textureNames] и кэширует их.
     */
    fun loadAllTextures(textureNames: Iterable<String>)

    /**
     * Освобождает все ресурсы, занятые кэшированными текстурами, и очищает кэш.
     */
    fun clearCache()

    /**
     * Загружает пакет тайлов [tileSetName] для дальнейшего использования.
     */
    fun setTileSet(tileSetName: String)

    /**
     * Отрисовывает тайл [tileID] из текущего пакета тайлов.
     * Центр текстуры тайла находится в точке (x, y) виртуального экрана.
     */
    fun drawTile(tileID: Int, x: Float, y: Float)

    /**
     * Стандартный метод отрисовки текстуры с центром в точке (x, y).
     */
    fun draw(textureName: String, x: Float, y: Float)

    /**
     * Этот метод нацелен на отрисовку слоёв анимации из редактора.
     * @param textureName Название текстуры
     * @param x Координата центра текстуры на виртуальном экране.
     * @param y Координата центра текстуры на виртуальном экране.
     * @param basicWidth Обычный размер текстуры
     * @param basicHeight Обычный размер текстуры
     * @param scale Коэффициент увеличения всей текстуры
     * @param scaleX Коэффициент увеличения ширины текстуры
     * @param scaleY Коэффициент увеличения высоты текстуры
     * @param angle Угол поворота текстуры
     */
    fun draw(textureName: String, x: Float, y: Float, basicWidth: Float, basicHeight: Float, scale: Float, scaleX: Float, scaleY: Float, angle: Float)

    /**
     * Этот метод является обёрткой аналогичного метода класса [SpriteBatch].
     * Этот метод не рекомендуется использовать.
     * Здесь x, y уже не совпадают с центром текстуры.
     */
    fun draw(textureName: String, x: Float, y: Float, originX: Float, originY: Float, width: Float, height: Float, scaleX: Float, scaleY: Float, rotation: Float, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, flipX: Boolean, flipY: Boolean)

}