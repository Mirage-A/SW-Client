package com.mirage.ui.inventory

import com.mirage.gameview.drawers.templates.HumanoidDrawerTemplate
import com.mirage.ui.widgets.*
import com.mirage.utils.Assets
import com.mirage.utils.Log
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.properties.WeaponType
import com.mirage.utils.preferences.EquipmentSlot
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
private const val itemBtnsCount = 16


class InventoryWindow(virtualScreen: VirtualScreen, onClose: () -> Unit) : Widget {

    private var initialEquipment: Equipment = Prefs.profile.currentEquipment

    private var equipment: Equipment = initialEquipment.copy()

    private var selectedSlot: EquipmentSlot? = null

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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.HELMET) }
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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.CHEST) }
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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.LEGGINGS) }
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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.CLOAK) }
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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.MAIN_HAND) }
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
            },
            onPressed = { selectEquipmentSlot(EquipmentSlot.OFF_HAND) }
    )

    fun drawHelmetIcon(virtualScreen: VirtualScreen, helmet: String, rect: Rectangle) {
        val helmetTexture = "../equipment/head/$helmet/DOWN"
        virtualScreen.draw(helmetTexture, rect)
    }

    fun drawChestIcon(virtualScreen: VirtualScreen, chest: String, rect: Rectangle) {
        val bodyTexture = "../equipment/body/$chest/body"
        val handTopTexture = "../equipment/body/$chest/handtop"
        val handBottomTexture = "../equipment/body/$chest/handbottom"
        val neckTexture = "../equipment/body/$chest/neck"
        //TODO
        virtualScreen.draw(bodyTexture, rect)
    }

    fun drawLegsIcon(virtualScreen: VirtualScreen, legs: String, rect: Rectangle) {
        val legTopTexture = "../equipment/legs/$legs/legtop"
        val legBottomTexture = "../equipment/legs/$legs/legbottom"
        //TODO
        virtualScreen.draw(legBottomTexture, rect)
    }

    fun drawCloakIcon(virtualScreen: VirtualScreen, cloak: String, rect: Rectangle) {
        val cloakTexture = "../equipment/cloak/$cloak/cloak"
        //TODO
        virtualScreen.draw(cloakTexture, rect)
    }

    fun drawWeaponIcon(virtualScreen: VirtualScreen, weapon: String, rect: Rectangle) {
        val weaponTexture = "../equipment/weapon/$weapon/weapon"
        //TODO
        virtualScreen.draw(weaponTexture, rect)
    }

    fun drawNoneIcon(virtualScreen: VirtualScreen, mockString: String = "none", rect: Rectangle) {
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


    fun getItemBtnSize(virtualWidth: Float, virtualHeight: Float): Float =
            min(getBorderedBtnSize(virtualWidth), (virtualHeight - slotLabelHeight - pageNavigatorHeight/ 5 * equipmentBtnMargin) / 4f)

    val itemBtns = Array(itemBtnsCount) {
        Button(
                "ui/inventory/left-button-background",
                "ui/inventory/left-button-background-highlighted",
                "ui/inventory/left-button-background-pressed",
                sizeUpdater = {
                    w, h -> Rectangle(
                        getRightColumnCenterX(w) + getItemBtnSize(w, h) * (it % 4 - 1.5f) + equipmentBtnMargin * (it % 4 - 1.5f),
                        - (slotLabelHeight + pageNavigatorHeight) / 2f - getItemBtnSize(w, h) * (it / 4 - 1.5f) - equipmentBtnMargin * (it / 4 - 1.5f),
                        getItemBtnSize(w, h), getItemBtnSize(w, h)
                )
                }
        )
    }


    val rightColumn = CompositeWidget(*itemBtns, pageNavigator, selectedSlotLabel, rightBackground)


    var humanoidDrawer = HumanoidDrawerTemplate(equipment, humanoidScale)

    fun getSlotName(slot: EquipmentSlot?) = when (slot) {
        null -> "Inventory"
        EquipmentSlot.HELMET -> "Helmet"
        EquipmentSlot.CHEST -> "Chest"
        EquipmentSlot.LEGGINGS -> "Leggings"
        EquipmentSlot.CLOAK -> "Cloak"
        EquipmentSlot.MAIN_HAND -> "Main hand"
        EquipmentSlot.OFF_HAND -> "Off hand"
    }

    fun selectEquipmentSlot(slot: EquipmentSlot?) {
        if (slot == null || slot == selectedSlot) {
            selectedSlot = null
            pageNavigator.isVisible = false
            itemBtns.forEach { it.isVisible = false }
            selectedSlotLabel.text = "Inventory"
            return
        }
        selectedSlot = slot
        selectedSlotLabel.text = getSlotName(slot)
        pageNavigator.isVisible = true
        pageNavigator.pageIndex = 0
        val availableItems = Prefs.account.availableEquipment[slot]
        if (availableItems == null) {
            selectEquipmentSlot(null)
            return
        }
        val itemsCount = availableItems.size
        pageNavigator.pageCount = (itemsCount - 1) / itemsCount + 1
        val loadPage: (Int) -> Unit = {
            val startIndex = it * itemBtnsCount
            for (i in 0 until itemBtnsCount) {
                with (itemBtns[i]) {
                    isVisible = if (startIndex + i < itemsCount) {
                        onPressed = { openItemMessage(slot, availableItems[startIndex + i]) }
                        true
                    }
                    else false
                }
            }
        }
        pageNavigator.onPageSwitch = loadPage
        loadPage(0)
    }

    val itemMessage = ConfirmMessage(
            virtualScreen, "Title", "description", "Equip", "Cancel", true
    ).apply {
        setCancelAction { isVisible = false }
    }

    fun openItemMessage(itemType: EquipmentSlot, itemName: String) {
        with (itemMessage) {
            setOkAction {
                when (itemType) {
                    EquipmentSlot.HELMET -> equipment = equipment.copy(helmet = itemName)
                    EquipmentSlot.CHEST -> equipment = equipment.copy(chest = itemName)
                    EquipmentSlot.LEGGINGS -> equipment = equipment.copy(legs = itemName)
                    EquipmentSlot.CLOAK -> equipment = equipment.copy(cloak = itemName)
                    EquipmentSlot.MAIN_HAND -> {
                        val weaponType = Assets.loadEquipmentData(itemType, itemName).weaponType ?: WeaponType.ONE_HANDED
                        val isTwoHanded = weaponType.isTwoHanded()
                        equipment = if (isTwoHanded) {
                            equipment.copy(mainHand = itemName, offHand = "null", weaponType = weaponType)
                        }
                        else if (equipment.weaponType.isTwoHanded()) {
                            equipment.copy(mainHand = itemName, offHand = itemName, weaponType = WeaponType.DUAL)
                        }
                        else {
                            equipment.copy(mainHand = itemName)
                        }
                    }
                    EquipmentSlot.OFF_HAND -> {
                        val weaponType = Assets.loadEquipmentData(itemType, itemName).weaponType ?: WeaponType.ONE_HANDED
                        val isTwoHanded = weaponType in arrayOf(WeaponType.TWO_HANDED, WeaponType.BOW, WeaponType.STAFF)
                        if (isTwoHanded) {
                            Log.e("Error: Two handed weapons must not be visible in off hand page")
                        }
                        else if (equipment.weaponType.isTwoHanded()) {
                            equipment = equipment.copy(mainHand = itemName, offHand = itemName, weaponType = WeaponType.DUAL)
                        }
                        else if (weaponType == WeaponType.SHIELD) {
                            equipment = equipment.copy(offHand = itemName, weaponType = WeaponType.SHIELD)
                        }
                        else {
                            equipment = equipment.copy(offHand = itemName, weaponType = WeaponType.DUAL)
                        }
                    }
                }
                isVisible = false
                humanoidDrawer = HumanoidDrawerTemplate(equipment, humanoidScale)
            }
            isVisible = true
        }
    }


    val compositeWidget = CompositeWidget(itemMessage, leftColumnBtns, centerColumn, leftColumn, rightColumn)

    fun open() {
        initialEquipment = Prefs.profile.currentEquipment
        equipment = initialEquipment.copy()
        selectEquipmentSlot(null)
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
            WeaponType.UNARMED -> ::drawNoneIcon
            else -> ::drawWeaponIcon
        }
        val offDrawer: (VirtualScreen, String, Rectangle) -> Unit = when (equipment.weaponType) {
            WeaponType.UNARMED, WeaponType.ONE_HANDED, WeaponType.TWO_HANDED, WeaponType.STAFF, WeaponType.BOW -> ::drawNoneIcon
            WeaponType.SHIELD, WeaponType.DUAL -> ::drawWeaponIcon
        }
        mainDrawer(virtualScreen, equipment.mainHand, mainRect)
        offDrawer(virtualScreen, equipment.offHand, offRect)
        rightColumn.draw(virtualScreen)

        val items = Prefs.account.availableEquipment[selectedSlot]
        val drawer = when (selectedSlot) {
            null -> ::drawNoneIcon
            EquipmentSlot.HELMET -> ::drawHelmetIcon
            EquipmentSlot.CHEST -> ::drawChestIcon
            EquipmentSlot.LEGGINGS -> ::drawLegsIcon
            EquipmentSlot.CLOAK -> ::drawCloakIcon
            EquipmentSlot.MAIN_HAND, EquipmentSlot.OFF_HAND -> ::drawWeaponIcon
        }
        if (items != null) {
            val itemsCount = items.size
            for (i in 0 until itemBtnsCount) {
                val itemIndex = i + itemBtnsCount * pageNavigator.pageIndex
                if (i >= itemsCount) break
                drawer(virtualScreen, items[itemIndex], itemBtns[i].rect.innerRect(16f))
            }
        }

        centerColumn.draw(virtualScreen)
        itemMessage.draw(virtualScreen)

    }
}