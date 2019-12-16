package com.mirage.gamelogic

import com.mirage.utils.extensions.EntityID
import com.mirage.utils.extensions.GameMapName
import com.mirage.utils.extensions.ReturnCode


data class PlayerTransferRequest(val entityID: EntityID, val gameMapName: GameMapName, val returnCode: ReturnCode)