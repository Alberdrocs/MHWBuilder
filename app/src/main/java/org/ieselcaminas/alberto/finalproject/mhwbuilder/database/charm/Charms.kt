package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charms")
data class Charms(
    @PrimaryKey @ColumnInfo(name = "charm_id") var charmId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "skill_rank_id") val skillRankId: ArrayList<Int>
    )