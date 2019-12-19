package com.mirage.ui.inventory

import com.badlogic.gdx.graphics.g2d.PixmapPacker
import com.mirage.gameview.drawers.templates.HumanoidDrawerTemplate
import com.mirage.ui.widgets.*
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.properties.WeaponType
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.min

private const val saveCancelBtnHeight = 80f
private const val saveCancelBtnMargin = 40f
private const val centerColumnWidthPart = 0.25f
private const val equipmentPartIconSize = 128f
private const val equipmentPartIconBorderSize = 8f
private const val equipmentPartBorderedBtnSize = equipmentPartIconSize + 2f * equipmentPartIconBorderSize
private const val equipmentBtnMargin = 16f
private const val itemBtnSize = 128f
private const val leftBackgroundTextureWidth = 900f
private const val humanoidScale = 4f
private const val humanoidSize = humanoidScale * 128f
private const val slotLabelHeight = 64f
private const val pageNavigatorHeight = 64f
private const val arrowSize = 50f
private const val arrowMargin = (pageNavigatorHeight - arrowSize) / 2f


class InventoryWindow(virtualScreen: VirtualScreen, onClose: () -> Unit) : Widget {

    private var initialEquipment: Equipment = Prefs.profile.currentEquipment

    private var equipment: Equipment = initialEquipment.copy()

    val centerBackground = ImageWidget("ui/inventory/inventory-center-background") {
        w, h -> Rectangle(0f, 0f, w * centerColumnWidthPart, h)
    }

    val saveBtn = Button(
            "ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Save", 30f),
            {
                w, h -> Rectangle(0f, - h / 2f + saveCancelBtnMargin + saveCancelBtnHeight * 1.5f,
                    w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
            },
            {
                isVisible = false
                Prefs.profile.currentEquipment = equipment
                Prefs.saveCurrentProfile()
                onClose()
            }
    )

    val cancelBtn = Button(
            "ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Cancel", 30f),
            {
                w, h -> Rectangle(0f, - h / 2f + saveCancelBtnMargin + saveCancelBtnHeight / 2f,
                    w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
            },
            {
                isVisible = false
                onClose()
            }
    )

    val centerColumn = CompositeWidget(saveBtn, cancelBtn, centerBackground)

    private fun getLeftColumnCenterX(virtualWidth: Float): Float =
            - virtualWidth * centerColumnWidthPart / 2f - (virtualWidth / 2f - virtualWidth * centerColumnWidthPart / 2f) / 2f

    private fun getRightColumnCenterX(virtualWidth: Float): Float = - getLeftColumnCenterX(virtualWidth)

    private fun getBorderedBtnSize(virtualWidth: Float): Float =
            min(equipmentPartBorderedBtnSize, (virtualWidth * (1f - centerColumnWidthPart) / 2f - 5f * equipmentBtnMargin) / 4f)

    private fun getRightColumnWidth(virtualWidth: Float): Float =
            virtualWidth * (1f - centerColumnWidthPart) / 2f

    val leftBackground = ImageWidget("ui/inventory/left-background") {
        w, h -> Rectangle(getLeftColumnCenterX(w), 0f, leftBackgroundTextureWidth, h)
    }


