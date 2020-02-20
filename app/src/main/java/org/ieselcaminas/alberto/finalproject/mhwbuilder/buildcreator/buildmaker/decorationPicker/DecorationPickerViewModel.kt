package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class DecorationPickerViewModel(
    application: Application,
    private val database: DecorationDAO,
    private val binding: DecorationPickerFragmentBinding
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val decorationSearchQuery: LiveData<String>
        get() {
            val text: MutableLiveData<String> = MutableLiveData()
            text.value = ""
            binding.decorationQuery.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text.value = p0.toString()
                }

            })
            return text
        }

    fun getDecorationsOfSlot(slot: Int): LiveData<List<Decoration>> {
        return database.getDecorationsOfSlot(slot)
    }


}
