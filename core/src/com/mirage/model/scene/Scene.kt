package com.mirage.model.scene

import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Player
import java.io.File
import java.util.LinkedList

class Scene {
    var width = 5
    var height = 5
    val objects: LinkedList<SceneObject> = LinkedList()
    var tileMatrix: Array<IntArray> = Array(0) {IntArray(0) {0} }

    /**
     * Загружает сцену из файла с картой и возвращает ссылку на игрока
     */
    fun loadMapFromFile(map: File) : Player{
        tileMatrix = Array(width) {IntArray(height) {0} }
        //TODO
        val player = Player()
        objects.add(player)
        return player
    }
}