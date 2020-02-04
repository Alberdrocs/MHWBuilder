package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BuildCreatorViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuildCreatorViewModel::class.java)) {
            return BuildCreatorViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}