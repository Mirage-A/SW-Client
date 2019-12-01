package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import com.mirage.utils.Assets
import com.mirage.utils.Log
import com.mirage.utils.TestSamples
import com.mirage.utils.game.objects.ExtendedObject
import java.io.Reader

object SceneLoader {

    private val cachedTemplates = HashMap<String, ExtendedObject>()

    private val gson = GsonBuilder().create() //TODO Настроить десериализацию для удобной работы с шаблонными объектами

    fun loadMap(name: String) : GameMap =
            try {
                loadMap(Assets.loadReader("maps/$name.json")!!)
            }
            catch(ex: Exception) {
                Log.e("Error while loading map from scene: $name")
                TestSamples.TEST_SMALL_MAP
            }

    fun loadMap(reader: Reader) : GameMap =
            try {
                gson.fromJson<GameMap>(reader, GameMap::class.java)
            }
            catch (ex: Exception) {
                Log.e("Error while loading scene.")
                ex.printStackTrace()
                TestSamples.TEST_SMALL_MAP
            }

    fun loadInitialObjects(name: String) : List<ExtendedObject> =
            try {
                loadInitialObjects(Assets.loadReader("maps/$name.json")!!)
            }
            catch(ex: Exception) {
                Log.e("Error while loading initial objects from scene: $name")
                ArrayList()
            }

    fun loadInitialObjects(reader: Reader) : List<ExtendedObject> =
            try {

                val objsList: List<GameObject> = gson.fromJson<NullableObjectsList>(objsReader, NullableObjectsList::class.java).objects.map {
                    val templateName = it.template
                    if (templateName == null) {
                        GameObject(
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
                                state = it.state ?: "",
                                action = it.action ?: "IDLE")
                    } else {
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
                                moveDirection = GameObject.MoveDirection.fromString(it.moveDirection
                                        ?: template.moveDirection.toString()),
                                isMoving = it.isMoving ?: template.isMoving ?: false,
                                transparencyRange = it.transparencyRange ?: template.transparencyRange ?: 0f,
                                state = it.state ?: template.state ?: "",
                                action = it.action ?: template.action ?: "IDLE"
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
            catch (ex: Exception) {
                Log.e("Error while loading scene.")
                ex.printStackTrace()
                return Pair(TestSamples.TEST_SMALL_MAP, TestSamples.TEST_NO_GAME_OBJECTS)
            }

    /**
     * Загружает шаблон объекта по названию.
     */
    fun loadTemplate(name: String) : GameObject = cachedTemplates[name] ?: try {
        val t = loadTemplate(Assets.loadReader("templates/$name.json")!!)
        cachedTemplates[name] = t
        t
    }
    catch (ex: Exception) {
        Log.e("Error while loading template: $name")
        Log.e(ex.stackTrace.toString())
        TestSamples.TEST_GAME_OBJECT
    }

    /**
     * Загружает шаблон объекта через [reader] без кэширования.
     */
    fun loadTemplate(reader: Reader) : GameObject = try {
        val t = gson.fromJson<NullableGameObject>(reader, NullableGameObject::class.java)
        GameObject(
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
                state = t.state ?: "",
                action = t.action ?: "IDLE"
        )

    }
    catch (ex: Exception) {
        Log.e("Error while loading template.")
        Log.e(ex.stackTrace.toString())
        TestSamples.TEST_GAME_OBJECT
    }


}