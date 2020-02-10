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

    private var _navigateToArmorPiece = MutableLiveData<Int>()
    val navigateToArmorPiece: LiveData<Int>
        get() = _navigateToArmorPiece

    fun startNavigationToArmorPiece(armorPieceId: Int){
        _navigateToArmorPiece.value = armorPieceId
    }

    fun getArmorPiecesOfType(armorType: String): LiveData<List<ArmorPiece>> {
        return database.getAllArmorPiecesOfType(armorType)
    }
}