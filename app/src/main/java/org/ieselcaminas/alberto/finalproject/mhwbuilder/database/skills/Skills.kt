package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class Skills (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "description")val description: String,
    @Embedded val ranks: ArrayList<SkillRank>
)

@Entity(tableName = "skill_rank")
data class SkillRank(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "skill_description") val skillDescription: String,
    @ColumnInfo val level: Byte,
    @ColumnInfo val modifiers: HashMap<String, Int>?
)