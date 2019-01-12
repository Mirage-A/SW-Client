package com.mirage.view.animation

import java.io.File
import org.dom4j.io.SAXReader
import org.dom4j.DocumentHelper
import org.dom4j.Element
import java.io.FileWriter
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import javax.swing.JOptionPane


/**
 * Основной класс модели
 * Абстрактная модель, хранящая все данные об анимации
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
     * Номер текущего кадра анимации
     */
    var curFrame : Int = -1
    /**
     * Текущее направление движения
     */
    var curMoveDirection = MoveDirection.RIGHT
    /**
     * Текущий тип оружия
     */
    var curWeaponType = WeaponType.UNARMED
    /**
     * Название анимации
     */
    var name : String = "NO_NAME"
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

    constructor(type: AnimationType) : this() {
        this.type = type
    }

    /**
     * Конструктор считывания анимации из файла XML (.swa)
     */
    constructor(swaFile : File) : this() {
        try {
            for (moveDirection in MoveDirection.values()) {
                data[moveDirection] = HashMap()
                for (weaponType in WeaponType.values()) {
                    data[moveDirection]!![weaponType] = ArrayList()
                }
            }
            val reader = SAXReader()
            val document = reader.read(swaFile)
            val animation = document.rootElement
            type = AnimationType.fromString(animation.attributeValue("type"))
            name = animation.attributeValue("name")
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
            JOptionPane.showMessageDialog(null, "Unexpected error occurred:\n" + ex.message, "Error :(", JOptionPane.ERROR_MESSAGE)
            System.exit(0)
        }
    }
}