package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles
import ktx.ashley.mapperFor

class GameMap : Component, Pool.Poolable {
    var width = 0
    var height = 0
    var tiles = createEmptyTiles(width, height)
        set(value) {
            field = value
            shouldComputeTileGraphics = true
        }


    var shouldComputeTileGraphics = false

    /**
     * Менят тайл на карте.
     *
     * Используется для дебаггинга
     */
    fun switchTile(x: Int, y: Int) {
        tiles[x][y] = if (tiles[x][y].terrain == Terrain.FLOOR) Tile(
            Terrain.WALL
        ) else Tile(Terrain.FLOOR)
        shouldComputeTileGraphics = true
    }


    fun isWalkable(x: Int, y: Int): Boolean = tiles[x][y].terrain == Terrain.FLOOR

    fun isExplored(position: Position): Boolean = tiles[position.x][position.y].explored

    fun blockSight(x: Int, y: Int): Boolean {
        return try {
            tiles[x][y].blockSight
        } catch (e: ArrayIndexOutOfBoundsException) {
            true
        }
    }

    fun markAsExplored(position: Position) {
        tiles[position.x][position.y].explored = true
    }

    override fun reset() {
        tiles = createEmptyTiles(width, height)
    }

    companion object {
        val mapper = mapperFor<GameMap>()
    }
}