package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ArmorPieceDAO{
    @Insert
    fun insert(armor: ArmorPiece)

    @Update
    fun update(armor: ArmorPiece)

    @Query("SELECT * FROM armor_piece")
    fun getAllArmorPieces():List<ArmorPiece>

    @Query("SELECT * FROM armor_piece WHERE type = :armorType AND (rarity BETWEEN 8 AND 12)")
    fun getAllArmorPiecesOfType(armorType: String): LiveData<List<ArmorPiece>>

    @Query("DELETE from armor_piece")
    fun clear()

    @Transaction
    @Query("SELECT * FROM armor_set")
    fun getArmorSetWithArmorPieces(): List<ArmorSetWithArmorPiece>

    @Transaction
    @Query("SELECT * FROM armor_piece")
    fun getArmorPieceWithArmorSkills(): LiveData<List<ArmorPieceWithSkillRank>>
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