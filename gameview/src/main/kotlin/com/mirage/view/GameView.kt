package com.mirage.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference

/**
 * Визуальное представление состояния игры.
 * Данный интерфейс отвечает за отрисовку игровой карты, объектов, анимаций.
 * Данный интерфейс НЕ отвечает за отрисовку UI и работу с пользовательским вводом.
 */
interface GameView {

    /**
     * Загружает необходимые ресурсы для изображения состояния [initialState].
     * Этот метод должен вызываться при получении начального состояния от логики.
     */
    fun loadDrawers(initialState: GameObjects)

    /**
     * Обновляет ресурсы и загружает новые при изменении состояния.
     * Этот метод должен вызываться при переходе на новый [com.mirage.utils.game.states.GameStateSnapshot],
     * либо при получении клиентом [com.mirage.utils.messaging.GameStateUpdateMessage].
     * Этот метод НЕ должен вызываться при каждой перерисовке, т.е. при переходе на интерполированное состояние.
     */
    fun updateDrawers(oldState: GameObjects, diff: StateDifference)

    /**
     * Отрисовывает карту и состояние игры на [batch].
     * Центр камеры всегда должен находиться в точке (0, 0).
     * @param batch Холст, на котором будет отрисовываться состояние.
     * @param objs Состояние, которое будет отрисовано.
     * @param playerPositionOnScene Координаты игрока на сцене; используются для определения центра "камеры" (на самом деле камера не двигается)
     * @param virtualScreenWidth Ширина виртуального экрана
     * @param virtualScreenHeight Высота виртуального экрана
     */
    fun renderGameState(batch: SpriteBatch, objs: GameObjects, playerPositionOnScene: Point, virtualScreenWidth: Int, virtualScreenHeight: Int)


}