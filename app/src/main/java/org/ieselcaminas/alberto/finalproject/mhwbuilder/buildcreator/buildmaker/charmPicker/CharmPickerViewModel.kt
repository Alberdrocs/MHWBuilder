package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.charmPicker

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.Charms
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.CharmsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.CharmPickerFragmentBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class CharmPickerViewModel(
    application: Application,
    private val database: CharmsDAO,
    private val binding: CharmPickerFragmentBinding
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val charmSearchQuery: LiveData<String>
        get() {
            val text: MutableLiveData<String> = MutableLiveData()
            text.value = ""
            binding.charmQuery.addTextChangedListener(object : TextWatcher {
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

    fun getAllCharms(): LiveData<List<Charms>> {
        return database.getAllCharms()
    }


}
