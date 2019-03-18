package com.mirage.admin

import com.mirage.server.Room
import java.awt.*
import javax.swing.*

class AdminGUI(rooms: List<Room>) : JFrame() {

    private var selectedRoom: Room? = null

    /**
     * Столбец 1: Статус сервера, комнаты, список комнат
     */

    private val serverButton = JButton().apply {
        text = "Server: Offline"
        foreground = Color.RED
    }

    private val roomsLabel = JLabel().apply {
        text = "Rooms: 0"
        horizontalAlignment = JLabel.CENTER
    }

    private val roomsList = JList<String>(object: AbstractListModel<String>() {
        override fun getElementAt(index: Int): String = rooms[index].toString()
        override fun getSize(): Int = rooms.size
    }).apply {

    }

    private val roomsScrollPane = JScrollPane(roomsList).apply {
        preferredSize = Dimension(190, 131)
    }

    /**
     * Столбец 2: игроки онлайн, игроки в комнате, список игроков в комнате
     */

    private val playersOnlineLabel = JLabel().apply {
        text = "Players online: 0"
        horizontalAlignment = JLabel.CENTER
    }

    private val playersInRoomLabel = JLabel().apply {
        text = "Players in room: 0"
        horizontalAlignment = JLabel.CENTER
    }

    private val playersList = JList<String>(object: AbstractListModel<String>() {
        override fun getElementAt(index: Int): String = selectedRoom?.getPlayerByIndex(index).toString()
        override fun getSize(): Int = selectedRoom?.getPlayersCount() ?: 0
    }).apply {
        println(minimumSize)
        println(preferredSize)
    }

    private val playersScrollPane = JScrollPane(playersList).apply {
        preferredSize = Dimension(190, 131)
    }


    private val rootPanel = JPanel().apply {
        val defaultInsets = Insets(5, 5, 5, 5)
        val buttonsWeightX = 0.1
        val buttonsWeightY = 0.1
        val viewWeightX = 4.0
        val viewHeightY = 10.0
        val scrollPaneWeightY = 15.0
        layout = GridBagLayout()

        add(serverButton, GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            gridwidth = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = buttonsWeightY
            insets = defaultInsets
        })
        add(roomsLabel, GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            gridwidth = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = buttonsWeightY
            insets = defaultInsets
        })
        add(roomsScrollPane, GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            gridwidth = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = scrollPaneWeightY
            insets = defaultInsets
        })

        add(playersOnlineLabel, GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            gridwidth = 1
            gridheight = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = buttonsWeightY
            insets = defaultInsets
        })
        add(playersInRoomLabel, GridBagConstraints().apply {
            gridx = 1
            gridy = 1
            gridwidth = 1
            gridheight = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = buttonsWeightY
            insets = defaultInsets
        })
        add(playersScrollPane, GridBagConstraints().apply {
            gridx = 1
            gridy = 2
            gridwidth = 1
            fill = GridBagConstraints.BOTH
            weightx = buttonsWeightX
            weighty = scrollPaneWeightY
            insets = defaultInsets
        })

        add(JButton(), GridBagConstraints().apply {
            gridx = 2
            gridy = 0
            gridwidth = 5
            gridheight = 5
            fill = GridBagConstraints.BOTH
            weightx = viewWeightX
            weighty = viewHeightY
            insets = defaultInsets
        })
    }

    init {
        title = "Shattered World: Administrator"
        setSize(800, 600)
        setLocation(1600 / 2 - 800 / 2, 900 / 2 - 600 / 2)
        contentPane = rootPanel
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

}
