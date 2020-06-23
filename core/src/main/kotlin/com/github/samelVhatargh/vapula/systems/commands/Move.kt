package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.allOf
import ktx.ashley.get


class Move : EntitySystem() {
    fun execute(entity: Entity, direction: Direction) {
        val mapEntity = engine.getEntitiesFor(allOf(GameMap::class).get()).first()

        val gameMap = mapEntity[GameMap.mapper]!!
        val position = entity[Position.mapper]!!

        val newX = position.x + direction.x
        val newY = position.y + direction.y

        val entities = engine.getEntitiesFor(OCCUPY_SPACE_FAMILY)
        val obstacle = entities.find {
            val occupierPosition = it[Position.mapper]!!
            occupierPosition.x == newX && occupierPosition.y == newY
        }

        if (obstacle == null && gameMap.isWalkable(newX, newY)) {
            position.x = newX
            position.y = newY

            entity[FieldOfView.mapper]?.shouldUpdate = true
        }
    }
}