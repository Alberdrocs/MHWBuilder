package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.charmPicker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.CharmsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.CharmPickerFragmentBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class CharmPickerViewModelFactory(
    private val application: Application,
    private val dataSource: CharmsDAO,
    private val binding: CharmPickerFragmentBinding
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharmPickerViewModel::class.java)) {
            return CharmPickerViewModel(application, dataSource, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}