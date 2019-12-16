package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.virtualscreen.VirtualScreen

class PageNavigator(
        initialPageIndex: Int,
        initialPageCount: Int,
        val leftButton: Button,
        val rightButton: Button,
        val pageTextLabel: LabelWidget,
        var onPageSwitch: ((Int) -> Unit)? = null
) : Widget {

    val compositeWidget = CompositeWidget(leftButton, rightButton, pageTextLabel)

    var pageIndex = initialPageIndex
        set(value) {
            field = value
            update()
        }

    var pageCount = initialPageCount
        set(value) {
            field = value
            update()
        }

    private fun update() {
        pageTextLabel.label.text = "Page ${pageIndex + 1}/$pageCount"
        leftButton.isVisible = pageIndex > 0
        rightButton.isVisible = pageIndex < pageCount - 1
    }

    init {
        leftButton.onPressed = {
            if (pageIndex > 0) {
                --pageIndex
                onPageSwitch?.invoke(pageIndex)
            }
        }
        rightButton.onPressed = {
            if (pageIndex < pageCount - 1) {
                ++pageIndex
                onPageSwitch?.invoke(pageIndex)
            }
        }
        update()
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) { compositeWidget.resize(virtualWidth, virtualHeight) }

    override fun touchUp(virtualPoint: Point): Boolean = compositeWidget.touchUp(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean = compositeWidget.touchDown(virtualPoint)

    override fun mouseMoved(virtualPoint: Point) { compositeWidget.mouseMoved(virtualPoint) }

    override fun draw(virtualScreen: VirtualScreen) { compositeWidget.draw(virtualScreen) }

    override fun unpress() {
        compositeWidget.unpress()
    }

}