package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import org.json.JSONObject



class SkillsViewModel(
    val database: SkillsDAO,
    val databaseRank: SkillRankDAO,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var activeSkills = MutableLiveData<Skills?>()

    private var skills = database.getAllSkills()

    init {
        initializeActiveSkills()
    }

    private fun initializeActiveSkills() {
        uiScope.launch {
            //activeSkills.value = getSkillsFromDatabase()
        }
    }

    private suspend fun getSkillsFromDatabase(): Skills? {
        return withContext(Dispatchers.IO) {
            var skill = database.get(1)
            skill
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(skill: Skills) {
        withContext(Dispatchers.IO) {
            database.update(skill)
        }
    }

    private suspend fun insert(skill: Skills) {
        withContext(Dispatchers.IO) {
            database.insert(skill)
        }
    }

    private suspend fun insertRank(skillRank: SkillRank) {
        withContext(Dispatchers.IO) {
            databaseRank.insert(skillRank)
        }
    }

    private suspend fun getSkillWithRanks(): List<SkillWithRanks> {
        return withContext(Dispatchers.IO) {
            val skillWithRanks = databaseRank.getSkillWithRanks()
            Log.i("TAG", "" + skillWithRanks.size)
            skillWithRanks

        }
    }


    fun onStartTracking(inputStream: InputStream?) {
        uiScope.launch {

            //insertSkills(inputStream)
            val skill = getSkillsFromDatabase()
            Log.i("TAG", skill?.name)
            val skillsWithRanks = getSkillWithRanks()
            Log.i("TAG", "" + skillsWithRanks.size)
            for (i in 0 until skillsWithRanks.size){
                val skillWithRanks = skillsWithRanks[i]
                Log.i("TAG", skillWithRanks.skill.name)
                for(x in 0..skillWithRanks.skillRank.size -1){
                    Log.i("TAG","\t" + skillWithRanks.skillRank[x].modifiers.toString())
                }
            }
            //onClear()
        }
    }

    private suspend fun SkillsViewModel.insertSkills(
        inputStream: InputStream?
    ) {
        var json: String? = null

        try {
            var gson = Gson()
            val inputString = inputStream?.bufferedReader().use { it?.readText() }
            val jsonArray = JSONArray(inputString)
            for (i in 0..jsonArray.length() - 1) {
                val skill = jsonArray.getJSONObject(i)
                Log.i(
                    "TAG",
                    "id: " + skill.getInt("id") + ". Name: " + skill.getString("name") + ". Description: " + skill.getString(
                        "description"
                    )
                )
                val newSkill = Skills(
                    skill.getInt("id"),
                    skill.getString("name"),
                    skill.getString("description"),
                    null,
                    null
                )
                insert(newSkill)
                val ranks = skill.getJSONArray("ranks")
                for (x in 0..ranks.length() - 1) {
                    val rankLevel = ranks.getJSONObject(x)
                    var modifiers: HashMap<String, String>? = HashMap()
                    if (!rankLevel.getString("modifiers").equals("[]")) {
                        if (rankLevel.get("modifiers") is JSONObject) {
                            val modifier = rankLevel.getJSONObject("modifiers")
                            val keys = modifier.keys()
                            while (keys.hasNext()) {
                                val key = keys.next()
                                modifiers?.set(key, modifier.getString(key))
                            }

                        }
                    } else modifiers = null
                    Log.i(
                        "TAG",
                        "\tid: " + rankLevel.getInt("id") + ". Skill id: " + rankLevel.getInt("skill") + ". Level: " + rankLevel.getInt(
                            "level"
                        ) +
                                ". Description: " + rankLevel.getString("description") + " Modifiers: " + modifiers.toString()
                    )
                    val skillRank = SkillRank(
                        rankLevel.getInt("id"),
                        rankLevel.getInt("skill"),
                        rankLevel.getString("description"),
                        rankLevel.getInt("level").toByte(),
                        modifiers
                    )
                    insertRank(skillRank)
                }
            }

        } catch (e: IOException) {
            Log.i("TAG", e.toString())
        }
    }

    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // And clear tonight since it's no longer in the database
            activeSkills.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
