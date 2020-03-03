package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ArmorPickerFragmentBinding

class ArmorPickerViewModel(
    application: Application,
    private val database: ArmorPieceDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val binding: ArmorPickerFragmentBinding
) : AndroidViewModel(application) {

    val armorSearchQuery: LiveData<String>
        get() {
            val text: MutableLiveData<String> = MutableLiveData()
            text.value = ""
            binding.armorSearchQuery.addTextChangedListener(object : TextWatcher {
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


    fun getArmorPiecesOfType(armorType: String, rarityFrom: Int, rarityTo: Int): LiveData<List<ArmorPiece>> {
        return database.getAllArmorPiecesOfType(armorType, rarityFrom, rarityTo)
    }

    fun getAllSkills(): LiveData<List<SkillWithRanks>> {
        return dataSourceSkillRank.getAllSkillWithRanks()
    }
}