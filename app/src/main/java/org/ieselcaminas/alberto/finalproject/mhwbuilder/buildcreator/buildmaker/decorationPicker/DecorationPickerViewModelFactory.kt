package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO

class DecorationPickerViewModelFactory(
    private val application: Application,
    private val dataSource: DecorationDAO
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DecorationPickerViewModel::class.java)) {
            return DecorationPickerViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}