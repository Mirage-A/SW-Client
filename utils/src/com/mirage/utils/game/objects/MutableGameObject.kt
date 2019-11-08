package com.mirage.utils.game.objects

/**
 * Изменяемый объект карты.
 * Используется в однопоточной среде во время обработки изменений состояния игры за тик игровой логики.
 */
class MutableGameObject(
        var name: String,
        var template: String,
        var type: GameObject.Type,
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float,
        var isRigid: Boolean,
        var speed: Float,
        var moveDirection: GameObject.MoveDirection,
        var isMoving: Boolean,
        var transparencyRange: Float,
        var state: String
) {

    /**
     * Создаёт неизменяемую копию этого объекта.
     */
    fun saveChanges() : GameObject =
            GameObject(name, template, type, x, y, width, height, isRigid, speed, moveDirection, isMoving, transparencyRange, state)

}
