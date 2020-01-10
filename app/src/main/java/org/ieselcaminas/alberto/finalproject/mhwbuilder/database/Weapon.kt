package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SlotsType{
    LEVEL1, LEVEL2, LEVEL3, LEVEL4
}

enum class ElementsType{
    POISON, SLEEP, PARALYSIS, BLAST, STUN, FIRE, WATER, THUNDER, ICE, DRAGON
}

enum class PhialType{
    POWER, POWERELEMENTAL, POISON, DRAGON, EXHAUST, PARALYSIS
}

enum class SpecialAmmoType{
    WYVERNBLAST, WYVERNSNIPE, WYVERNHEART
}

data class WeaponElement(
    val type: ElementsType,
    val damage: Int,
    val hidden: Boolean
)

data class Ammo(
    val name: String
)

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
    @ColumnInfo(name = "coatings") val coatings: ArrayList<ElementsType>,
    @ColumnInfo(name = "defense_bonus") val defenseBonus: Int,
    @ColumnInfo(name = "damage_type") val damageType: String,
    @Embedded val skills: Skills,
    @ColumnInfo(name = "durability") val durability: HashMap<String, Int>,
    @ColumnInfo(name = "slots") val slots: ArrayList<SlotsType>,
    @ColumnInfo(name = "elements") val elements: WeaponElement,
    @ColumnInfo(name = "phial") val phial: PhialType,
    @ColumnInfo(name = "deviation") val deviation: String,
    @ColumnInfo(name = "special_ammo") val specialAmmo: SpecialAmmoType,
    @ColumnInfo(name = "ammo") val ammo: ArrayList<Ammo>

)