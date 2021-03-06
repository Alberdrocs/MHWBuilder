package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import  androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface SkillsDAO {

    @Insert
    fun insert(skill: Skills)

    @Update
    fun update(skill: Skills)

    @Query("SELECT * FROM skill WHERE id = :key")
    fun get(key: Int): LiveData<Skills>

    @Query("SELECT * FROM skill ORDER BY id DESC")
    fun getAllSkills(): LiveData<List<Skills>>

    @Query("DELETE from skill")
    fun clear()
}

@Dao
interface SkillRankDAO {

    @Insert
    fun insert(skillRank: SkillRank)

    @Query("SELECT * FROM skill_rank")
    fun getSkillRanks(): LiveData<List<SkillRank>>

    @Query("SELECT * FROM skill_rank WHERE skill_rank_id = :key")
    fun get(key: Int): LiveData<SkillRank>

    @Transaction
    @Query("SELECT * FROM skill WHERE id = :key")
    fun getSkillWithRanks(key: Int): LiveData<SkillWithRanks>

    @Transaction
    @Query("SELECT * FROM skill WHERE id = :key OR id = :key2")
    fun getListOfSkillWithRanks(key: Int, key2: Int): LiveData<List<SkillWithRanks>>

    @Transaction
    @Query("SELECT * FROM skill WHERE id IN (SELECT DISTINCT(skill_rank_id) FROM skill_rank)")
    fun getAllSkillWithRanks(): LiveData<List<SkillWithRanks>>

}