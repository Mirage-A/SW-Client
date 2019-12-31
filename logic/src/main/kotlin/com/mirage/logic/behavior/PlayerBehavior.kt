package com.mirage.logic.behavior

import com.mirage.core.game.objects.properties.PlayerAttributes
import com.mirage.core.messaging.*
import com.mirage.core.utils.*
import com.mirage.logic.data.LogicData
import com.mirage.logic.processors.moveObject
import com.mirage.logic.processors.runScript
import com.mirage.logic.state.ExtendedEntity
import org.luaj.vm2.LuaValue
import kotlin.math.max

/** Represents player behavior. This implementation should be used for all player entities. */
internal class PlayerBehavior(
        private val id: EntityID,
        private val attributes: PlayerAttributes,
        private val skillNames: SkillNames,
        data: LogicData
) : Behavior {

    private val entity = data.state.entities[id] ?: ExtendedEntity()

    private var targetID: EntityID? = null

    // Time left until new attack can be started
    private var currentAttackCooldown: IntervalMillis = 0L
    // Time left until current attack is finished
    private var currentAttackDuration: IntervalMillis = 0L

    override fun onUpdate(delta: IntervalMillis, data: LogicData, coercedScriptActions: LuaValue) {
        currentAttackDuration = max(0L, currentAttackDuration - delta)
        currentAttackCooldown = max(0L, currentAttackCooldown - delta)
        with(data) {
            when (entity.action) {
                "idle", "running" -> {
                    // Auto attack
                    val target = data.state.entities[targetID]
                    //TODO range
                    if (
                            target != null &&
                            entity.factionID != target.factionID &&
                            currentAttackCooldown == 0L &&
                            entity.position..target.position < attributes.attackRange &&
                            entity.looksForward(target)
                    ) {
                        entity.action = "attack"
                        //TODO haste
                        currentAttackCooldown = attributes.attackCooldown
                        currentAttackDuration = attributes.attackDuration
                    }
                    else {
                        entity.action = if (entity.isMoving) "running" else "idle"
                    }
                }
                "attack" -> {
                    val target = data.state.entities[targetID]
                    //TODO haste, range
                    // Attack finished
                    if (target != null && currentAttackDuration <= 0L) {
                        // Attack finished
                        when {
                            !entity.looksForward(target) -> {
                                //TODO Can't see target
                            }
                            entity.position..target.position >= attributes.attackRange -> {
                                //TODO Out of range
                            }
                            else -> {
                                //TODO Attack!
                                val targetBehavior = data.behaviors[targetID]
                                //TODO on-attack script, power
                                targetBehavior?.onDamage(attributes.attackDamage, id, data, coercedScriptActions)
                            }
                        }
                        currentAttackDuration = 0L
                        entity.action = if (entity.isMoving) "running" else "idle"
                    }
                }
            }
            if (entity.isMoving) {
                //TODO haste
                moveObject(entity, delta, gameMap, state)
            }
        }
    }

    override fun onDamage(damage: Int, source: EntityID, data: LogicData, coercedScriptActions: LuaValue) {
        //TODO Armor
        entity.health -= damage
        if (entity.health <= 0) {
            entity.health = 0
            entity.action = "dead"
            entity.state = "dead"
        }
    }

    fun handleClientMessage(msg: ClientMessage, data: LogicData, coercedScriptActions: LuaValue) {
        with(data) {
            when (msg) {
                is MoveDirectionClientMessage -> {
                    if (entity.state != "dead") state.entities[id]?.moveDirection = msg.md
                }
                is SetMovingClientMessage -> {
                    if (entity.state != "dead") state.entities[id]?.isMoving = msg.isMoving
                }
                is CastSkillClientMessage -> {
                    //TODO Skill casting
                }
                is InteractionClientMessage -> {
                    val player = state.entities[id]
                    val entity = state.entities[msg.entityID]
                    if (player != null && entity != null && rangeBetween(player.position, entity.position) < entity.interactionRange) {
                        assets.runScript(
                                "scenes/$gameMapName/templates/entities/${entity.template}/interaction",
                                tableOf("playerID" to id, "entityID" to msg.entityID),
                                coercedScriptActions
                        )
                    }
                }
                is SetTargetClientMessage -> {
                    //TODO Cancelling attacks
                    targetID = msg.targetID
                }
            }
        }
    }
}