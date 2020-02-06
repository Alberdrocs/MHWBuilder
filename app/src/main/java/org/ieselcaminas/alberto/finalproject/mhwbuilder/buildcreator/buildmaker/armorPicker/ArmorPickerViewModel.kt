package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

class ArmorPickerViewModel(
    application: Application,
    private val database: ArmorPieceDAO,
    private val databaseSet: ArmorSetDAO,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private suspend fun getAllArmorPieces(): List<ArmorPiece> {
        return withContext(Dispatchers.IO) {
            val allArmorPieces = database.getAllArmorPieces()
            allArmorPieces
        }
    }

    private suspend fun getAllArmorPiecesOfType(armorType: String): LiveData<List<ArmorPiece>> {
        return withContext(Dispatchers.IO) {
            val allArmorPiecesOfType = database.getAllArmorPiecesOfType(armorType)
            allArmorPiecesOfType
        }
    }

    private suspend fun getSkillRank(key: Int): LiveData<SkillRank> {
        return withContext(Dispatchers.IO) {
            val allArmorPiecesOfType = dataSourceSkillRank.get(key)
            allArmorPiecesOfType
        }
    }

    private suspend fun getSkill(key: Int): LiveData<Skills> {
        return withContext(Dispatchers.IO) {
            val allArmorPiecesOfType = dataSourceSkill.get(key)
            allArmorPiecesOfType
        }
    }

//    fun getSkillName(key: Int): String? {
//        uiScope.launch {
//            val skillId = getSkillRank(key).skillId
//            val skillName = getSkill(skillId).name
//        }
//        return null
//    }

    fun getArmorPiecesOfType(armorType: String): LiveData<List<ArmorPiece>> {
        return database.getAllArmorPiecesOfType(armorType)
    }
}