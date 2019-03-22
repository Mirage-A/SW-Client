package com.mirage.admin

import java.awt.Component
import java.awt.Container
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.AbstractListModel

fun Component.resizeListener(listener: (ComponentEvent?) -> Unit) =
        addComponentListener(object: ComponentListener {

    override fun componentResized(e: ComponentEvent?) = listener(e)

    override fun componentMoved(e: ComponentEvent?) = Unit

    override fun componentHidden(e: ComponentEvent?) = Unit

    override fun componentShown(e: ComponentEvent?) = Unit

})

fun Container.addAll(vararg args: Component) {
    for (comp in args) {
        this.add(comp)
    }
}