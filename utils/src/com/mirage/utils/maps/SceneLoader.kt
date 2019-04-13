package com.mirage.utils.maps

import com.google.gson.Gson
import com.mirage.utils.Assets
import com.mirage.utils.gameobjects.*
import com.mirage.utils.gameobjects.NullableBuildingsList
import com.mirage.utils.gameobjects.NullableEntitiesList
import java.io.Reader

object SceneLoader {

    private val cachedBuildingTemplates = HashMap<String, Building>()

    private val cachedEntityTemplates = HashMap<String, Entity>()


    /**
     * Загружает сцену (пару из карты и объектов) по названию карты (папки, в которой хранится карта - например, "test").
     */
    fun loadScene(name: String) : Pair<GameMap, GameObjects> =
            loadScene(Assets.loadReader("maps/$name/map.json"),
                    Assets.loadReader("maps/$name/buildings.json"),
                    Assets.loadReader("maps/$name/entities.json"))

    /**
     * Загружает сцену (пару из карты и объектов)
     */
    fun loadScene(mapReader: Reader, buildingReader: Reader, entitiesReader: Reader) : Pair<GameMap, GameObjects> {
        val gson = Gson()
        val map = gson.fromJson<GameMap>(mapReader, GameMap::class.java)
        val buildingsList : List<Building> = gson.fromJson<NullableBuildingsList>(buildingReader, NullableBuildingsList::class.java).buildings.map {
            val templateName = it.template
            if (templateName == null) {
                Building(
                        name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        isRigid = it.isRigid ?: false,
                        speed = it.speed ?: 0f,
                        moveDirection = it.moveDirection,
                        isMoving = it.isMoving ?: false,
                        scripts = it.scripts)
            }
            else {
                val template = loadBuildingTemplate(templateName)
                it.projectOn(template) as Building
            }
        }
        val entitiesList : List<Entity> = gson.fromJson<NullableEntitiesList>(entitiesReader, NullableEntitiesList::class.java).entities.map {
            val templateName = it.template
            if (templateName == null) {
                Entity(
                        name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        isRigid = it.isRigid ?: false,
                        speed = it.speed ?: 0f,
                        moveDirection = it.moveDirection,
                        isMoving = it.isMoving ?: false,
                        scripts = it.scripts
                )
            }
            else {
                val template = loadEntityTemplate(templateName)
                it.projectOn(template) as Entity
            }
        }
        val objectsMap = HashMap<Long, GameObject>()
        var counter = Long.MIN_VALUE
        for (obj in buildingsList) {
            objectsMap[counter++] = obj
        }
        for (obj in entitiesList) {
            objectsMap[counter++] = obj
        }
        return Pair(map, GameObjects(objectsMap, buildingsList.size + entitiesList.size + Long.MIN_VALUE))
    }

    /**
     * Загружает шаблон [Entity] по названию.
     * //TODO Использовать не Gdx.files.internal, а использовать Assets.getReader, а уже в нем отказаться от libGdx при запуске сервера.
     */
    fun loadEntityTemplate(name: String) : Entity = cachedEntityTemplates[name] ?: run {
        val t = loadEntityTemplate(Assets.loadReader("templates/entities/$name.json"))
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
        val t = loadBuildingTemplate(Assets.loadReader("templates/buildings/$name.json"))
        cachedBuildingTemplates[name] = t
        t
    }

    /**
     * Загружает шаблон [Building] через [reader] без кэширования.
     */
    fun loadBuildingTemplate(reader: Reader) : Building =
            Gson().fromJson<Building>(reader, Building::class.java)

}