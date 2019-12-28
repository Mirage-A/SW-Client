package com.mirage.logic.data

import com.google.gson.reflect.TypeToken
import com.mirage.core.utils.Assets
import com.mirage.core.utils.Log
import com.mirage.core.utils.Rectangle
import com.mirage.core.utils.EntityID
import com.mirage.core.utils.GameMapName
import com.mirage.core.utils.fromJson
import com.mirage.core.game.maps.SceneLoader
import com.mirage.logic.state.ExtendedBuilding
import com.mirage.logic.state.ExtendedEntity
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.logic.state.ExtendedState
import com.mirage.logic.behavior.Behavior
import com.mirage.logic.behavior.DummyBehavior
import com.mirage.logic.behavior.NullBehavior
import java.io.Reader
import java.lang.reflect.Type

internal class ExtendedSceneLoader(gameMapName: GameMapName) : SceneLoader(gameMapName) {

    private val cachedEntityTemplates = HashMap<String, ExtendedEntity>()
    private val cachedBuildingTemplates = HashMap<String, ExtendedBuilding>()
    private val cachedBehaviorInfo = HashMap<String, Map<String, Any?>>()

    internal fun loadBehavior(entityTemplate: String, entityID: EntityID, data: LogicData): Behavior =
            try {
                val cached = cachedBehaviorInfo[entityTemplate]
                val info = if (cached == null) {
                    val loaded = gson.fromJson<BehaviorInfoWrapper>(getEntityTemplateReader(entityTemplate)!!)!!.behavior!!
                    cachedBehaviorInfo[entityTemplate] = loaded
                    loaded
                } else cached
                when (info["type"]) {
                    "null" -> NullBehavior()
                    "dummy" -> DummyBehavior(entityID, data)
                    else -> NullBehavior()
                }
            } catch (ex: Exception) {
                Log.e("Error while loading entity behavior: $entityTemplate")
                NullBehavior()
            }


    fun loadAreas(): Iterable<ScriptArea> =
            try {
                loadAreas(Assets.loadReader("scenes/$gameMapName/areas/areas.json")!!)
            } catch (ex: Exception) {
                Log.e("Error while loading areas from scene: $gameMapName")
                ArrayList()
            }

    fun loadAreas(reader: Reader): Iterable<ScriptArea> =
            try {
                val type: Type = object : TypeToken<List<NullableArea?>?>() {}.type
                val nullableAreas: List<NullableArea> = gson.fromJson(reader, type) ?: ArrayList()
                nullableAreas.map {
                    ScriptArea(
                            Rectangle(it.x ?: 0f, it.y ?: 0f, it.width ?: 0f, it.height ?: 0f),
                            it.playersOnly ?: true,
                            it.onEnter,
                            it.onLeave
                    )
                }
            } catch (ex: Exception) {
                Log.e("Error while loading areas.")
                ex.printStackTrace()
                ArrayList()
            }

    fun loadAreaScript(scriptName: String): Reader? =
            Assets.loadReader("scenes/$gameMapName/areas/$scriptName.lua")

    fun loadInitialState(): ExtendedState =
            try {
                loadInitialState(Assets.loadReader(
                        "scenes/$gameMapName/buildings.json")!!,
                        Assets.loadReader("scenes/$gameMapName/entities.json")!!
                )
            } catch (ex: Exception) {
                Log.e("Error while loading initial objects from scene: $gameMapName")
                ExtendedState()
            }

    fun loadInitialState(buildingsReader: Reader, entitiesReader: Reader): ExtendedState =
            try {
                val buildingsType: Type = object : TypeToken<List<NullableBuilding?>?>() {}.type
                val nullableBuildings = gson.fromJson<List<NullableBuilding>?>(buildingsReader, buildingsType)
                val buildingsList = nullableBuildings?.map {
                    it.projectOnTemplate(loadBuildingTemplate(it.template ?: "undefined"))
                } ?: ArrayList()
                val entitiesType: Type = object : TypeToken<List<NullableEntity?>?>() {}.type
                val nullableEntities = gson.fromJson<List<NullableEntity>?>(entitiesReader, entitiesType)
                val entitiesList = nullableEntities?.map {
                    it.projectOnTemplate(loadEntityTemplate(it.template ?: "undefined"))
                } ?: ArrayList()
                ExtendedState(buildingsList, entitiesList)
            } catch (ex: Exception) {
                Log.e("Error while loading initial objects from scene.")
                ex.printStackTrace()
                ExtendedState()
            }

    fun loadEntityTemplate(name: String): ExtendedEntity = cachedEntityTemplates[name] ?: try {
        val t = gson.fromJson<NullableEntity>(
                getEntityTemplateReader(name)!!
        )?.projectOnTemplate(defaultEntity) ?: defaultEntity
        cachedEntityTemplates[name] = t
        t
    } catch (ex: Exception) {
        Log.e("Error while loading entity template: $name")
        Log.e(ex.stackTrace.toString())
        ExtendedEntity()
    }

    fun loadBuildingTemplate(name: String): ExtendedBuilding = cachedBuildingTemplates[name] ?: try {
        val t = gson.fromJson<NullableBuilding>(
                getBuildingTemplateReader(name)!!
        )?.projectOnTemplate(defaultBuilding) ?: defaultBuilding
        cachedBuildingTemplates[name] = t
        t
    } catch (ex: Exception) {
        Log.e("Error while loading building template: $name")
        Log.e(ex.stackTrace.toString())
        ExtendedBuilding()
    }

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
            val interactionRange: Float?,
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
                interactionRange ?: t.interactionRange,
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

    private class NullableArea(
            val x: Float?,
            val y: Float?,
            val width: Float?,
            val height: Float?,
            val playersOnly: Boolean?,
            val onEnter: String?,
            val onLeave: String?
    )

    private class BehaviorInfoWrapper(val behavior: Map<String, Any?>?)
}