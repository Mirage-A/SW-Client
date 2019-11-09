package com.mirage.view.objectdrawers

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.Log
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference
import com.mirage.view.utils.TextureLoader

/**
 * Класс, хранящий визуальные представления объектов и позволяющий получать их по ID объекта.
 */
class Drawers {

    private val drawers : MutableMap<Long, ObjectDrawer?> = HashMap()

    /**
     * Обновляет словарь [drawers], изменяя визуальные представления объектов, измененных за тик.
     * Этот метод следует вызывать при переходе [com.mirage.utils.game.states.SnapshotManager] на новое состояние.
     * @see SnapshotManager
     * //TODO
     */
    fun updateDrawers(oldState: GameObjects, diff: StateDifference) {
        for ((id, obj) in diff.newObjects) {
            drawers[id] = loadDrawer(obj)
        }
        for ((id, newObj) in diff.changedObjects) {
            val originDrawer = drawers[id]
            if (originDrawer == null) {
                Log.e("ERROR (Drawers::updateDrawers): initial drawer not found")
            }
            else {
                drawers[id] = updateDrawer(originDrawer, oldState[id], newObj)
            }
        }
        for (id in diff.removedObjects) {
            //TODO drawers[id].dispose()
        }
    }

    /**
     * Возвращает новое представление объекта в зависимости от изменений за тик.
     * //TODO
     */
    private fun updateDrawer(originDrawer: ObjectDrawer, oldObj: GameObject?, newObj: GameObject) : ObjectDrawer {
        return originDrawer
    }

    /**
     * Полностью загружает представление объекта [obj] и возвращает его.
     */
    private fun loadDrawer(obj: GameObject) : ObjectDrawer {
        return TestObjectFiller(obj)
        /*
        drawers[id] = when {
            obj.name == "player" -> HumanoidAnimation(loadPlayerTexturesMap(obj), BodyAction.IDLE, LegsAction.IDLE, obj.moveDirection, WeaponType.UNARMED)
            obj.properties.containsKey("animation") -> ObjectAnimation(obj.properties.getString("animation", "MAIN_GATE_OPEN"))
            obj.properties.containsKey("texture") -> TextureLoader.getStaticTexture("objects/" + obj.properties.getString("texture", "null.png"), Image.Alignment.CENTER)
            else -> {if (SHOW_INVISIBLE_OBJECTS_MODE) TestObjectFiller(obj, camera) else null}
        }
        transparentDrawers[obj] = when {
            obj.properties.containsKey("animation-tp") -> ObjectAnimation(obj.properties.getString("animation-tp", "MAIN_GATE_OPEN"))
            obj.properties.containsKey("texture-tp") -> TextureLoader.getStaticTexture("objects/" + obj.properties.getString("texture-tp", "null.png"), Image.Alignment.CENTER)
            else -> this[obj, true]
        }
        */
    }

    /**
     * Полностью загружает визуальные представления всех объектов.
     */
    fun loadDrawers(objs: GameObjects) {
        for ((id, obj) in objs) {
            drawers[id] = loadDrawer(obj)
        }
    }

    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private fun loadPlayerTexturesMap(obj: MapObject): MutableMap<String, Image> {
        val texturesMap = java.util.HashMap<String, Image>()
        for (md in GameObject.MoveDirection.values()) {
            texturesMap["head$md"] = TextureLoader.getStaticTexture("equipment/head/0000$md")
        }
        texturesMap["body"] = TextureLoader.getStaticTexture("equipment/body/0000")
        texturesMap["handtop"] = TextureLoader.getStaticTexture("equipment/handtop/0000")
        texturesMap["handbottom"] = TextureLoader.getStaticTexture("equipment/handbottom/0000")
        texturesMap["legtop"] = TextureLoader.getStaticTexture("equipment/legtop/0000")
        texturesMap["legbottom"] = TextureLoader.getStaticTexture("equipment/legbottom/0000")
        texturesMap["cloak"] = TextureLoader.getStaticTexture("equipment/cloak/0000")
        texturesMap["neck"] = TextureLoader.getStaticTexture("equipment/neck/0000")
        texturesMap["weapon1"] = TextureLoader.getStaticTexture("equipment/onehanded/0000")
        texturesMap["weapon2"] = TextureLoader.getStaticTexture("equipment/onehanded/0000")
        return texturesMap
    }


    operator fun get(id: Long) = drawers[id]

    operator fun set(id: Long, drawer: ObjectDrawer?) {
        drawers[id] = drawer
    }
}