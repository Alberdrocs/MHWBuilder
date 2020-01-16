package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import  androidx.lifecycle.LiveData
import  androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SkillsDatabaseDAO {

    @Insert
    fun insert(skill: Skills)

    @Update
    fun update(skill: Skills)

    @Query("SELECT * FROM skill WHERE id = :key")
    fun get(key: Int): Skills

    @Query("SELECT * FROM skill ORDER BY id DESC")
    fun getAllSkills(): LiveData<List<Skills>>
}