package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

internal class ConfirmMessage(
        virtualScreen: VirtualScreen,
        title: String = "Confirm action",
        description: String = "This action needs to be confirmed",
        okTitle: String = "OK",
        cancelTitle: String = "Cancel",
        private val blocksFocus: Boolean = false,
        override var isVisible: Boolean = false
) : Widget {

    var title: String
        get() = titleLabel.text
        set(value) {
            titleLabel.text = value
        }

    var description: String
        get() = descriptionLabel.text
        set(value) {
            descriptionLabel.text = value
        }

    private val titleFontCapHeight = 24f
    private val descriptionFontCapHeight = 16f
    private val okCancelFontCapHeight = 20f

    private val background = ImageWidget(
            textureName = "ui/message-background",
            sizeUpdater = {
                _, _ -> Rectangle(0f, 0f, 800f, 500f)
            })

    private val titleLabel = LabelWidget(
            virtualScreen = virtualScreen,
            text = title,
            fontCapHeight = titleFontCapHeight,
            sizeUpdater = {
                _, _ -> Rectangle(0f, 175f, 530f, 84f)
            }
    )

    private val descriptionLabel = LabelWidget(
            virtualScreen = virtualScreen,
            text = description,
            fontCapHeight = descriptionFontCapHeight,
            sizeUpdater = {
                _, _ -> Rectangle(0f, 0f, 600f, 240f)
            }
    )

    private val okButton = Button(
            boundedLabel = LabelWidget(virtualScreen, okTitle, okCancelFontCapHeight),
            sizeUpdater = {
                _, _ -> Rectangle(-176f, -185f, 260f, 86f)
            }
    )

    private val cancelButton = Button(
            boundedLabel = LabelWidget(virtualScreen, cancelTitle, okCancelFontCapHeight),
            sizeUpdater = {
                _, _ -> Rectangle(176f, -185f, 260f, 86f)
            }
    )


    private val compositeWidget = CompositeWidget(okButton, cancelButton, titleLabel, descriptionLabel, background)

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

    fun setOkAction(block: () -> Unit) {
        okButton.onPressed = block
    }

    fun setCancelAction(block: () -> Unit) {
        cancelButton.onPressed = block
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) compositeWidget.draw(virtualScreen)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        compositeWidget.resize(virtualWidth, virtualHeight)
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        val processed = compositeWidget.touchUp(virtualPoint)
        return processed || blocksFocus
    }


    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        val processed = compositeWidget.touchDown(virtualPoint)
        return processed || blocksFocus
    }


    override fun mouseMoved(virtualPoint: Point): Boolean =
            isVisible && compositeWidget.mouseMoved(virtualPoint)

}