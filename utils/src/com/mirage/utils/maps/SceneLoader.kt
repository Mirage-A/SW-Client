package com.mirage.utils.maps

import com.google.gson.Gson
import com.mirage.utils.Assets
import java.io.Reader

object SceneLoader {

    private val cachedBuildingTemplates = HashMap<String, Building>()

    private val cachedEntityTemplates = HashMap<String, Entity>()


    /**
     * Загружает сцену (пару из карты и объектов) по названию карты (папки, в которой хранится карта - например, "test").
     */
    fun loadScene(name: String) : Pair<GameMap, GameObjects> =
            loadScene(Assets.loadReader("maps/$name/map.json"),
                    Assets.loadReader("maps/$name/objects.json"),
                    Assets.loadReader("maps/$name/entityDifferences.json"))

    /**
     * Загружает сцену (пару из карты и объектов)
     */
    fun loadScene(mapReader: Reader, buildingReader: Reader, entitiesReader: Reader) : Pair<GameMap, GameObjects> {
        val gson = Gson()
        val map = gson.fromJson<GameMap>(mapReader, GameMap::class.java)
        var counter = Long.MIN_VALUE
        val buildingsList : List<Building> = gson.fromJson<NullableBuildingsList>(buildingReader, NullableBuildingsList::class.java).buildingDifferences.map {
            val templateName = it.template
            if (templateName == null) {
                Building(
                        id = counter++,
                        name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        state = it.state,
                        isRigid = it.isRigid ?: false,
                        scripts = it.scripts)
            }
            else {
                val template = loadBuildingTemplate(templateName)
                it.projectOn(template)
            }
        }
        val entitiesList : List<Entity> = gson.fromJson<NullableEntitiesList>(entitiesReader, NullableEntitiesList::class.java).entityDifferences.map {
            val templateName = it.template
            if (templateName == null) {
                Entity(
                        id = counter++,
                        name = it.name,
                        template = it.template,
                        x = it.x ?: 0f,
                        y = it.y ?: 0f,
                        width = it.width ?: 0f,
                        height = it.height ?: 0f,
                        state = it.state,
                        isRigid = it.isRigid ?: false,
                        scripts = it.scripts,
                        speed = it.speed ?: 0f,
                        moveDirection = it.moveDirection ?: "DOWN")
            }
            else {
                val template = loadEntityTemplate(templateName)
                it.projectOn(template)
            }
        }
        val objectsMap = HashMap<Long, GameObject>()
        for (obj in buildingsList) {
            objectsMap[obj.id] = obj
        }
        for (obj in entitiesList) {
            objectsMap[obj.id] = obj
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