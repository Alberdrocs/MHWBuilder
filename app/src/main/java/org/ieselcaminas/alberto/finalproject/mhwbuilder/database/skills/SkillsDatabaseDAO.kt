package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import  androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface SkillsDatabaseDAO {

    @Insert
    fun insert(skill: Skills)

    @Update
    fun update(skill: Skills)

//    @Transaction
//    @Query("SELECT * FROM skill")
//    fun getSkillWithRanks(): List<SkillWithRanks>


    @Query("SELECT * FROM skill WHERE id = :key")
    fun get(key: Int): Skills

    @Query("SELECT * FROM skill ORDER BY id DESC")
    fun getAllSkills(): LiveData<List<Skills>>

    @Query("DELETE from skill")
    fun clear()
}

@Dao
interface SkillRankDatabaseDAO {

}