package com.mirage.core.game.maps

/** Immutable game map data */
data class GameMap(

        /** Name of this scene on loading screen */
        val name: String?,
        /** Description of this scene on loading screen */
        val description: String?,
        /** Width in tiles */
        val width: Int,
        /** Height in tiles */
        val height: Int,

        val spawnX: Float,

        val spawnY: Float,
        /** Name of file with tile textures */
        val tileSetName: String,
        /** ID of default tile. Default tile is drawn outside of map bounds */
        val defaultTileID: Int,
        /** List of IDs of tiles in tileset */
        private val tiles: List<Int>,
        /**
         * Walkability of tiles
         * 1 - walkable
         * 2 - not walkable, but shootable
         * 3 - not walkable, not shootable
         */
        private val collisions: List<Int>
) {

    fun getTileID(x: Int, y: Int): Int = tiles[x + y * width]

    fun isTileWalkable(x: Int, y: Int): Boolean = collisions[x + y * width] == 1

    fun isTileShootable(x: Int, y: Int): Boolean = collisions[x + y * width] == 1 || collisions[x + y * width] == 2

}