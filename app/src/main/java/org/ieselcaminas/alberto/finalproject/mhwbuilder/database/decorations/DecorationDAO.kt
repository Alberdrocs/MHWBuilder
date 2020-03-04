package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DecorationDAO{
    @Insert
    fun insert(decoration: Decoration)

    @Update
    fun update(decoration: Decoration)

    @Query("DELETE from decoration")
    fun clear()

    @Query("SELECT * FROM decoration WHERE decoration_id = :key")
    fun getDecoration(key: Int): LiveData<Decoration>

    @Query("SELECT * FROM decoration WHERE slot BETWEEN 1 AND :slot ORDER BY slot")
    fun getDecorationsOfSlot(slot: Int): LiveData<List<Decoration>>
}