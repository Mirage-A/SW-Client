package com.mirage.core.game.maps

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mirage.core.Assets
import com.mirage.core.Log
import com.mirage.core.TestSamples
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.fromJson
import com.mirage.core.game.objects.extended.ExtendedBuilding
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.states.ExtendedState
import java.io.Reader
import java.lang.reflect.Type


open class SceneLoader(protected val gameMapName: GameMapName) {


    protected val gson = Gson()

    fun loadMap(): GameMap =
            try {
                loadMap(Assets.loadReader("scenes/$gameMapName/map.json")!!)
            }
            catch(ex: Exception) {
                Log.e("Error while loading map from scene: $gameMapName")
                TestSamples.TEST_SMALL_MAP
            }

    fun loadMap(reader: Reader): GameMap =
            try {
                gson.fromJson(reader) ?: TestSamples.TEST_SMALL_MAP
            }
            catch (ex: Exception) {
                Log.e("Error while loading scene.")
                ex.printStackTrace()
                TestSamples.TEST_SMALL_MAP
            }


    fun getEntityTemplateReader(name: String): Reader? = Assets.loadReader("scenes/$gameMapName/templates/entities/$name/entity.json")

    fun getBuildingTemplateReader(name: String): Reader? = Assets.loadReader("scenes/$gameMapName/templates/buildings/$name/building.json")


}