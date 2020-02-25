package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.CharmsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

class EquipmentViewModelFactory(
    private val application: Application,
    private val dataSource: ArmorPieceDAO,
    private val dataSourceSet: ArmorSetDAO,
    private val dataSourceRank: SkillRankDAO,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceCharms: CharmsDAO
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            return EquipmentViewModel(application, dataSource, dataSourceSet, dataSourceRank, dataSourceSkill,dataSourceCharms) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}