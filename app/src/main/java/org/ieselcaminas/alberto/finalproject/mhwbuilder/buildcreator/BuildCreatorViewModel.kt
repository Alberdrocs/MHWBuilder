package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator

import android.util.Log
import androidx.lifecycle.ViewModel

class BuildCreatorViewModel : ViewModel() {
    init {
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel cleared")
    }
}
