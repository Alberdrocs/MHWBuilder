package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.SelectedArmor
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class EquipmentViewModel(
    application: Application,
    private val database: ArmorPieceDAO,
    private val databaseSet: ArmorSetDAO,
    private val viewLifecycleOwner: LifecycleOwner
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _currentArmorPieces = MutableLiveData<ArrayList<SelectedArmor>>()
    val currentArmorPieces: LiveData<ArrayList<SelectedArmor>>
        get() = _currentArmorPieces

    init {
        getPieceOfEachType().observe(viewLifecycleOwner, Observer {
            it?.let {
                val selectedArmor: ArrayList<SelectedArmor> = ArrayList()
                for (i in 0 until it.size){
                    selectedArmor.add(SelectedArmor(it[i],"Skill 1", "Skill 2"))
                }
                _currentArmorPieces.value = selectedArmor
            }
        })
    }

    fun setCurrentArmorPieces(armorPieces: ArrayList<SelectedArmor>){
        _currentArmorPieces.value = armorPieces
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



    fun onStartTracking() {
        uiScope.launch {
            //clear()
            //insertArmorPiece(inputStreamPiece)
            //insertArmorsSets(inputStreamSet)

            //insertTest()
//            Log.i("TAG", getAllArmorPieces()[0].name)
//            Log.i("TAG", getAllArmorSets()[0].name)
//            Log.i("TAG", getArmorSetWithPieces()[0].armorPiece[0].name)
//            Log.i("TAG",getArmorSetWithPieces().armorPiece[0].name)
//            Log.i("TAG",getArmorSetWithPieces().armorSet.name)
        }
    }

    private suspend fun EquipmentViewModel.insertArmorPiece(
        inputStreamPiece: InputStream?
    ) {
        try {
            val inputString = inputStreamPiece?.bufferedReader().use { it?.readText() }
            val jsonArray = JSONArray(inputString)
            for (i in 0..jsonArray.length() - 1) {
                val piece = jsonArray.getJSONObject(i)
                val defenseList: ArrayList<Int> = ArrayList()
                val defense = piece.getJSONObject("defense")
                defenseList.add(0, defense.getInt("base"))
                defenseList.add(1, defense.getInt("max"))
                defenseList.add(2, defense.getInt("augmented"))
                val resistancesHashMap: HashMap<String, Int> = HashMap()
                val resistance = piece.getJSONObject("resistances")
                resistancesHashMap.set("fire", resistance.getInt("fire"))
                resistancesHashMap.set("water", resistance.getInt("water"))
                resistancesHashMap.set("ice", resistance.getInt("ice"))
                resistancesHashMap.set("thunder", resistance.getInt("thunder"))
                resistancesHashMap.set("dragon", resistance.getInt("dragon"))
                var slotsList: ArrayList<Int>? = ArrayList()
                if (piece.getString("slots") == "[]") {
                    slotsList = null
                } else {
                    val slot = piece.getJSONArray("slots")
                    for (x in 0 until slot.length()) {
                        val rank = slot.getJSONObject(x)
                        slotsList?.add(rank.getInt("rank"))
                    }
                }
                var skillRankId: ArrayList<Int>? = ArrayList()
                if (piece.getString("skills") == "[]") {
                    skillRankId = null
                } else {
                    val skills = piece.getJSONArray("skills")
                    for (x in 0 until skills.length()) {
                        val skill = skills.getJSONObject(x)
                        skillRankId?.add(skill.getInt("id"))
                    }
                }
                var set: Int?
                if (piece.getString("armorSet") == "null") {
                    set = null
                } else {
                    val setObject = piece.getJSONObject("armorSet")
                    set = setObject.getInt("id")
                }

                val armorPiece = ArmorPiece(
                    piece.getInt("id"),
                    piece.getString("name"),
                    piece.getString("type"),
                    piece.getString("rank"),
                    piece.getInt("rarity").toByte(),
                    defenseList,
                    resistancesHashMap,
                    slotsList,
                    skillRankId,
                    set
                )
                Log.i(
                    "TAG",
                    "Id: " + armorPiece.armorPieceId + ". Name: " + armorPiece.name + ". Type: " + armorPiece.type + ". Rank: " + armorPiece.rank +
                            ". Rarity: " + armorPiece.rarity + ". Defense: " + armorPiece.defense.toString() + ". Resistances: " + armorPiece.resistances.toString() + ". Slots: " +
                            armorPiece.slots.toString() + ". Skills Id: " + armorPiece.skillRankId.toString() + ". Armor Set Id: " + armorPiece.armorSetId
                )
                insert(armorPiece)
            }
        } catch (e: IOException) {
            Log.i("TAG", e.toString())
        }
    }

    private suspend fun EquipmentViewModel.insertArmorsSets(
        inputStreamSet: InputStream?
    ) {
        try {
            val inputString = inputStreamSet?.bufferedReader().use { it?.readText() }
            val jsonArray = JSONArray(inputString)
            for (i in 0..jsonArray.length() - 1) {
                val set = jsonArray.getJSONObject(i)
                if (set.getString("bonus").equals("null")) {
                    val armorSet = ArmorSet(
                        set.getInt("id"),
                        set.getString("name"),
                        set.getString("rank"),
                        null,
                        null,
                        null
                    )
                    Log.i(
                        "TAG",
                        "Id: " + armorSet.armorSetId + ". Name: " + armorSet.name + ". Rank: " + armorSet.rank
                    )
                    insert(armorSet)
                } else {
                    val bonus = set.getJSONObject("bonus")
                    val requiredPieces: ArrayList<Int> = ArrayList()
                    val skillRanksId: ArrayList<Int> = ArrayList()
                    val ranks = bonus.getJSONArray("ranks")
                    for (x in 0..ranks.length() - 1) {
                        val rank = ranks.getJSONObject(x)
                        requiredPieces.add(rank.getInt("pieces"))
                        val skill = rank.getJSONObject("skill")
                        skillRanksId.add(skill.getInt("id"))
                    }
                    val armorSet = ArmorSet(
                        set.getInt("id"),
                        set.getString("name"),
                        set.getString("rank"),
                        bonus.getString("name"),
                        requiredPieces,
                        skillRanksId
                    )
                    Log.i(
                        "TAG",
                        "Id: " + armorSet.armorSetId + ". Name: " + armorSet.name + ". Rank: " + armorSet.rank + ". Bonus name: " + armorSet.bonusName + ". Required pieces: "
                                + requiredPieces + ". Bonus skills: " + skillRanksId
                    )
                    insert(armorSet)
                }
            }
        } catch (e: IOException) {
            Log.i("TAG", e.toString())
        }
    }

    private suspend fun EquipmentViewModel.insertTest() {
        var piecesRequired: ArrayList<Int> = ArrayList()
        piecesRequired.add(0, 2)
        piecesRequired.add(1, 4)
        var skillRankid: ArrayList<Int> = ArrayList()
        skillRankid.add(0, 312)
        skillRankid.add(1, 314)
        val armorSet =
            ArmorSet(157, "Lunastra Beta", "high", "Lunastra Favor", piecesRequired, skillRankid)
        insert(armorSet)
        var defense: ArrayList<Int> = ArrayList()
        defense.add(0, 70)
        defense.add(1, 76)
        defense.add(2, 90)
        var resistances: HashMap<String, Int> = HashMap()
        resistances.set("fire", 3)
        resistances.set("water", 1)
        resistances.set("ice", -3)
        resistances.set("thunder", 1)
        resistances.set("dragon", -2)
        val slots: ArrayList<Int> = ArrayList()
        slots.add(3)
        val skillRankId: ArrayList<Int> = ArrayList()
        skillRankId.add(0, 216)
        skillRankId.add(1, 301)
        val armorPiece = ArmorPiece(
            688,
            "Empress Crown Alpha",
            "head",
            "high",
            8,
            defense,
            resistances,
            slots,
            skillRankId,
            157
        )
        insert(armorPiece)
        Log.i("TAG", "insertado")
    }
}
