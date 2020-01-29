package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import java.util.*
import kotlin.collections.HashMap


class Converters {
    @TypeConverter
    fun fromStringToArrayList(value: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun gettingListFromString(genreIds: String?): ArrayList<Int>? {
        val list = ArrayList<Int>()

        val array = genreIds?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()

        if (array != null) {
            for (s in array) {
                if (!s.isEmpty()) {
                    list.add(Integer.parseInt(s))
                }
            }
        }
        return list
    }

    @TypeConverter
    fun writingStringFromList(list: ArrayList<Int>?): String? {
        var genreIds = ""
        if (list != null) {
            for (i in list) {
                genreIds += ",$i"
            }
        }
        return genreIds
    }

    @TypeConverter
    fun fromStringToHashMapSS(value: String?): HashMap<String, String>? {
        val mapType = object : TypeToken<HashMap<String, String>>() {

        }.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringToMapSS(map: HashMap<String, String>?): String? {
        val gson = Gson()
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromStringToHashMapSI(value: String?): HashMap<String, Int>? {
        val mapType = object : TypeToken<HashMap<String, Int>>() {

        }.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringToMapSI(map: HashMap<String, Int>?): String? {
        val gson = Gson()
        return gson.toJson(map)
    }

}