    val helmetBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) - getBorderedBtnSize(w) * 1.5f - equipmentBtnMargin * 1.5f,
                    - h / 2f + getBorderedBtnSize(w) / 2f + equipmentBtnMargin,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
                )
            }
    )

    val chestBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) - getBorderedBtnSize(w) * 0.5f - equipmentBtnMargin / 2f,
                    - h / 2f + getBorderedBtnSize(w) / 2f + equipmentBtnMargin,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
            )
            }
    )

    val legsBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) + getBorderedBtnSize(w) * 0.5f + equipmentBtnMargin / 2f,
                    - h / 2f + getBorderedBtnSize(w) / 2f + equipmentBtnMargin,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
            )
            }
    )

    val cloakBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) + getBorderedBtnSize(w) * 1.5f + equipmentBtnMargin * 1.5f,
                    - h / 2f + getBorderedBtnSize(w) / 2f + equipmentBtnMargin,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
            )
            }
    )

    val mainHandBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) - getBorderedBtnSize(w) * 0.5f - equipmentBtnMargin / 2f,
                    - h / 2f + getBorderedBtnSize(w) * 1.5f + equipmentBtnMargin * 2f,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
            )
            }
    )

    val offHandBtn = Button(
            "ui/inventory/left-button-background",
            "ui/inventory/left-button-background-highlighted",
            "ui/inventory/left-button-background-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getLeftColumnCenterX(w) + getBorderedBtnSize(w) * 0.5f + equipmentBtnMargin / 2f,
                    - h / 2f + getBorderedBtnSize(w) * 1.5f + equipmentBtnMargin * 2f,
                    getBorderedBtnSize(w), getBorderedBtnSize(w)
            )
            }
    )

    fun drawHelmetIcon(virtualScreen: VirtualScreen, helmet: String, rect: Rectangle) {
        val helmetTexture = "equipment/head/$helmet/DOWN"
        virtualScreen.draw(helmetTexture, rect)
    }

    fun drawChestIcon(virtualScreen: VirtualScreen, chest: String, rect: Rectangle) {
        val bodyTexture = "equipment/body/$chest/body"
        val handTopTexture = "equipment/body/$chest/handtop"
        val handBottomTexture = "equipment/body/$chest/handbottom"
        val neckTexture = "equipment/body/$chest/neck"
        //TODO
        virtualScreen.draw(bodyTexture, rect)
    }

    fun drawLegsIcon(virtualScreen: VirtualScreen, legs: String, rect: Rectangle) {
        val legTopTexture = "equipment/legs/$legs/legtop"
        val legBottomTexture = "equipment/legs/$legs/legbottom"
        //TODO
        virtualScreen.draw(legBottomTexture, rect)
    }

    fun drawCloakIcon(virtualScreen: VirtualScreen, cloak: String, rect: Rectangle) {
        val cloakTexture = "equipment/cloak/$cloak/cloak"
        //TODO
        virtualScreen.draw(cloakTexture, rect)
    }

    fun drawOneHandedIcon(virtualScreen: VirtualScreen, oneHanded: String, rect: Rectangle) {
        val oneHandedTexture = "equipment/onehanded/$oneHanded"
        //TODO
        virtualScreen.draw(oneHandedTexture, rect)
    }

    fun drawTwoHandedIcon(virtualScreen: VirtualScreen, twoHanded: String, rect: Rectangle) {
        val twoHandedTexture = "equipment/twohanded/$twoHanded"
        //TODO
        virtualScreen.draw(twoHandedTexture, rect)
    }

    fun drawNoneTexture(virtualScreen: VirtualScreen, mockString: String = "none", rect: Rectangle) {
        virtualScreen.draw("ui/inventory/no-selection", rect)
    }



    val leftColumn = CompositeWidget(leftBackground)

    val leftColumnBtns = CompositeWidget(helmetBtn, chestBtn, legsBtn, cloakBtn, mainHandBtn, offHandBtn)

    val rightBackground = ImageWidget("ui/inventory/right-background") {
        w, h -> Rectangle(getRightColumnCenterX(w), 0f, w * (1f - centerColumnWidthPart) / 2f, h)
    }

    val selectedSlotLabel = LabelWidget(virtualScreen.createLabel("Inventory", 42f)) {
        w, h -> Rectangle(
            getRightColumnCenterX(w), h / 2f - slotLabelHeight / 2f,
            w * (1f - centerColumnWidthPart) / 2f, slotLabelHeight
            )
    }

    val leftArrow = Button(
            textureName = "ui/inventory/left-arrow",
            sizeUpdater = {
                w, h -> Rectangle(
                    getRightColumnCenterX(w) - getRightColumnWidth(w) / 2f + arrowMargin + arrowSize / 2f,
                    h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
                    arrowSize, arrowSize
                    )
            }
    )

    val rightArrow = Button(
            textureName = "ui/inventory/right-arrow",
            sizeUpdater = {
                w, h -> Rectangle(
                    getRightColumnCenterX(w) + getRightColumnWidth(w) / 2f - arrowMargin - arrowSize / 2f,
                    h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
                    arrowSize, arrowSize
                    )
            }
    )

    val pageLabel = LabelWidget(virtualScreen.createLabel("", 32f)) {
        w, h -> Rectangle(
            getRightColumnCenterX(w), h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
            getRightColumnWidth(w), pageNavigatorHeight
            )
    }

    val pageNavigator = PageNavigator(0, 4, leftArrow, rightArrow, pageLabel)


    val rightColumn = CompositeWidget(pageNavigator, selectedSlotLabel, rightBackground)

    val compositeWidget = CompositeWidget(leftColumnBtns, centerColumn, leftColumn, rightColumn)

    var humanoidDrawer = HumanoidDrawerTemplate(equipment, humanoidScale) //TODO Zoom

    fun open() {
        //TODO
        initialEquipment = Prefs.profile.currentEquipment
        equipment = initialEquipment.copy()
        isVisible = true
    }

    override var isVisible: Boolean = false

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            compositeWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point): Boolean =
            if (isVisible) compositeWidget.touchUp(virtualPoint) else false

    override fun touchDown(virtualPoint: Point): Boolean =
            if (isVisible) compositeWidget.touchDown(virtualPoint) else false

    override fun mouseMoved(virtualPoint: Point) {
        if (isVisible) compositeWidget.mouseMoved(virtualPoint)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        val w = virtualScreen.width
        val h = virtualScreen.height
        leftBackground.draw(virtualScreen)
        humanoidDrawer.draw(
                virtualScreen,
                - w * centerColumnWidthPart / 2f - (w / 2f - w * centerColumnWidthPart / 2f) / 2f,
                - humanoidSize / 2f,
                true,
                "idle",
                0L,
                false,
                0L,
                MoveDirection.RIGHT
        )
        leftColumnBtns.draw(virtualScreen)
        drawHelmetIcon(virtualScreen, equipment.helmet, helmetBtn.rect.innerRect(16f))
        drawChestIcon(virtualScreen, equipment.chest, chestBtn.rect.innerRect(16f))
        drawLegsIcon(virtualScreen, equipment.legs, legsBtn.rect.innerRect(16f))
        drawCloakIcon(virtualScreen, equipment.cloak, cloakBtn.rect.innerRect(16f))
        val mainRect = mainHandBtn.rect.innerRect(16f)
        val offRect = offHandBtn.rect.innerRect(16f)
        val mainDrawer: (VirtualScreen, String, Rectangle) -> Unit = when (equipment.weaponType) {
            WeaponType.UNARMED -> ::drawNoneTexture
            WeaponType.ONE_HANDED, WeaponType.ONE_HANDED_AND_SHIELD, WeaponType.DUAL -> ::drawOneHandedIcon
            WeaponType.TWO_HANDED, WeaponType.STAFF, WeaponType.BOW -> ::drawTwoHandedIcon
        }
        val offDrawer: (VirtualScreen, String, Rectangle) -> Unit = when (equipment.weaponType) {
            WeaponType.UNARMED, WeaponType.ONE_HANDED, WeaponType.TWO_HANDED, WeaponType.STAFF, WeaponType.BOW -> ::drawNoneTexture
            WeaponType.ONE_HANDED_AND_SHIELD, WeaponType.DUAL -> ::drawOneHandedIcon
        }
        mainDrawer(virtualScreen, equipment.rightWeapon, mainRect)
        offDrawer(virtualScreen, equipment.leftWeapon, offRect)
        rightColumn.draw(virtualScreen)
        centerColumn.draw(virtualScreen)
    }
}