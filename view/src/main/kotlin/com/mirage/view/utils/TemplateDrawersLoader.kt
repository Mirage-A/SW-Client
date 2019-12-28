package com.mirage.view.utils

import com.google.gson.Gson
import com.mirage.core.Log
import com.mirage.core.extensions.fromJson
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.WeaponType
import com.mirage.view.drawers.DrawerTemplate
import com.mirage.view.drawers.templates.*
import java.io.Reader

/**
 * Загружает шаблонные представления для всех состояний шаблона [templateName]
 * @return Словарь, в котором по ключу - состоянию объекта получаем шаблонное представление.
 */
internal fun loadDrawersFromTemplateReader(reader: Reader, templateName: String): MutableMap<String, DrawerTemplate> {
    val info = Gson().fromJson<TemplateDrawersInfo>(reader)
    val states = info?.drawers?.keys ?: run {
        Log.e("Error while loading drawers from template $templateName")
        return HashMap()
    }
    val result = HashMap<String, DrawerTemplate>()
    val visitedStates = HashSet<String>()
    fun loadState(state: String): DrawerTemplate {
        val drawerInfo: Map<String, String> = info.drawers[state] ?: return EmptyDrawerTemplate()
        return when (drawerInfo["type"]) {
            "empty" -> EmptyDrawerTemplate()
            "staticTexture" -> StaticTextureDrawerTemplate(drawerInfo["textureName"] ?: "null")
            "copy" -> {
                val originState = drawerInfo["originState"] ?: run {
                    Log.e("Error: template=$templateName state=$state type=copy: field originState not found or is not string.")
                    return EmptyDrawerTemplate()
                }
                if (visitedStates.contains(originState)) {
                    Log.e("Error: template=$templateName state=$state type=copy: endless recursion.")
                    return EmptyDrawerTemplate()
                }
                visitedStates.add(originState)
                loadState(originState)
            }
            "humanoid" -> {
                val helmet = drawerInfo["helmet"] ?: "default"
                val chest = drawerInfo["chest"] ?: "default"
                val cloak = drawerInfo["cloak"] ?: "default"
                val legs = drawerInfo["legs"] ?: "default"
                val weaponType = drawerInfo["weaponType"] ?: "UNARMED"
                val rightWeapon = drawerInfo["rightWeapon"] ?: "default"
                val leftWeapon = drawerInfo["leftWeapon"] ?: "default"
                HumanoidDrawerTemplate(Equipment(
                        helmet, chest, cloak, legs, rightWeapon, leftWeapon, WeaponType.fromString(weaponType)
                ))
            }
            "animation" -> (drawerInfo["animationName"])?.let { AnimationDrawerTemplate(it) } ?: EmptyDrawerTemplate()
            "opaqueTransparent" -> {
                val opaqueDrawer: DrawerTemplate = drawerInfo["opaqueDrawer"]?.let {
                    loadState(it)
                } ?: run {
                    Log.e("Error: template=$templateName state=$state: can't load opaqueDrawer.")
                    return EmptyDrawerTemplate()
                }
                val transparentDrawer: DrawerTemplate = drawerInfo["transparentDrawer"]?.let {
                    loadState(it)
                } ?: run {
                    Log.e("Error: template=$templateName state=$state: can't load transparentDrawer.")
                    return EmptyDrawerTemplate()
                }
                OpaqueTransparentDrawerTemplate(opaqueDrawer, transparentDrawer)
            }
            else -> {
                Log.e("Error: template=$templateName state=$state: can't determine type of drawer.")
                return EmptyDrawerTemplate()
            }
        }
    }
    for (state in states) {
        if (!result.containsKey(state)) result[state] = loadState(state)
    }
    return result
}

internal fun loadEntityDrawersFromTemplate(sceneLoader: SceneLoader, templateName: String): MutableMap<String, DrawerTemplate> {
    val reader = sceneLoader.getEntityTemplateReader(templateName)
    if (reader == null) {
        Log.e("Error: can't find entity template $templateName")
        return HashMap()
    }
    return loadDrawersFromTemplateReader(reader, templateName)
}

internal fun loadBuildingDrawersFromTemplate(sceneLoader: SceneLoader, templateName: String): MutableMap<String, DrawerTemplate> {
    val reader = sceneLoader.getBuildingTemplateReader(templateName)
    if (reader == null) {
        Log.e("Error: can't find building template $templateName")
        return HashMap()
    }
    return loadDrawersFromTemplateReader(reader, templateName)
}

private class TemplateDrawersInfo(val drawers: HashMap<String, HashMap<String, String>>)