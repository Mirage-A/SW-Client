package com.mirage.view.gameobjects

import com.badlogic.gdx.maps.MapObject
import com.mirage.controller.Controller
import com.mirage.model.config
import com.mirage.model.extensions.getFloat
import com.mirage.model.extensions.getString
import com.mirage.view.TextureLoader
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.view.animation.MoveDirection
import com.mirage.view.animation.WeaponType

/**
 * Класс, хранящий визуальные представления объектов
 */
class Drawers {

    private val opaqueDrawers = HashMap<MapObject, ObjectDrawer?>()
    private val transparentDrawers = HashMap<MapObject, ObjectDrawer?>()


    /**
     * Загружает objectDrawer данного объекта карты
     */
    fun addObjectDrawer(obj: MapObject) {
        this[obj, true] = when {
            obj.name == "player" -> HumanoidAnimation(loadPlayerTexturesMap(obj), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(obj.properties.getFloat("moveAngle", 0f)), WeaponType.UNARMED)
            obj.properties.containsKey("animation") -> ObjectAnimation(obj.properties.getString("animation", "MAIN_GATE_OPEN"))
            obj.properties.containsKey("texture") -> TextureLoader.getStaticTexture("objects/" + obj.properties.getString("texture", "null.png"), Image.Alignment.CENTER)
            else -> if (config["show-invisible-objects"] == true) TestObjectFiller(obj, Controller.gameScreen.camera) else null
        }
        this[obj, false] = when {
            obj.properties.containsKey("animation-tp") -> ObjectAnimation(obj.properties.getString("animation-tp", "MAIN_GATE_OPEN"))
            obj.properties.containsKey("texture-tp") -> TextureLoader.getStaticTexture("objects/" + obj.properties.getString("texture-tp", "null.png"), Image.Alignment.CENTER)
            else -> this[obj, true]
        }
    }

    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private fun loadPlayerTexturesMap(obj: MapObject): MutableMap<String, Image> {
        val texturesMap = java.util.HashMap<String, Image>()
        for (md in MoveDirection.values()) {
            texturesMap["head$md"] = TextureLoader.getStaticTexture("equipment/head/0000$md.png")
        }
        texturesMap["body"] = TextureLoader.getStaticTexture("equipment/body/0000.png")
        texturesMap["handtop"] = TextureLoader.getStaticTexture("equipment/handtop/0000.png")
        texturesMap["handbottom"] = TextureLoader.getStaticTexture("equipment/handbottom/0000.png")
        texturesMap["legtop"] = TextureLoader.getStaticTexture("equipment/legtop/0000.png")
        texturesMap["legbottom"] = TextureLoader.getStaticTexture("equipment/legbottom/0000.png")
        texturesMap["cloak"] = TextureLoader.getStaticTexture("equipment/cloak/0000.png")
        texturesMap["neck"] = TextureLoader.getStaticTexture("equipment/neck/0000.png")
        texturesMap["weapon1"] = TextureLoader.getStaticTexture("equipment/onehanded/0000.png")
        texturesMap["weapon2"] = TextureLoader.getStaticTexture("equipment/onehanded/0000.png")
        return texturesMap
    }

    fun addOpaqueDrawer(obj: MapObject, drawer: ObjectDrawer?) {
        opaqueDrawers[obj] = drawer
    }

    fun addTransparentDrawer(obj: MapObject, drawer: ObjectDrawer?) {
        transparentDrawers[obj] = drawer
    }

    fun getOpaqueDrawer(obj: MapObject) = opaqueDrawers[obj]

    fun getTransparentDrawer(obj: MapObject) = transparentDrawers[obj]

    operator fun get(obj: MapObject, isOpaque: Boolean) =
            if (isOpaque) opaqueDrawers[obj] else transparentDrawers[obj]

    operator fun set(obj: MapObject, isOpaque: Boolean, drawer: ObjectDrawer?) {
        if (isOpaque) opaqueDrawers[obj] = drawer
        else transparentDrawers[obj] = drawer
    }
}