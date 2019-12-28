package com.mirage.admin

/*
import com.mirage.server.Room
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


internal class AdminGUI(private val rooms: List<Room>) : JFrame() {

    fun updateRoomsList() {
        roomsLabel.text = "Rooms: ${rooms.size}"
        roomsListModel.update()
        roomsList.revalidate()
        roomsScrollPane.revalidate()
    }

    fun updatePlayersOnlineCount() {
        var count = 0
        for (room in rooms) {
            count += room.getPlayersCount()
        }
        playersOnlineLabel.text = "Players online: $count"
    }

    fun updatePlayersInRoomList() {
        playersInRoomLabel.text = "Players in room: ${roomsList.selectedValue?.getPlayersCount() ?: 0}"
        playersListModel.update()
        playersList.revalidate()
        playersScrollPane.revalidate()
    }

    fun printInTerminal(text: String) {
        terminalTextArea.append(text)
        terminalScrollPane.revalidate()
    }

    fun printlnInTerminal(text: String) {
        printInTerminal("\n" + text)
    }

    fun printError(errorText: String) = printlnInTerminal(errorText) //TODO Red color

    fun clearTerminal() {
        terminalTextArea.text = ""
    }

    fun clearInput() {
        terminalInput.text = ""
    }

    fun setTerminalInputListener(listener: (String) -> Unit) {
        terminalInput.addKeyListener(object: KeyListener {
            override fun keyTyped(e: KeyEvent?) { }

            override fun keyPressed(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER) {
                    listener(terminalInput.text)
                }
            }

            override fun keyReleased(e: KeyEvent?) { }

        })
    }

    private val guiFont = Font("Comic Sans MS", Font.BOLD, 12)
    private val terminalFont = Font("Comic Sans MS", Font.PLAIN, 16)

    /**
     * Столбец 1: Статус сервера, комнаты, список комнат
     */

    private val serverStatusLabel = JLabel().apply {
        text = "Server: Online"
        foreground = Color(0, 202, 0)
        font = guiFont
        horizontalAlignment = JLabel.CENTER
    }

    private val roomsLabel = JLabel().apply {
        text = "Rooms: 0"
        font = guiFont
        horizontalAlignment = JLabel.CENTER
    }

    private val roomsListModel = object: AbstractListModel<Room>() {
        override fun getElementAt(index: Int): Room = rooms[index]
        override fun getSize(): Int = rooms.size
        fun update() = fireContentsChanged(this, 0, size - 1)
    }

    private val roomsList : JList<Room> = JList<Room>(roomsListModel).apply list@{
        selectionMode = DefaultListSelectionModel.SINGLE_SELECTION
        addListSelectionListener {
            playersListModel.update()
            updatePlayersInRoomList()
        }
    }

    private val roomsScrollPane = JScrollPane(roomsList).apply {
        preferredSize = Dimension(160, 131)
    }

    /**
     * Столбец 2: игроки онлайн, игроки в комнате, список игроков в комнате
     */

    private val playersOnlineLabel = JLabel().apply {
        text = "Players online: 0"
        font = guiFont
        horizontalAlignment = JLabel.CENTER
    }

    private val playersInRoomLabel = JLabel().apply {
        text = "Players in room: 0"
        font = guiFont
        horizontalAlignment = JLabel.CENTER
    }

    private val playersListModel = object : AbstractListModel<String>() {
        override fun getElementAt(index: Int): String = roomsList.selectedValue?.getPlayerByIndex(index)?.toString() ?: "Undefined"
        override fun getSize(): Int = roomsList.selectedValue?.getPlayersCount() ?: 0
        fun update() {
            fireContentsChanged(this, 0, size - 1)
        }
    }

    private val playersList = JList<String>(playersListModel)

    private val playersScrollPane = JScrollPane(playersList).apply {
        preferredSize = Dimension(160, 131)
    }

    /**
     * Столбец 3: панель с отображением карты комнаты и терминал администратора
     */

    private val viewPanel = JPanel().apply {

    }

    private val terminalTextArea = JTextArea().apply {
        isEditable = false
        font = terminalFont
        text = "Welcome to the terminal, Administrator!\n" +
                "Type 'help' to get list of available commands."
    }

    private var autoScroll = true

    private val terminalScrollPane = JScrollPane(terminalTextArea).apply {
        verticalScrollBar.addAdjustmentListener { e ->
            if (e.valueIsAdjusting) {
                autoScroll = e.adjustable.value + e.adjustable.visibleAmount == e.adjustable.maximum
            }
            else if (autoScroll) {
                e.adjustable.value = e.adjustable.maximum
            }
        }
    }

    private val terminalInput = JTextField().apply {
        font = terminalFont
    }

    private val inputH = 32
    private val terminalH = 320

    private val leftPanel = JPanel().apply {

        layout = null

        addAll(serverStatusLabel, roomsLabel, roomsScrollPane, playersOnlineLabel, playersInRoomLabel, playersScrollPane)

        resizeListener {
            val x1 = 4
            val x2 = width / 2 + 2
            val width2 = width / 2 - 6
            val playersScrollY = height - inputH - terminalH - 8
            serverStatusLabel.setBounds(x1, 4, width2, 32)
            playersOnlineLabel.setBounds(x2, 4, width2, 32)
            roomsLabel.setBounds(x1, 40, width2, 32)
            playersInRoomLabel.setBounds(x2, 40, width2, 32)
            roomsScrollPane.setBounds(x1, 76, width - 4, playersScrollY - 80)
            playersScrollPane.setBounds(x1, playersScrollY, width - 4, height - playersScrollY - 4)
        }
    }

    private val rightPanel = JPanel().apply {

        layout = null

        addAll(viewPanel, terminalScrollPane, terminalInput)

        resizeListener {
            terminalInput.setBounds(4, height - inputH - 4, width - 8, inputH)
            terminalScrollPane.setBounds(4, terminalInput.y - 4 - terminalH, width - 8, terminalH)
            viewPanel.setBounds(4, 4, width - 8, terminalScrollPane.y - 8)
            revalidate()
        }
    }

    private val rootPanel = JPanel().apply root@ {

        layout = null

        addAll(leftPanel, rightPanel)

        resizeListener {
            leftPanel.setBounds(0, 0, 280, height)
            rightPanel.setBounds(leftPanel.width, 0, width - leftPanel.width, height)
            revalidate()
            repaint()
        }
    }

    init {
        title = "Shattered World: Administrator"
        setSize(800, 600)
        val screen = Toolkit.getDefaultToolkit().screenSize
        setLocation(screen.width / 2 - 800 / 2, screen.height / 2 - 600 / 2)
        contentPane = rootPanel
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

}*/