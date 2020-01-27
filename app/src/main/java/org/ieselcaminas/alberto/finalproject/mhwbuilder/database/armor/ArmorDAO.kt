package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface ArmorDAO{
    @Insert
    fun insert(armor: Armor)

    @Update
    fun update(armor: Armor)
}