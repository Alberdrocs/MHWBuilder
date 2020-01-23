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
            var skillWithRanks = databaseRank.getSkillWithRanks()
            skillWithRanks
        }
    }


    fun onStartTracking(inputStream: InputStream?) {
        uiScope.launch {

            var json: String? = null

            try {
                var gson = Gson()
                val inputString = inputStream?.bufferedReader().use { it?.readText() }
                val jsonArray = JSONArray(inputString)
                for (i in 0..jsonArray.length()-1){
                    val skill = jsonArray.getJSONObject(i)
                    Log.i("TAG", "id: " + skill.getInt("id") + ". Name: " + skill.getString("name") + ". Description: " + skill.getString("description"))
                    val ranks = skill.getJSONArray("ranks")
                    for (x in 0..ranks.length() - 1){
                        val rankLevel = ranks.getJSONObject(x)
                        var modifiers: String? = null
                        if(!rankLevel.getString("modifiers").equals("[]")){
                            if(rankLevel.get("modifiers") is JSONObject) {
                                modifiers = ""
                                val modifier = rankLevel.getJSONObject("modifiers")
                                val keys = modifier.keys()
                                while (keys.hasNext()){
                                    val key = keys.next()
                                    modifiers += key + ": " + modifier.get(key) + "\n"
                                }

                            }
                        }
                        Log.i("TAG", "\tid: " + rankLevel.getInt("id") + ". Skill id: " + rankLevel.getInt("skill") + ". Level: " + rankLevel.getInt("level") +
                                ". Description: " + rankLevel.getString("description") + " Modifiers: " + modifiers)
                    }
                }

            }
            catch (e: IOException){
                Log.i("TAG", e.toString())
            }





//            val rankList: ArrayList<SkillRank> = ArrayList()
//            rankList.add(SkillRank(1, 1,"Reduces the number of times you take poison damage.",1,null, null))
//            rankList.add(SkillRank(2, 1,"Greatly reduces the number of times you take poison damage.",2,null,null))
//            rankList.add(SkillRank(3, 1,"Prevents poison.",3,null, null))
//            val newSkill = Skills(1,"Poison Resistance","Reduces damage while poisoned.", null)
//
//            val skillWithRanks = getSkillWithRanks()
//            Log.i("TAG", skillWithRanks[0].skill.description)
//            for (i in 0..2){
//                Log.i("TAG","Level: " + skillWithRanks[0].skillRank[i].level + ". " + skillWithRanks[0].skillRank[i].skillDescription)
//            }
//
//            insert(newSkill)
//
//            insertRank(rankList[0])
//            insertRank(rankList[1])
//            insertRank(rankList[2])
//            onClear()
//            Log.i("TAG", getSkillsFromDatabase()?.description)
//            activeSkills.value = getSkillsFromDatabase()
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
