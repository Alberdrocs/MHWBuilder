package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decoration")
data class Decoration (
    @PrimaryKey @ColumnInfo(name = "decoration_id") var decorationId: Int,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "slot") val slot: Byte,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "skill_rank_id") val skillRankId: ArrayList<Int>?
    )