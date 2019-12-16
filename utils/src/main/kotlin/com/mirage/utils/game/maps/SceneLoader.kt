package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.mirage.utils.Assets
import com.mirage.utils.Log
import com.mirage.utils.TestSamples
import com.mirage.utils.extensions.fromJson
import com.mirage.utils.extensions.mutableMap
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.extended.ExtendedObject
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.states.ExtendedState
import java.io.Reader

object SceneLoader {

    private val cachedEntityTemplates = HashMap<String, ExtendedEntity>()
    private val cachedBuildingTemplates = HashMap<String, ExtendedBuilding>()

    private val gson = Gson()

    fun loadMap(name: String): GameMap =
            try {
                loadMap(Assets.loadReader("maps/$name.json")!!)
            }
            catch(ex: Exception) {
                Log.e("Error while loading map from scene: $name")
                TestSamples.TEST_SMALL_MAP
            }

    fun loadMap(reader: Reader): GameMap =
            try {
                gson.fromJson<MapWrapper>(reader)?.map ?: TestSamples.TEST_SMALL_MAP
            }
            catch (ex: Exception) {
                Log.e("Error while loading scene.")
                ex.printStackTrace()
                TestSamples.TEST_SMALL_MAP
            }

    fun loadInitialState(name: String): ExtendedState =
            try {
                loadInitialState(Assets.loadReader("maps/$name.json")!!)
            }
            catch(ex: Exception) {
                Log.e("Error while loading initial objects from scene: $name")
                ExtendedState()
            }

    fun loadInitialState(reader: Reader): ExtendedState =
            try {

                val objs: ObjectsWrapper = gson.fromJson<StateWrapper>(reader)?.objects ?: ObjectsWrapper(null, null)
                val buildingsList = objs.buildings?.map {
                    it.projectOnTemplate(loadBuildingTemplate(it.template ?: "undefined"))
                } ?: ArrayList()
                val entitiesList = objs.entities?.map {
                    it.projectOnTemplate(loadEntityTemplate(it.template ?: "undefined"))
                } ?: ArrayList()
                ExtendedState(buildingsList, entitiesList)
            }
            catch (ex: Exception) {
                Log.e("Error while loading initial objects from scene.")
                ex.printStackTrace()
                ExtendedState()
            }


    fun loadEntityTemplate(name: String): ExtendedEntity = cachedEntityTemplates[name] ?: try {
        val t = gson.fromJson<NullableEntity>(
                Assets.loadReader("templates/entities/$name.json")!!
        )?.projectOnTemplate(defaultEntity) ?: defaultEntity
        cachedEntityTemplates[name] = t
        t
    }
    catch (ex: Exception) {
        Log.e("Error while loading entity template: $name")
        Log.e(ex.stackTrace.toString())
        ExtendedEntity()
    }

    fun loadBuildingTemplate(name: String): ExtendedBuilding = cachedBuildingTemplates[name] ?: try {
        val t = gson.fromJson<NullableBuilding>(
                Assets.loadReader("templates/buildings/$name.json")!!
        )?.projectOnTemplate(defaultBuilding) ?: defaultBuilding
        cachedBuildingTemplates[name] = t
        t
    }
    catch (ex: Exception) {
        Log.e("Error while loading building template: $name")
        Log.e(ex.stackTrace.toString())
        ExtendedBuilding()
    }

    private class MapWrapper(
            val map: GameMap?
    )

    private class StateWrapper(
            val objects: ObjectsWrapper?
    )

    private class ObjectsWrapper(
            val buildings: List<NullableBuilding>?,
            val entities: List<NullableEntity>?
    )

    private val defaultEntity = ExtendedEntity()

    private class NullableEntity(
            val template: String?,
            val x: Float?,
            val y: Float?,
            val name: String?,
            val width: Float?,
            val height: Float?,
            val speed: Float?,
            val moveDirection: String?,
            val isMoving: Boolean?,
            val state: String?,
            val action: String?,
            val health: Int?,
            val maxHealth: Int?,
            val factionID: Int?,
            val isRigid: Boolean?
    ) {
        fun projectOnTemplate(t: ExtendedEntity) = ExtendedEntity(
                template ?: t.template,
                x ?: t.x,
                y ?: t.y,
                name ?: t.name,
                width ?: t.width,
                height ?: t.height,
                speed ?: t.speed,
                MoveDirection.fromString(moveDirection ?: t.moveDirection.toString()),
                isMoving ?: t.isMoving,
                state ?: t.state,
                action ?: t.action,
                health ?: t.health,
                maxHealth ?: t.maxHealth,
                factionID ?: t.factionID,
                isRigid ?: t.isRigid

        )
    }

    private val defaultBuilding = ExtendedBuilding()

    private class NullableBuilding(
            val template: String?,
            val x: Float?,
            val y: Float?,
            val name: String?,
            val width: Float?,
            val height: Float?,
            val transparencyRange: Float?,
            val state: String?,
            val isRigid: Boolean?
    ) {
        fun projectOnTemplate(t: ExtendedBuilding) = ExtendedBuilding(
                template ?: t.template,
                x ?: t.x,
                y ?: t.y,
                name ?: t.name,
                width ?: t.width,
                height ?: t.height,
                transparencyRange ?: t.transparencyRange,
                state ?: t.state,
                isRigid ?: t.isRigid

        )
    }


}