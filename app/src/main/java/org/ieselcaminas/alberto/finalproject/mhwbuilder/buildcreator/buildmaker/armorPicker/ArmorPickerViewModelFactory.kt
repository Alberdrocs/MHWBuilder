package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ArmorPickerFragmentBinding

class ArmorPickerViewModelFactory(
    private val application: Application,
    private val dataSource: ArmorPieceDAO,
    private val dataSourceSet: ArmorSetDAO,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val binding: ArmorPickerFragmentBinding
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArmorPickerViewModel::class.java)) {
            return ArmorPickerViewModel(application, dataSource, dataSourceSet, dataSourceSkill, dataSourceSkillRank, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}