package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor


import androidx.room.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank


@Entity(tableName = "armor_piece")
data class ArmorPiece (
    @PrimaryKey @ColumnInfo(name = "armor_piece_id") var armorPieceId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "rank") val rank: String,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "defense") val defense: ArrayList<Int>,
    @ColumnInfo(name = "resistances") val resistances: HashMap<String, Int>?,
    @ColumnInfo(name = "slots") val slots: ArrayList<Int>?,
    @ColumnInfo(name = "skill_rank_id") val skillRankId: ArrayList<Int>?,
    @ColumnInfo(name = "armor_set_id") var armorSetId: Int?
)

@Entity(tableName = "armor_set")
data class ArmorSet(
    @PrimaryKey @ColumnInfo(name = "armor_set_id")var armorSetId: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val rank: String,
    @ColumnInfo(name = "bonus_name") val bonusName: String?,
    @ColumnInfo(name = "required_pieces") val requiredPieces: ArrayList<Int>?,
    @ColumnInfo(name = "skill_rank_id") val setBonusSkillRankId: ArrayList<Int>?
)

data class ArmorSetWithArmorPiece(
    @Embedded val armorSet: ArmorSet,
    @Relation(
        parentColumn = "armor_set_id",
        entityColumn = "armor_set_id"
    )
    val armorPiece: List<ArmorPiece>
)