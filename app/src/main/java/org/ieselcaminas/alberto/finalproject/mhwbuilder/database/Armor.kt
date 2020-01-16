package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills

enum class ArmorType{
    HEAD, CHEST, GLOVES, WAIST, LEGS
}

enum class ArmorRank{
    LOW, HIGH, MASTER
}

@Entity
data class Armor (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: ArmorType,
    @ColumnInfo(name = "rank") val rank: ArmorRank,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "defense") val defense: ArrayList<Int>,
    @ColumnInfo(name = "resistances") val resistances: HashMap<String, Int>,
    @ColumnInfo(name = "slots") val slots: ArrayList<SlotsType>,
    @Embedded val skills: Skills
)