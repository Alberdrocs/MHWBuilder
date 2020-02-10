package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO

class EquipmentViewModelFactory(
    private val application: Application,
    private val dataSource: ArmorPieceDAO,
    private val dataSourceSet: ArmorSetDAO,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            return EquipmentViewModel(application, dataSource, dataSourceSet, viewLifecycleOwner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}