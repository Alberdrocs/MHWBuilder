package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDatabaseDAO

class SkillsViewModel(
    val database: SkillsDatabaseDAO,
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


    fun onStartTracking() {
        uiScope.launch {
            val rankList: ArrayList<SkillRank> = ArrayList()
            rankList.add(SkillRank(1, 1,"Reduces the number of times you take poison damage.",1,null, null))
            rankList.add(SkillRank(2, 1,"Greatly reduces the number of times you take poison damage.",2,null,null))
            rankList.add(SkillRank(3, 1,"Prevents poison.",3,null, null))
            //val newSkill = Skills(1,"Poison Resistance","Reduces damage while poisoned.",rankList)

            //insertRank(rankList[0])
            //insertRank(rankList[1])
            //insertRank(rankList[2])
            //onClear()
            //Log.i("TAG", getSkillsFromDatabase()?.description)
            //activeSkills.value = getSkillsFromDatabase()
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
