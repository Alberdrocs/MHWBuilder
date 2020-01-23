package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import androidx.room.*

@Entity(tableName = "skill")
data class Skills (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "level_cap")val levelCap: Byte?
)

@Entity(tableName = "skill_rank",
    foreignKeys = [ForeignKey(entity = Skills::class, parentColumns = ["id"], childColumns = ["skill_id"])],
    indices = [Index("skill_id")])
data class SkillRank(
    @PrimaryKey @ColumnInfo(name = "skill_rank_id") var skillRankId: Int,
    @ColumnInfo(name = "skill_id") var skillId: Int,
    @ColumnInfo(name = "skill_description") val skillDescription: String,
    @ColumnInfo val level: Byte,
    val modifiers: HashMap<String,String>?
)

data class SkillWithRanks(
    @Embedded val skill: Skills,
    @Relation(
        parentColumn = "id",
        entityColumn = "skill_id"
    )
    val skillRank: List<SkillRank> = emptyList()
)