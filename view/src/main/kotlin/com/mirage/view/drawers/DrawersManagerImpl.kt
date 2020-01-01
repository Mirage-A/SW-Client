package com.mirage.view.drawers

import com.mirage.core.utils.Log
import com.mirage.core.utils.Rectangle
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedBuilding
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.VirtualScreen
import com.mirage.view.drawers.animation.AnimationLoader
import com.mirage.view.drawers.templates.EmptyDrawerTemplate
import com.mirage.view.drawers.templates.HumanoidDrawerTemplate
import com.mirage.view.utils.loadBuildingDrawersFromTemplate
import com.mirage.view.utils.loadEntityDrawersFromTemplate

class DrawersManagerImpl(
        private val animationLoader: AnimationLoader,
        private val sceneLoader: SceneLoader
) : DrawersManager {

    /**
     * Словарь, в котором кэшируются шаблонные представления.
     * Первый ключ - название шаблона, второй ключ - состояние объекта.
     */
    private val cachedBuildingDrawerTemplates: MutableMap<String, MutableMap<String, DrawerTemplate>> = HashMap()
    private val cachedEntityDrawerTemplates: MutableMap<String, MutableMap<String, DrawerTemplate>> = HashMap()

    /**
     * Словарь, в котором хранятся представления конкретных объектов в их текущем состоянии.
     * Ключ - ID объекта.
     */
    private val buildingDrawers: MutableMap<Long, Drawer> = HashMap()
    private val entityDrawers: MutableMap<Long, Drawer> = HashMap()

    /**
     * Словарь, в котором хранится информация об экипировке объектов.
     * Это нужно, чтобы при смене состояния информация об экипировке не терялась.
     */
    private val equipment: MutableMap<Long, Equipment> = HashMap()

    /** Загружает шаблонные представления для всех состояний шаблона [templateName] и кэширует их */
    private fun loadBuildingTemplateDrawers(templateName: String) {
        cachedBuildingDrawerTemplates[templateName] = loadBuildingDrawersFromTemplate(animationLoader, sceneLoader, templateName)
    }

    private fun loadEntityTemplateDrawers(templateName: String) {
        cachedEntityDrawerTemplates[templateName] = loadEntityDrawersFromTemplate(animationLoader, sceneLoader, templateName)
    }

    override fun getEntityHitbox(entityID: Long): Rectangle? = entityDrawers[entityID]?.hitBox

    override fun drawBuilding(buildingID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, currentTimeMillis: Long) {
        val drawer: Drawer = buildingDrawers[buildingID] ?: run {
            Log.e("Drawer not loaded. buildingID=$buildingID")
            return
        }
        drawer.draw(virtualScreen, x, y, width, height, isOpaque, currentTimeMillis)
    }

    override fun drawEntity(entityID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: MoveDirection) {
        val drawer: Drawer = entityDrawers[entityID] ?: run {
            Log.e("Drawer not loaded. entityID=$entityID")
            return
        }
        drawer.draw(virtualScreen, x, y, width, height, isOpaque, currentTimeMillis, moveDirection)
    }

    override fun loadDrawers(initialState: SimplifiedState, currentTimeMillis: Long) {
        for ((id, building) in initialState.buildings) {
            loadBuildingDrawer(id, building, currentTimeMillis)
        }
        for ((id, entity) in initialState.entities) {
            loadEntityDrawer(id, entity, currentTimeMillis)
        }
    }

    override fun updateDrawers(stateDifference: StateDifference, oldState: SimplifiedState, currentTimeMillis: Long) {
        for ((id, obj) in stateDifference.buildingsDifference.new) {
            loadBuildingDrawer(id, obj, currentTimeMillis)
        }
        for ((id, newObj) in stateDifference.buildingsDifference.changed) {
            val oldObj = oldState.buildings[id]
            if (oldObj == null || newObj.template != oldObj.template) {
                loadBuildingDrawer(id, newObj, currentTimeMillis)
                continue
            }
            if (newObj.state != oldObj.state) {
                loadBuildingDrawer(id, newObj, currentTimeMillis)
                continue
            }
        }
        for ((id, obj) in stateDifference.entitiesDifference.new) {
            loadEntityDrawer(id, obj, currentTimeMillis)
        }
        for ((id, newObj) in stateDifference.entitiesDifference.changed) {
            val oldObj = oldState.entities[id]
            if (oldObj == null || newObj.template != oldObj.template) {
                loadEntityDrawer(id, newObj, currentTimeMillis)
                continue
            }
            if (newObj.state != oldObj.state) {
                loadEntityDrawer(id, newObj, currentTimeMillis)
                continue
            }
            if (newObj.action != oldObj.action) {
                setAction(id, newObj.action, currentTimeMillis)
            }
            if (newObj.isMoving != oldObj.isMoving) {
                setMoving(id, newObj.isMoving, currentTimeMillis)
            }
        }
    }

    override fun updateEquipment(entityID: Long, entity: SimplifiedEntity, equipment: Equipment, currentTimeMillis: Long) {
        println("Equipment changed $entityID $equipment")
        this.equipment[entityID] = equipment
        val drawer: Drawer = DrawerImpl(HumanoidDrawerTemplate(animationLoader, equipment))
        drawer.setAction(entity.action, currentTimeMillis)
        drawer.setMoving(entity.isMoving, currentTimeMillis)
        entityDrawers[entityID] = drawer
    }

    private fun loadBuildingDrawer(buildingID: Long, building: SimplifiedBuilding, currentTimeMillis: Long) {
        if (cachedBuildingDrawerTemplates[building.template] == null) {
            loadBuildingTemplateDrawers(building.template)
        }
        val templateDrawerStates: Map<String, DrawerTemplate>? = cachedBuildingDrawerTemplates[building.template]
        val drawer = if (templateDrawerStates == null) {
            Log.e("Error while loading drawer from a template. (buildingID=$building template=${building.template})")
            DrawerImpl(EmptyDrawerTemplate())
        } else {
            val template: DrawerTemplate = templateDrawerStates[building.state] ?: templateDrawerStates["default"]
            ?: run {
                Log.e("Error: template=${building.template} state=${building.state}: can't load neither state nor default state.")
                EmptyDrawerTemplate()
            }
            DrawerImpl(template, currentTimeMillis)
        }
        buildingDrawers[buildingID] = drawer
    }

    private fun loadEntityDrawer(entityID: Long, entity: SimplifiedEntity, currentTimeMillis: Long) {
        val drawer: Drawer
        if (cachedEntityDrawerTemplates[entity.template] == null) {
            loadEntityTemplateDrawers(entity.template)
        }
        val templateDrawerStates: Map<String, DrawerTemplate>? = cachedEntityDrawerTemplates[entity.template]
        if (templateDrawerStates == null) {
            Log.e("Error while loading drawer from a template. (entityID=$entityID template=${entity.template})")
            drawer = DrawerImpl(EmptyDrawerTemplate())
        } else {
            val template: DrawerTemplate = templateDrawerStates[entity.state] ?: templateDrawerStates["default"]
            ?: run {
                Log.e("Error: template=${entity.template} state=${entity.state}: can't load neither state nor default state.")
                EmptyDrawerTemplate()
            }

            val objEquipment = equipment[entityID]
            drawer = if (template is HumanoidDrawerTemplate && objEquipment != null && objEquipment != template.equipment) {
                DrawerImpl(HumanoidDrawerTemplate(animationLoader, objEquipment), currentTimeMillis)
            } else DrawerImpl(template, currentTimeMillis)
        }
        drawer.setAction(entity.action, currentTimeMillis)
        drawer.setMoving(entity.isMoving, currentTimeMillis)
        entityDrawers[entityID] = drawer
    }

    private fun setAction(entityID: Long, newAction: String, currentTimeMillis: Long) {
        entityDrawers[entityID]?.setAction(newAction, currentTimeMillis)
    }

    private fun setMoving(entityID: Long, newMoving: Boolean, currentTimeMillis: Long) {
        entityDrawers[entityID]?.setMoving(newMoving, currentTimeMillis)
    }

}