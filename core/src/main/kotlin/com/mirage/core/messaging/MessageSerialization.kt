package com.mirage.core.messaging

import com.google.gson.Gson
import com.mirage.core.utils.*

private val gson = Gson()

fun serializeServerMessage(msg: ServerMessage): String {
    val clazz = msg::class.java
    val code = ServerMessage.classToCodeMap[clazz] ?: return run {
        Log.e("Error: class $clazz is not included in ServerMessage.serverMessageClasses list")
        ""
    }
    return serializeMessage(code, msg)
}

fun serializeClientMessage(msg: ClientMessage): String {
    val clazz = msg::class.java
    val code = ClientMessage.classToCodeMap[clazz] ?: return run {
        Log.e("Error: class $clazz is not included in ClientMessage.clientMessageClasses list")
        ""
    }
    return serializeMessage(code, msg)
}

private fun serializeMessage(code: Int, msg: Any): String {
    val serializedMessage = gson.toJson(msg)
    val filteredMessage = serializedMessage.filterNot { it == INNER_DLMTR || it == OUTER_DLMTR || it == MAP_OBJ_DLMTR || it == PROPS_DLMTR }
    if (serializedMessage != filteredMessage) {
        Log.e("Serialized message contained special characters: $msg")
    }
    return "$code$INNER_DLMTR$filteredMessage"
}

fun deserializeServerMessage(serializedMessage: String): ServerMessage? {
    val codeAndMsg = serializedMessage.split(INNER_DLMTR)
    if (codeAndMsg.size != 2) {
        Log.e("Error: received string is not a serialized message: $serializedMessage")
        return null
    }
    return try {
        val code = codeAndMsg[0].toInt()
        val msgString = codeAndMsg[1]
        val clazz = ServerMessage.codeToClassMap[code]
        val msg = gson.fromJson<ServerMessage>(msgString.reader(), clazz)
        msg as ServerMessage
    } catch (ex: Exception) {
        Log.e("Error: received string is not a serialized message: $serializedMessage")
        null
    }
}

fun deserializeClientMessage(serializedMessage: String): ClientMessage? {
    val codeAndMsg = serializedMessage.split(INNER_DLMTR)
    if (codeAndMsg.size != 2) {
        Log.e("Error: received string is not a serialized message: $serializedMessage")
        return null
    }
    return try {
        val code = codeAndMsg[0].toInt()
        val msgString = codeAndMsg[1]
        val clazz = ClientMessage.codeToClassMap[code]
        val msg = gson.fromJson<ClientMessage>(msgString.reader(), clazz)
        msg as ClientMessage
    } catch (ex: Exception) {
        Log.e("Error: received string is not a serialized message: $serializedMessage")
        null
    }
}