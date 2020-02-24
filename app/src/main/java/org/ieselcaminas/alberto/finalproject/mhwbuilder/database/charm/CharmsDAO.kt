package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharmsDAO{
    @Insert
    fun insert(charms: Charms)

    @Query("DELETE from charms")
    fun clear()

    @Query("SELECT * FROM charms WHERE charm_id = :key")
    fun getCharm(key: Int): LiveData<Charms>
}