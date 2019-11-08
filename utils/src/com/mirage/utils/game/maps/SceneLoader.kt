package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.mirage.utils.Assets
import com.mirage.utils.game.objects.*
import java.io.Reader

object SceneLoader {

    private val cachedTemplates = HashMap<String, GameObject>()


    /**
     * Загружает сцену (пару из карты и объектов) по названию карты (папки, в которой хранится карта - например, "test").
     */
    fun loadScene(name: String) : Pair<GameMap, GameObjects> =
            loadScene(Assets.loadReader("maps/$name/map.json"),
                    Assets.loadReader("maps/$name/objects.json"))

    /**
     * Загружает сцену (пару из карты и объектов)
     */
    fun loadScene(mapReader: Reader, objsReader: Reader) : Pair<GameMap, GameObjects> {
        val gson = Gson()
        val map = gson.fromJson<GameMap>(mapReader, GameMap::class.java)
        val objsList : List<GameObject> = gson.fromJson<NullableObjectsList>(objsReader, NullableObjectsList::class.java).objects.map {
            val templateName = it.template
            if (templateName == null) {
                GameObject (
                        name = it.name ?: "",
                        template = it.template ?: "",
                        type = GameObject.Type.fromString(it.type ?: ""),
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        isRigid = it.isRigid ?: false,
                        speed = it.speed ?: 0f,
                        moveDirection = GameObject.MoveDirection.fromString(it.moveDirection ?: ""),
                        isMoving = it.isMoving ?: false,
                        transparencyRange = it.transparencyRange ?: 0f,
                        state = it.state ?: "")
            }
            else {
                val template = loadTemplate(templateName)
                template.with(
                        name = it.name ?: template.name ?: "",
                        template = templateName,
                        type = GameObject.Type.fromString(it.type ?: template.type.toString()),
                        x = it.x ?: template.x ?: 0f,
                        y = it.y ?: template.y ?: 0f,
                        width = it.width ?: template.width ?: 0f,
                        height = it.height ?: template.height ?: 0f,
                        isRigid = it.isRigid ?: template.isRigid ?: false,
                        speed = it.speed ?: template.speed ?: 0f,
                        moveDirection = GameObject.MoveDirection.fromString(it.moveDirection ?: template.moveDirection.toString()),
                        isMoving = it.isMoving ?: template.isMoving ?: false,
                        transparencyRange = it.transparencyRange ?: template.transparencyRange ?: 0f,
                        state = it.state ?: template.state ?: ""
                )
            }
        }
        val objectsMap = HashMap<Long, GameObject>()
        var counter = Long.MIN_VALUE
        for (obj in objsList) {
            objectsMap[counter++] = obj
        }
        return Pair(map, GameObjects(objectsMap, objsList.size + Long.MIN_VALUE))
    }

    /**
     * Загружает шаблон объекта по названию.
     * //TODO Использовать не Gdx.files.internal, а использовать Assets.getReader, а уже в нем отказаться от libGdx при запуске сервера.
     */
    fun loadTemplate(name: String) : GameObject = cachedTemplates[name] ?: run {
        val t = loadTemplate(Assets.loadReader("templates/$name.json"))
        cachedTemplates[name] = t
        t
    }

    /**
     * Загружает шаблон объекта через [reader] без кэширования.
     */
    fun loadTemplate(reader: Reader) : GameObject {
        val t = Gson().fromJson<NullableGameObject>(reader, NullableGameObject::class.java)
        return GameObject(
                name = t.name ?: "",
                template = t.template ?: "",
                type = GameObject.Type.fromString(t.type ?: "BUILDING"),
                x = t.x ?: 0f,
                y = t.y ?: 0f,
                width = t.width ?: 0f,
                height = t.height ?: 0f,
                isRigid = t.isRigid ?: false,
                speed = t.speed ?: 0f,
                moveDirection = GameObject.MoveDirection.fromString(t.moveDirection ?: "DOWN"),
                isMoving = t.isMoving ?: false,
                transparencyRange = t.transparencyRange ?: 0f,
                state = t.state ?: ""
        )
    }

    /**
     * Класс, объект которого создаётся при десериализации объекта из JSON-файла.
     */
    private data class NullableGameObject (
            val name: String?,
            val template: String?,
            val type: String?,
            val x: Float?,
            val y: Float?,
            val width: Float?,
            val height: Float?,
            val isRigid: Boolean?,
            val speed: Float?,
            val moveDirection: String?,
            val isMoving: Boolean?,
            val transparencyRange: Float?,
            val state: String?
    )

    private data class NullableObjectsList(val objects: List<NullableGameObject>)

}