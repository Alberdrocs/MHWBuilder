package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece

class BuildCreatorViewModel(application: Application) : ViewModel() {
    init {
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BuildCreatorViewModel", "BuildCreatorViewModel cleared")
    }
}

class SelectedArmor(val armorPiece: ArmorPiece, val skillName1:String?, val skillName2:String?){
}
