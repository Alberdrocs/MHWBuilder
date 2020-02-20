package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class DecorationPickerViewModelFactory(
    private val application: Application,
    private val dataSource: DecorationDAO,
    private val binding: DecorationPickerFragmentBinding
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DecorationPickerViewModel::class.java)) {
            return DecorationPickerViewModel(application, dataSource, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}