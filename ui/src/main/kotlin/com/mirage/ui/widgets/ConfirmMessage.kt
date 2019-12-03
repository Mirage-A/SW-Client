package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

class ConfirmMessage(
        virtualScreen: VirtualScreen,
        title: String = "Confirm action",
        description: String = "This action needs to be confirmed",
        okTitle: String = "OK",
        cancelTitle: String = "Cancel"
) : Widget {

    var isVisible = false

    private val backgroundTextureName = "ui/message-background"
    private val titleFontCapHeight = 24f
    private val descriptionFontCapHeight = 16f
    private val okCancelFontCapHeight = 20f

    private val titleLabel = virtualScreen.createLabel(title, titleFontCapHeight)
    private val descriptionLabel = virtualScreen.createLabel(description, descriptionFontCapHeight)
    private val okButton = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel(okTitle, okCancelFontCapHeight),
            null
    )
    private val cancelButton = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel(cancelTitle, okCancelFontCapHeight),
            null
    )

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

    fun setOkAction(block: () -> Unit) {
        okButton.onPressed = block
    }

    fun setCancelAction(block: () -> Unit) {
        cancelButton.onPressed = block
    }

    fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        virtualScreen.draw(backgroundTextureName, 0f, 0f)
        titleLabel.draw()
        descriptionLabel.draw()
        okButton.draw(virtualScreen)
        cancelButton.draw(virtualScreen)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        titleLabel.rect = Rectangle(0f, 175f, 530f, 84f)
        descriptionLabel.rect = Rectangle(0f, 0f, 600f, 240f)
        okButton.rect = Rectangle(-176f, -185f, 260f, 86f)
        okButton.boundedLabel?.rect = okButton.rect
        cancelButton.rect = Rectangle(176f, -185f, 260f, 86f)
        cancelButton.boundedLabel?.rect = cancelButton.rect
    }

    override fun touchUp(virtualPoint: Point) {
        if (!isVisible) return
        okButton.touchUp(virtualPoint)
        cancelButton.touchUp(virtualPoint)
    }

    override fun touchDown(virtualPoint: Point) {
        if (!isVisible) return
        okButton.touchDown(virtualPoint)
        cancelButton.touchDown(virtualPoint)

    }

    override fun mouseMoved(virtualPoint: Point) {
        if (!isVisible) return
        okButton.mouseMoved(virtualPoint)
        cancelButton.mouseMoved(virtualPoint)

    }
}