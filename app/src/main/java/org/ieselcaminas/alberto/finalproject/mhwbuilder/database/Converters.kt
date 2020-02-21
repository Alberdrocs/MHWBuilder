package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Converters {

    @TypeConverter
    fun gettingListFromString(value: String?): ArrayList<Int>? {
        if (value == "") {
            return null
        }
        val list = mutableListOf<Int>()
        val array = value?.split(",")
        if (array != null) {
            for (s in array) {
                try {
                    list.add(s.toInt())
                } catch (e: NumberFormatException) {

                }

            }
        }
        return list as ArrayList<Int>
    }

    @TypeConverter
    fun writingStringFromList(list: ArrayList<Int>?): String? {
        var result = ""
        list?.forEachIndexed { index, element ->
            result += element
            if (index != (list.size - 1)) {
                result += ","
            }
        }
        return result
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