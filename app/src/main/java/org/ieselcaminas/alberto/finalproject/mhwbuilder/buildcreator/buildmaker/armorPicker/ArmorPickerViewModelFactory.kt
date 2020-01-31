package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO

class ArmorPickerViewModelFactory(
    private val application: Application,
    private val dataSource: ArmorPieceDAO,
    private val dataSourceSet: ArmorSetDAO
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArmorPickerViewModel::class.java)) {
            return ArmorPickerViewModel(application, dataSource, dataSourceSet) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}