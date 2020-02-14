package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.lang.IndexOutOfBoundsException

class EquipmentViewModel(
    application: Application,
    private val database: ArmorPieceDAO,
    private val databaseSet: ArmorSetDAO,
    private val databaseRank: SkillRankDAO,
    private val databaseSkill: SkillsDAO,
    private val viewLifecycleOwner: LifecycleOwner
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _currentArmorPieces = MutableLiveData<ArrayList<SelectedArmor>>()
    val currentArmorPieces: LiveData<ArrayList<SelectedArmor>>
        get() = _currentArmorPieces


    val currentDefenseValue: LiveData<Int>
        get() {
            var defense:MutableLiveData<Int> = MutableLiveData()
            defense.value = 0
            for (i in 0 until 5){
                val armorDefense = currentArmorPieces.value?.get(i)?.armorPiece?.defense?.get(0)
                if (armorDefense != null) {
                    val currentValue = defense.value
                    defense.value = armorDefense + currentValue!!
                }
            }
            return defense
        }

    val currentResistancesValues: LiveData<HashMap<String, Int>>
    get() {
        var resistances: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
        var baseResistances = HashMap<String, Int>()
        baseResistances["fire"] = 0
        baseResistances["ice"] = 0
        baseResistances["thunder"] = 0
        baseResistances["dragon"] = 0
        baseResistances["water"] = 0
        resistances.value = baseResistances
        for (i in 0 until 5){
            val armorResistances = currentArmorPieces.value?.get(i)?.armorPiece?.resistances
            if (armorResistances != null){
                val currentResistances = resistances.value
                currentResistances?.set("fire",
                    currentResistances["fire"]!! + armorResistances["fire"]!!
                )
                currentResistances?.set("ice",
                    currentResistances["ice"]!! + armorResistances["ice"]!!
                )
                currentResistances?.set("thunder",
                    currentResistances["thunder"]!! + armorResistances["thunder"]!!
                )
                currentResistances?.set("dragon",
                    currentResistances["dragon"]!! + armorResistances["dragon"]!!
                )
                currentResistances?.set("water",
                    currentResistances["water"]!! + armorResistances["water"]!!
                )
                resistances.value = currentResistances
            }
        }
        return resistances
    }


    private var _currentSkillsForDisplay = MutableLiveData<ArrayList<SkillsForDisplay>>()
    val currentSkillsForDisplay: LiveData<ArrayList<SkillsForDisplay>>
        get() = _currentSkillsForDisplay


    init {
        getPieceOfEachType().observe(viewLifecycleOwner, Observer {
            it?.let {
                val selectedArmor: ArrayList<SelectedArmor> = ArrayList()
                for (i in 0 until it.size){
                    selectedArmor.add(SelectedArmor(it[i],"Skill 1", "Skill 2", ArrayList()))
                }
                _currentArmorPieces.value = selectedArmor
            }
        })
    }

    fun setCurrentArmorPieces(armorPieces: ArrayList<SelectedArmor>){
        _currentArmorPieces.value = armorPieces
    }

    fun setCurrentSkillsForDisplay(skillsForDisplay: ArrayList<SkillsForDisplay>){
        _currentSkillsForDisplay.value = skillsForDisplay
    }


    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
            databaseSet.clear()
        }
    }

    private suspend fun insert(armorPiece: ArmorPiece) {
        withContext(Dispatchers.IO) {
            database.insert(armorPiece)
        }
    }

    private suspend fun insert(armorSet: ArmorSet) {
        withContext(Dispatchers.IO) {
            databaseSet.insert(armorSet)
        }
    }

    private suspend fun getArmorSetWithPieces(): List<ArmorSetWithArmorPiece> {
        return withContext(Dispatchers.IO) {
            val armorSetWithPieces = database.getArmorSetWithArmorPieces()
            armorSetWithPieces
        }
    }

    private suspend fun getAllArmorSets(): List<ArmorSet> {
        return withContext(Dispatchers.IO) {
            val allArmorSets = databaseSet.getAllArmorSets()
            allArmorSets
        }
    }

    private suspend fun getAllArmorPieces(): List<ArmorPiece> {
        return withContext(Dispatchers.IO) {
            val allArmorPieces = database.getAllArmorPieces()
            allArmorPieces
        }
    }

    fun getArmorPieceSkillRank(skillRankId: Int): LiveData<SkillRank> {
        return databaseRank.get(skillRankId)
    }

    fun getSkillRankSkill(skillId: Int): LiveData<Skills> {
        return databaseSkill.get(skillId)
    }

    fun getSkillWithRanks(skillId: Int): LiveData<SkillWithRanks>{
        return databaseRank.getSkillWithRanks(skillId)
    }

//    private suspend fun getPieceOfEachType(): LiveData<List<ArmorPiece>> {
//        return withContext(Dispatchers.IO) {
//            val allArmorPieces = database.getFirstArmorPiecesOfType()
//            allArmorPieces
//        }
//    }

    fun getArmorPiece(armorPieceId: Int): LiveData<ArmorPiece>{
        return database.getArmorPieces(armorPieceId)
    }

    fun getPieceOfEachType(): LiveData<List<ArmorPiece>> {
        return database.getFirstArmorPiecesOfType()
    }

}

class SelectedArmor(val armorPiece: ArmorPiece, val skillName1:String?, val skillName2:String?, var decorations: ArrayList<Decoration?>){
}

class SkillsForDisplay(val skill: Skills, val skillRanks: ArrayList<SkillRank>, val activeLevels: Int)
