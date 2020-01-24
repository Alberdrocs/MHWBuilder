package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList
import com.google.gson.Gson
import java.util.*
import kotlin.collections.HashMap


class Converters {
    @TypeConverter
    fun fromString(value: String?): HashMap<String, String>? {
        val mapType = object : TypeToken<HashMap<String, String>>() {

        }.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: HashMap<String, String>?): String? {
        val gson = Gson()
        return gson.toJson(map)
    }
}