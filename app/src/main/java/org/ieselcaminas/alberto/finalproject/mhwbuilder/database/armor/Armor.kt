package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor

import androidx.room.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank


@Entity(tableName = "armor_piece")
data class ArmorPiece (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "rank") val rank: String,
    @ColumnInfo(name = "rarity") val rarity: Byte,
    @ColumnInfo(name = "defense") val defense: ArrayList<Int>,
    @ColumnInfo(name = "resistances") val resistances: HashMap<String, Int>,
    @ColumnInfo(name = "slots") val slots: ArrayList<Int>,
    @ColumnInfo(name = "skill_rank_id") val skillRankId: Int,
    @ColumnInfo(name = "armor_set_id") var armorSetId: Int
)

@Entity(tableName = "armor_set")
data class ArmorSet(
    @PrimaryKey @ColumnInfo(name = "armor_set_id")var armorSetId: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val rank: String,
    @ColumnInfo val requiredPieces: ArrayList<Int>?,
    @ColumnInfo(name = "skill_rank_id") val setBonusSkillsRanksId: ArrayList<Int>?
)

data class ArmorWithSkillRank(
    @Embedded val armor: ArmorPiece,
    @Relation(
        parentColumn = "id",
        entityColumn = "skill_rank_id"
    )
    val skillRank: List<SkillRank>
)