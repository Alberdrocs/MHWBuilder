package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArmorPieceDAO{
    @Insert
    fun insert(armor: ArmorPiece)

    @Update
    fun update(armor: ArmorPiece)

    @Query("SELECT * FROM armor_piece")
    fun getAllArmorPieces():List<ArmorPiece>

    @Query("DELETE from armor_piece")
    fun clear()

    @Transaction
    @Query("SELECT * FROM armor_set")
    fun getArmorSetWithArmorPieces(): List<ArmorSetWithArmorPiece>
}

@Dao
interface ArmorSetDAO{
    @Insert
    fun insert(armorSet: ArmorSet)

    @Update
    fun update(armorSet: ArmorSet)

    @Query("SELECT * FROM armor_set")
    fun getAllArmorSets():List<ArmorSet>

    @Query("DELETE from armor_set")
    fun clear()
}