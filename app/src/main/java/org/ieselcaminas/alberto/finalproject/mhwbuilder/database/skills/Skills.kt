package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import androidx.room.*

@Entity(tableName = "skill")
data class Skills (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "description")val description: String
)

@Entity(tableName = "skill_rank")
data class SkillRank(
    @PrimaryKey var skillRankId: Int,
    var skillId: Int,
    @ColumnInfo(name = "skill_description") val skillDescription: String,
    @ColumnInfo val level: Byte,
    @ColumnInfo(name = "modifiers_name") val modifiersName: String?,
    @ColumnInfo(name = "modifiers_value") val modifiersValue: String?
)

//data class SkillWithRanks(
//    @Embedded val skill: Skills,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "skillId"
//    )
//    val skillRank: List<SkillRank>
//)