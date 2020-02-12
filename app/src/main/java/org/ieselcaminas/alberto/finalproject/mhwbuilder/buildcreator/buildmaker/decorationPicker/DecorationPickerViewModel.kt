package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO

class DecorationPickerViewModel(
    application: Application,
    private val database: DecorationDAO
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getDecorationsOfSlot(slot: Int): LiveData<List<Decoration>> {
        return database.getDecorationsOfSlot(slot)
    }


}
