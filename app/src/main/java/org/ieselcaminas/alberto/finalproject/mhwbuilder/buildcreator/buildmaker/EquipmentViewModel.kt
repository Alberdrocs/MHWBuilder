package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.Charms
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.CharmsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.*
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EquipmentViewModel(
    application: Application,
    private val database: ArmorPieceDAO,
    private val databaseSet: ArmorSetDAO,
    private val databaseRank: SkillRankDAO,
    private val databaseSkill: SkillsDAO,
    private val databaseCharm: CharmsDAO
) : AndroidViewModel(application) {

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

    val currentHealthValue: LiveData<Int>
    get() {
        var health: MutableLiveData<Int> = MutableLiveData()
        health.value = 100
        if (currentSkillsForDisplay.value!!.contains("Health Boost")){
            when(currentSkillsForDisplay.value!!["Health Boost"]?.activeLevels){
                1 -> health.value = 115
                2 -> health.value = 130
                3 -> health.value = 150
            }
        }
        return health
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
                currentResistances?.set("fire", currentResistances["fire"]!! + armorResistances["fire"]!!)
                currentResistances?.set("ice", currentResistances["ice"]!! + armorResistances["ice"]!!)
                currentResistances?.set("thunder", currentResistances["thunder"]!! + armorResistances["thunder"]!!)
                currentResistances?.set("dragon", currentResistances["dragon"]!! + armorResistances["dragon"]!!)
                currentResistances?.set("water", currentResistances["water"]!! + armorResistances["water"]!!)
                resistances.value = currentResistances
            }
        }
        return resistances
    }

    private var _currentSkillsForDisplay = MutableLiveData<HashMap<String, SkillsForDisplay>>()
    val currentSkillsForDisplay: LiveData<HashMap<String, SkillsForDisplay>>
        get() = _currentSkillsForDisplay

    private var _currentCharm = MutableLiveData<Charms>()
    val currentCharm: LiveData<Charms>
        get() = _currentCharm

    private var _currentSetSkillsForDisplay = MutableLiveData<ArrayList<Skills>>()
    val currentSetSkillsForDisplay: LiveData<ArrayList<Skills>>
        get() = _currentSetSkillsForDisplay


    init {
        _currentSkillsForDisplay.value = HashMap()
        val selectedArmor: ArrayList<SelectedArmor> = ArrayList()
        selectedArmor.add(SelectedArmor(ArmorPiece(749,"Bone Helm Alpha +","head","master",9, arrayListOf(114,0,0),
            hashMapOf("fire" to 2,"ice" to 0, "thunder" to 2, "dragon" to 2, "water" to 0), arrayListOf(3), arrayListOf(54,150),169)
            ,"Health Boost x2","Partbreaker x1", arrayListOf(null, null, null)))
        selectedArmor.add(SelectedArmor(ArmorPiece(750,"Bone Mail Alpha +","chest","master",9, arrayListOf(114,0,0),
            hashMapOf("fire" to 2,"ice" to 0, "thunder" to 2, "dragon" to 2, "water" to 0), arrayListOf(2), arrayListOf(40,53),169)
            ,"Attack Boost x2","Health Boost x1", arrayListOf(null, null, null)))
        selectedArmor.add(SelectedArmor(ArmorPiece(751,"Bone Vambraces Alpha +","gloves","master",9, arrayListOf(114,0,0),
            hashMapOf("fire" to 2,"ice" to 0, "thunder" to 2, "dragon" to 2, "water" to 0), arrayListOf(2), arrayListOf(39,154),169)
            ,"Attack Boost x1","Master Mounter x1", arrayListOf(null, null, null)))
        selectedArmor.add(SelectedArmor(ArmorPiece(752,"Bone Coil Alpha +","waist","master",9, arrayListOf(114,0,0),
            hashMapOf("fire" to 2,"ice" to 0, "thunder" to 2, "dragon" to 2, "water" to 0), arrayListOf(3), arrayListOf(150,186),169)
            ,"Partbreaker x1","Horn Maestro x1", arrayListOf(null, null, null)))
        selectedArmor.add(SelectedArmor(ArmorPiece(752,"Bone Greaves Alpha +","legs","master",9, arrayListOf(114,0,0),
            hashMapOf("fire" to 2,"ice" to 0, "thunder" to 2, "dragon" to 2, "water" to 0), arrayListOf(3), arrayListOf(153,186),169)
            ,"Slugger x1","Horn Maestro x1", arrayListOf(null, null, null)))
        _currentArmorPieces.value = selectedArmor
        _currentCharm.value = Charms(1, "Poison Charm 3",6, arrayListOf(3))
    }

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun setCurrentArmorPieces(armorPieces: ArrayList<SelectedArmor>){
        _currentArmorPieces.value = armorPieces
    }

    fun setCurrentSkillsForDisplay(skillsForDisplay: HashMap<String, SkillsForDisplay>){
        _currentSkillsForDisplay.value = skillsForDisplay
    }

    fun setCurrentCharm(charms: Charms){
        _currentCharm.value = charms
    }

    fun getSkillRank(skillRankId: Int): LiveData<SkillRank> {
        return databaseRank.get(skillRankId)
    }

    fun getSkillRankSkill(skillId: Int): LiveData<Skills> {
        return databaseSkill.get(skillId)
    }

    fun getSkillWithRanks(skillId: Int): LiveData<SkillWithRanks>{
        return databaseRank.getSkillWithRanks(skillId)
    }

    fun getAllCharms(): LiveData<List<Charms>>{
        return  databaseCharm.getAllCharms()
    }

    private fun getPieceOfEachType(): LiveData<List<ArmorPiece>> {
        return database.getFirstArmorPiecesOfType()
    }

}

class SelectedArmor(val armorPiece: ArmorPiece, val skillName1:String?, val skillName2:String?, var decorations: ArrayList<Decoration?>)

class SkillsForDisplay(val skill: Skills, val skillRanks: ArrayList<SkillRank>, val activeLevels: Int)

class SetSkillForDisplay(val skill: Skills)
