package com.mirage.gameview.drawers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.Log
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference
import com.mirage.gameview.drawers.templates.EmptyDrawerTemplate
import com.mirage.gameview.drawers.templates.HumanoidDrawerTemplate
import com.mirage.gameview.utils.loadDrawersFromTemplate

class DrawersManagerImpl : DrawersManager {

    /**
     * Словарь, в котором кэшируются шаблонные представления.
     * Первый ключ - название шаблона, второй ключ - состояние объекта.
     */
    private val cachedDrawerTemplates: MutableMap<String, MutableMap<String, DrawerTemplate>> = HashMap()

    /**
     * Словарь, в котором хранятся представления конкретных объектов в их текущем состоянии.
     * Ключ - ID объекта.
     */
    private val drawers: MutableMap<Long, Drawer> = HashMap()

    /**
     * Словарь, в котором хранится информация об экипировке объектов.
     * Это нужно, чтобы при смене состояния информация об экипировке не терялась.
     */
    private val equipment: MutableMap<Long, GameObject.HumanoidEquipment> = HashMap()

    /**
     * Загружает шаблонные представления для всех состояний шаблона [template] и кэширует их в [cachedDrawerTemplates].
     */
    private fun loadTemplateDrawers(templateName: String) {
        cachedDrawerTemplates[templateName] = loadDrawersFromTemplate(templateName)
    }

    override fun draw(objID: Long, batch: SpriteBatch, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: GameObject.MoveDirection) {
        val drawer : Drawer = drawers[objID] ?: run {
            Log.e("Drawer not loaded. objID=$objID")
            return
        }
        drawer.draw(batch, x, y, isOpaque, currentTimeMillis, moveDirection)
    }

    override fun loadDrawers(initialState: GameObjects, currentTimeMillis: Long) {
        for ((id, obj) in initialState) {
            loadDrawer(id, obj, currentTimeMillis)
        }
    }

    override fun updateDrawers(stateDifference: StateDifference, oldState: GameObjects, currentTimeMillis: Long) {
        for ((id, obj) in stateDifference.newObjects) {
            loadDrawer(id, obj, currentTimeMillis)
            setAction(id, obj.action, currentTimeMillis)
            setMoving(id, obj.isMoving, currentTimeMillis)
        }
        for ((id, newObj) in stateDifference.changedObjects) {
            val oldObj = oldState[id]
            if (oldObj == null || newObj.template != oldObj.template) {
                loadDrawer(id, newObj, currentTimeMillis)
                setAction(id, newObj.action, currentTimeMillis)
                setMoving(id, newObj.isMoving, currentTimeMillis)
                continue
            }
            if (newObj.state != oldObj.state) {
                loadDrawer(id, newObj, currentTimeMillis)
                setAction(id, newObj.action, currentTimeMillis)
                setMoving(id, newObj.isMoving, currentTimeMillis)
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

    override fun updateEquipment(objID: Long, obj: GameObject, equipment: GameObject.HumanoidEquipment, currentTimeMillis: Long) {
        this.equipment[objID] = equipment
        loadDrawer(objID, obj, currentTimeMillis)
    }

    private fun loadDrawer(objID: Long, obj: GameObject, currentTimeMillis: Long) {
        if (cachedDrawerTemplates[obj.template] == null) {
            loadTemplateDrawers(obj.template)
        }
        val templateDrawerStates : Map<String, DrawerTemplate>? = cachedDrawerTemplates[obj.template]
        if (templateDrawerStates == null) {
            Log.e("Error while loading drawer from a template. (objID=$objID template=${obj.template})")
            drawers[objID] = DrawerImpl(EmptyDrawerTemplate())
        }
        else {
            val template: DrawerTemplate = templateDrawerStates[obj.state] ?:
                templateDrawerStates["default"] ?: run {
                    Log.e("Error: template=${obj.template} state=${obj.state}: can't load neither state nor default state.")
                    EmptyDrawerTemplate()
                }

            val objEquipment = equipment[objID]
            if (template is HumanoidDrawerTemplate && objEquipment != null && objEquipment != template.equipment) {
                drawers[objID] = DrawerImpl(HumanoidDrawerTemplate(objEquipment), currentTimeMillis)
            } else drawers[objID] = DrawerImpl(template, currentTimeMillis)
        }
    }

    private fun setAction(objID: Long, newAction: String, currentTimeMillis: Long) {
        drawers[objID]?.setAction(newAction, currentTimeMillis)
    }

    private fun setMoving(objID: Long, newMoving: Boolean, currentTimeMillis: Long) {
        drawers[objID]?.setMoving(newMoving, currentTimeMillis)
    }

}