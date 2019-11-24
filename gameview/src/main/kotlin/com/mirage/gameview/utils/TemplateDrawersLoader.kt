package com.mirage.gameview.utils

import com.google.gson.Gson
import com.mirage.gameview.drawers.DrawerTemplate
import com.mirage.gameview.drawers.templates.*
import com.mirage.utils.Assets
import com.mirage.utils.Log
import com.mirage.utils.game.objects.GameObject
import java.io.Reader

/**
 * Загружает шаблонные представления для всех состояний шаблона [templateName]
 * @return Словарь, в котором по ключу - состоянию объекта получаем шаблонное представление.
 */
internal fun loadDrawersFromTemplateReader(reader: Reader, templateName: String) : MutableMap<String, DrawerTemplate> {
    val info = Gson().fromJson<TemplateDrawersInfo>(reader, TemplateDrawersInfo::class.java)
    val states = info?.drawers?.keys ?: run {
        Log.e("Error while loading drawers from template $templateName")
        return HashMap()
    }
    val result = HashMap<String, DrawerTemplate>()
    val visitedStates = HashSet<String>()
    fun loadState(state: String) : DrawerTemplate {
        val drawerInfo : Map<String, String> = info.drawers[state] ?: return EmptyDrawerTemplate()
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
                val gloves = drawerInfo["gloves"] ?: "default"
                val legs = drawerInfo["legs"] ?: "default"
                val weaponType = drawerInfo["weaponType"] ?: "UNARMED"
                val rightWeapon = drawerInfo["rightWeapon"] ?: "default"
                val leftWeapon = drawerInfo["leftWeapon"] ?: "default"
                HumanoidDrawerTemplate(GameObject.HumanoidEquipment(
                        helmet, chest, gloves, cloak, legs, rightWeapon, leftWeapon, GameObject.WeaponType.fromString(weaponType)
                ))
            }
            "animation" -> (drawerInfo["animationName"])?.let { AnimationDrawerTemplate(it) } ?: EmptyDrawerTemplate()
            "opaqueTransparent" -> {
                val opaqueDrawer : DrawerTemplate = drawerInfo["opaqueDrawer"]?.let {
                    loadState(it)
                } ?: run {
                    Log.e("Error: template=$templateName state=$state: can't load opaqueDrawer.")
                    return EmptyDrawerTemplate()
                }
                val transparentDrawer : DrawerTemplate = drawerInfo["transparentDrawer"]?.let {
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

fun loadDrawersFromTemplate(templateName: String) : MutableMap<String, DrawerTemplate> {
    val reader = Assets.loadReader("templates/$templateName.json")
    if (reader == null) {
        Log.e("Error: can't find template $templateName")
        return HashMap()
    }
    return loadDrawersFromTemplateReader(reader, templateName)
}

private class TemplateDrawersInfo(val drawers: HashMap<String, HashMap<String, String>>)