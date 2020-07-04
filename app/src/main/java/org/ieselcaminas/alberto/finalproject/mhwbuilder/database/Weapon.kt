package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills



@Entity
data class Weapon (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "attack_display") val attackDisplay: Int,
    @ColumnInfo(name = "attack_raw") val attackRaw: Int,
    @ColumnInfo(name = "elderseal") val elderseal: String,
    @ColumnInfo(name = "affinity") val affinity: Int,
    @ColumnInfo(name = "coatings") val coatings: ArrayList<String>,
    @ColumnInfo(name = "defense_bonus") val defenseBonus: Int,
    @ColumnInfo(name = "damage_type") val damageType: String,
    @ColumnInfo(name = "skill_rank_id") val skillRankId: ArrayList<Int>?,
    @ColumnInfo(name = "durability") val durability: HashMap<String, Int>,
    @ColumnInfo(name = "slots") val slots: ArrayList<Int>,
    @ColumnInfo(name = "elements") val elements: String,
    @ColumnInfo(name = "phial") val phial: String,
    @ColumnInfo(name = "deviation") val deviation: String,
    @ColumnInfo(name = "special_ammo") val specialAmmo: String,
    @ColumnInfo(name = "ammo") val ammo: ArrayList<String>

)