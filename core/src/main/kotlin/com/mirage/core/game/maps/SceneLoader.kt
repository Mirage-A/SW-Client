package com.mirage.core.game.maps

import com.google.gson.Gson
import com.mirage.core.utils.Assets
import com.mirage.core.utils.Log
import com.mirage.core.utils.GameMapName
import com.mirage.core.utils.fromJson
import com.mirage.core.utils.TestSamples
import java.io.Reader


open class SceneLoader(protected val gameMapName: GameMapName) {


    protected val gson = Gson()

    fun loadMap(): GameMap =
            try {
                loadMap(Assets.loadReader("scenes/$gameMapName/map.json")!!)
            } catch (ex: Exception) {
                Log.e("Error while loading map from scene: $gameMapName")
                TestSamples.TEST_SMALL_MAP
            }

    fun loadMap(reader: Reader): GameMap =
            try {
                gson.fromJson(reader) ?: TestSamples.TEST_SMALL_MAP
            } catch (ex: Exception) {
                Log.e("Error while loading scene.")
                ex.printStackTrace()
                TestSamples.TEST_SMALL_MAP
            }


    fun getEntityTemplateReader(name: String): Reader? = Assets.loadReader("scenes/$gameMapName/templates/entities/$name/entity.json")

    fun getBuildingTemplateReader(name: String): Reader? = Assets.loadReader("scenes/$gameMapName/templates/buildings/$name/building.json")


}