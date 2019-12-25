package com.mirage.logic

import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.ReturnCode


data class PlayerTransferRequest(val entityID: EntityID, val gameMapName: GameMapName?, val returnCode: ReturnCode)