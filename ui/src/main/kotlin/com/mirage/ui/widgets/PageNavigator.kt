package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.virtualscreen.VirtualScreen

/** This widget implements navigation through multiple pages */
internal class PageNavigator(
        initialPageIndex: Int,
        initialPageCount: Int,
        /** Previous page button. onPressed will be set by PageNavigator */
        val leftButton: Button,
        /** Next page button. onPressed will be set by PageNavigator */
        val rightButton: Button,
        /** Label displaying current page and pages count */
        val pageTextLabel: LabelWidget,
        /** This method is invoked when user switches current page */
        var onPageSwitch: ((newPageIndex: Int) -> Unit)? = null,
        override var isVisible: Boolean = true
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
        pageTextLabel.text = "Page ${pageIndex + 1}/$pageCount"
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

    override fun resize(virtualWidth: Float, virtualHeight: Float) = compositeWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && compositeWidget.touchUp(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && compositeWidget.touchDown(virtualPoint)

    override fun mouseMoved(virtualPoint: Point): Boolean = isVisible && compositeWidget.mouseMoved(virtualPoint)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) compositeWidget.draw(virtualScreen)
    }

}