package com.mirage.utils.maps

import com.badlogic.gdx.Gdx
import com.google.gson.Gson
import com.mirage.utils.Assets
import java.io.Reader

object SceneLoader {

    private val cachedBuildingTemplates = HashMap<String, Building>()

    private val cachedEntityTemplates = HashMap<String, Entity>()


    /**
     * Загружает сцену (пару из карты и объектов) по названию карты (папки, в которой хранится карта - например, "test").
     * Используется Gdx.files.internal
     */
    fun loadScene(name: String) : Pair<GameMap, GameObjects> =
            loadScene(Gdx.files.internal("${Assets.assetsPath}maps/$name/map.json").reader(),
                    Gdx.files.internal("${Assets.assetsPath}maps/$name/objects.json").reader(),
                    Gdx.files.internal("${Assets.assetsPath}maps/$name/entities.json").reader())

    /**
     * Загружает сцену (пару из карты и объектов)
     */
    fun loadScene(mapReader: Reader, buildingReader: Reader, entitiesReader: Reader) : Pair<GameMap, GameObjects> {
        val gson = Gson()
        val map = gson.fromJson<GameMap>(mapReader, GameMap::class.java)
        val buildingsList : List<Building> = gson.fromJson<List<NullableBuilding>>(buildingReader, ArrayList::class.java).map {
            if (it.template == null) {
                Building(name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        state = it.state,
                        isRigid = it.isRigid ?: false)
            }
            else {
                val template = loadBuildingTemplate(it.template)
                Building(name = it.name ?: template.name,
                        template = it.template,
                        x = it.x ?: template.x,
                        y = it.y ?: template.y,
                        width = it.width ?: template.width,
                        height = it.height ?: template.height,
                        state = it.state ?: template.state,
                        isRigid = it.isRigid ?: template.isRigid
                )
            }
        }
        val entitiesList : List<Entity> = gson.fromJson<List<NullableEntity>>(entitiesReader, ArrayList::class.java).map {
            if (it.template == null) {
                Entity(name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        state = it.state,
                        isRigid = it.isRigid ?: false,
                        speed = it.speed ?: 0f,
                        moveDirection = it.moveDirection ?: "DOWN")
            }
            else {
                val template = loadEntityTemplate(it.template)
                Entity(name = it.name ?: template.name,
                        template = it.template,
                        x = it.x ?: template.x,
                        y = it.y ?: template.y,
                        width = it.width ?: template.width,
                        height = it.height ?: template.height,
                        state = it.state ?: template.state,
                        isRigid = it.isRigid ?: template.isRigid,
                        speed = it.speed ?: template.speed,
                        moveDirection = it.moveDirection ?: template.moveDirection
                )
            }
        }
        var counter = Long.MIN_VALUE
        val objectsMap = HashMap<Long, GameObject>()
        for (obj in buildingsList) {
            objectsMap[counter] = obj
            ++counter
        }
        for (obj in entitiesList) {
            objectsMap[counter] = obj
            ++counter
        }
        return Pair(map, GameObjects(objectsMap, counter))
    }

    /**
     * Загружает шаблон [Entity] по названию.
     * //TODO Использовать не Gdx.files.internal, а использовать Assets.getReader, а уже в нем отказаться от libGdx при запуске сервера.
     */
    fun loadEntityTemplate(name: String) : Entity = cachedEntityTemplates[name] ?: run {
        val t = loadEntityTemplate(Gdx.files.internal("${Assets.assetsPath}templates/entities/$name.json").reader())
        cachedEntityTemplates[name] = t
        t
    }

    /**
     * Загружает шаблон [Entity] через [reader] без кэширования.
     */
    fun loadEntityTemplate(reader: Reader) : Entity =
            Gson().fromJson<Entity>(reader, Entity::class.java)


    /**
     * Загружает шаблон [Building] по названию.
     */
    fun loadBuildingTemplate(name: String) : Building = cachedBuildingTemplates[name] ?: run {
        val t = loadBuildingTemplate(Gdx.files.internal("${Assets.assetsPath}templates/buildings/$name.json").reader())
        cachedBuildingTemplates[name] = t
        t
    }

    /**
     * Загружает шаблон [Building] через [reader] без кэширования.
     */
    fun loadBuildingTemplate(reader: Reader) : Building =
            Gson().fromJson<Building>(reader, Building::class.java)

}