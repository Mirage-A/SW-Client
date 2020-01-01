package com.mirage.view.drawers.animation

import com.mirage.core.utils.Log
import com.mirage.core.utils.Point
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.properties.WeaponType
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream


/** Animation, created in SW Animation Editor */
class Animation() {

    var frames: ArrayList<Frame> = ArrayList()

    var duration = 1000

    var isRepeatable = true

    var data = HashMap<MoveDirection, HashMap<WeaponType, ArrayList<Frame>>>()

    /** Reads animation from inputStream of .xml file */
    constructor(inputStream: InputStream?) : this() {
        for (moveDirection in MoveDirection.values()) {
            data[moveDirection] = HashMap()
            for (weaponType in WeaponType.values()) {
                data[moveDirection]!![weaponType] = ArrayList()
            }
        }
        if (inputStream == null) {
            return
        }
        try {
            val reader = SAXReader()
            val document = reader.read(inputStream)
            val animation = document.rootElement
            duration = Integer.parseInt(animation.attributeValue("duration"))
            isRepeatable = animation.attributeValue("isRepeatable") == "true"
            for (md in animation.elements()) {
                md as Element
                val moveDirection = MoveDirection.fromString(md.name)
                for (wt in md.elements()) {
                    wt as Element
                    val weaponType = WeaponType.fromString(wt.name)
                    val framesArr = data[moveDirection]!![weaponType]!!
                    for (fr in wt.elements()) {
                        fr as Element
                        val frame = Frame()
                        for (lyr in fr.elements()) {
                            lyr as Element
                            val layer = Layer(
                                    lyr.attributeValue("imageName"),
                                    lyr.attributeValue("x").toFloat(),
                                    lyr.attributeValue("y").toFloat(),
                                    lyr.attributeValue("scale").toFloat(),
                                    lyr.attributeValue("scaleX").toFloat(),
                                    lyr.attributeValue("scaleY").toFloat(),
                                    lyr.attributeValue("angle").toFloat(),
                                    lyr.attributeValue("flipX")?.toBoolean() ?: false,
                                    lyr.attributeValue("isVisible")?.toBoolean() ?: true
                            )
                            frame.layers.add(layer)
                        }
                        framesArr.add(frame)
                    }
                }
            }

            frames = data[MoveDirection.RIGHT]!![WeaponType.UNARMED]!!
        } catch (ex: Exception) {
            Log.e("Unexpected error occurred:\n" + ex.message)
        }
    }

    data class Frame(var layers: ArrayList<Layer> = ArrayList())

    data class Layer(var imageName: String, var x: Float = 0f, var y: Float = 0f, var scale: Float = 1f, var scaleX: Float = 1f,
                     var scaleY: Float = 1f, var angle: Float = 0f, var flipX: Boolean = false, var isVisible: Boolean = true) {

        fun getName() = imageName

        fun getPosition(): Point {
            return Point(x, y)
        }

    }

}