package com.mirage.gamelogic.scripting

import com.mirage.utils.game.oldobjects.GameObject
import org.luaj.vm2.LuaTable

/**
 * Интерфейс, который определяет методы, которыми может пользоваться вызываемый скрипт.
 * Этот интерфейс должен быть реализован в другом модуле.
 */
interface LogicScriptActions {

    /**
     * Вызывает другой скрипт внутри логики
     */
    fun runLogicScript(scriptName: String, args: LuaTable)

    /**
     * Отправляет сообщение о запуске скрипта с заданным именем и аргументами всем игрокам
     */
    fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable)

    /**
     * Находит объект по имени на карте.
     * Объект внутри скрипта изменять нельзя.
     */
    fun findObject(objName: String) : GameObject?

    /**
     * Находит все объекты с заданным именем.
     * Внутри скрипта эти объекты изменять нельзя.
     */
    fun findAllObjects(objName: String) : LuaTable

    /**
     * Находит все объекты - персонажи игроков.
     * Внутри скрипта эти объекты изменять нельзя.
     */
    fun findAllPlayers() : LuaTable

    //TODO Возможность получить изменяемый объект, чтобы как-то поменять его.

    /**
     * Логирует объект.
     */
    fun print(msg: Any?)
}