package com.mirage.view.animation

import com.mirage.utils.Log
import com.mirage.utils.game.objects.MoveDirection
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream


/**
 * Анимация, созданная с помощью Animation Editor-а
 * Объект этого класса содержит всю информацию об анимации,
 * а также конструктор загрузки анимации из XML-файла (.swa)
 */
class Animation() {
    /**
     * Тип анимации
     */
    var type : AnimationType = AnimationType.NULL
    /**
     * Список кадров анимации
     */
    var frames : ArrayList<Frame> = ArrayList()
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
     * Конструктор считывания анимации из файла XML (.swa)
     */
    constructor(inputStream : InputStream?) : this() {
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
            type = AnimationType.fromString(animation.attributeValue("type"))
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
        }
        catch(ex: Exception) {
            Log.e("Unexpected error occurred:\n" + ex.message)
            System.exit(0)
        }
    }
}