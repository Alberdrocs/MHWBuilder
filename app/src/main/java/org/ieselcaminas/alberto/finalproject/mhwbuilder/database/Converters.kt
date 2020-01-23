package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun stringToHashMap(string: String): HashMap<String,String>{
        //TODO implement TypeConverter
        return HashMap()
    }
}