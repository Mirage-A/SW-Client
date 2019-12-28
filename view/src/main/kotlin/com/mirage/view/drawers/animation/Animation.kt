package com.mirage.view.drawers.animation

import com.mirage.core.Log
import com.mirage.core.datastructures.Point
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.properties.WeaponType
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream


/**
 * Анимация, созданная с помощью Animation Editor-а
 * Объект этого класса содержит всю информацию об анимации,
 * а также конструктор загрузки анимации из XML-файла (.xml)
 */
class Animation() {
    /**
     * Список кадров анимации
     */
    var frames: ArrayList<Frame> = ArrayList()
    /**
     * Длительность (период) анимации
     */
    var duration = 1000
    /**
     * Является ли анимация периодической (иначе она останавливается на последнем кадре)
     */
    var isRepeatable = true
    /**
     * Словарь из данных анимации: по moveDirection-у и WeaponType-у получаем список кадров
     */
    var data = HashMap<MoveDirection, HashMap<WeaponType, ArrayList<Frame>>>()

    /**
     * Конструктор считывания анимации из файла XML (.xml)
     */
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
                                    lyr.attributeValue("basicWidth").toInt(),
                                    lyr.attributeValue("basicHeight").toInt()
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


    /**
     * Кадр анимации
     */
    data class Frame(var layers: ArrayList<Layer> = ArrayList())

    /**
     * Слой на кадре анимации
     */
    data class Layer(var imageName: String, var x: Float = 0f, var y: Float = 0f, var scale: Float = 1f, var scaleX: Float = 1f,
                     var scaleY: Float = 1f, var angle: Float = 0f, var basicWidth: Int = 0, var basicHeight: Int = 0) {
        /**
         * Обрезает формат изображения и возвращает название слоя
         */
        fun getName() = imageName.substring(0, imageName.length - 4)

        /**
         * Возвращает точку - координаты слоя
         */
        fun getPosition(): Point {
            return Point(x, y)
        }

    }

}