package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel

class BuildCreatorViewModel(application: Application) : ViewModel() {
    init {
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel cleared")
    }
}
