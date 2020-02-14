package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.EquipmentFragmentBinding


class Equipment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val dataSourceRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val viewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet, dataSourceRank, dataSourceSkill, viewLifecycleOwner)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(
                this, viewModelFactory).get(EquipmentViewModel::class.java)}

        val binding: EquipmentFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.equipment_fragment,  container, false)
        binding.equipmentViewModel = equipmentViewModel
        val adapter = EquipmentAdapter(activity)
        binding.armorPieceRecyclerView.adapter = adapter

        binding.lifecycleOwner = this

        //equipmentViewModel.getArmorPiece()

        if (equipmentViewModel != null) {
            equipmentViewModel.currentArmorPieces.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.data = it

                }
            })
        }
        if (equipmentViewModel != null) {
            Log.i("TAG", equipmentViewModel.currentArmorPieces.value.toString())
        }

        getSkillsForDisplayToViewModel(equipmentViewModel)

        if (equipmentViewModel != null) {
            equipmentViewModel.currentSkillsForDisplay.observe(viewLifecycleOwner, Observer {
                it?.let {
                    for (i in it){
                        Log.i("SkillsForDisplay", "Skill: " + i.skill.toString() + ", Skill Rank: " + i.skillRanks.toString() + ", Active levels: " + i.activeLevels)
                    }
                }
            })
        }

        binding.setLifecycleOwner(this)


        return binding.root
    }

    private fun getSkillsForDisplayToViewModel(equipmentViewModel: EquipmentViewModel?) {
        if (equipmentViewModel != null) {
            equipmentViewModel.currentArmorPieces.observe(viewLifecycleOwner, Observer {
                it?.let { selectedArmorArray ->
                    val skillsForDisplayMap = MutableLiveData<HashMap<Skills, SkillsForDisplay>>()
                    skillsForDisplayMap.value = HashMap()
                    for (i in 0 until 5) {
                        if (selectedArmorArray[i].armorPiece.skillRankId != null) {
                            selectedArmorArray[i].armorPiece.skillRankId?.get(0)
                                ?.let { skillRankId ->
                                    Log.i("SKillRank", skillRankId.toString())
                                    equipmentViewModel.getArmorPieceSkillRank(skillRankId)
                                        .observe(viewLifecycleOwner,
                                            Observer { skillRank ->
                                                Log.i("SKillRank2", skillRank.toString())
                                                equipmentViewModel.getSkillRankSkill(skillRank.skillId)
                                                    .observe(viewLifecycleOwner, Observer { skill ->
                                                        Log.i("SKillRank3", skill.toString())
                                                        if (skillsForDisplayMap.value!!.containsKey(
                                                                skill
                                                            )
                                                        ) {
                                                            val skillsForDisplay = SkillsForDisplay(
                                                                skill,
                                                                skillsForDisplayMap.value!![skill]!!.skillRanks,
                                                                skillsForDisplayMap.value!![skill]!!.activeLevels + skillRank.level
                                                            )
                                                            Log.i(
                                                                "SKillRank3If",
                                                                skillsForDisplay.toString()
                                                            )
                                                            skillsForDisplayMap.value!![skill] =
                                                                skillsForDisplay
                                                            setSkillsToViewModel(
                                                                skillsForDisplayMap,
                                                                equipmentViewModel
                                                            )
                                                        } else {
                                                            equipmentViewModel.getSkillWithRanks(
                                                                skill.id
                                                            ).observe(
                                                                viewLifecycleOwner,
                                                                Observer { skillWithRanks ->
                                                                    val arrayListSkillRank =
                                                                        skillWithRanks.skillRank as ArrayList<SkillRank>
                                                                    val skillsForDisplay =
                                                                        SkillsForDisplay(
                                                                            skill,
                                                                            arrayListSkillRank,
                                                                            skillRank.level.toInt()
                                                                        )
                                                                    Log.i(
                                                                        "SKillRank3Else",
                                                                        skillsForDisplay.skillRanks.toString()
                                                                    )
                                                                    skillsForDisplayMap.value!![skill] =
                                                                        skillsForDisplay
                                                                    Log.i(
                                                                        "SKillRank3Else2",
                                                                        skillsForDisplayMap.value!![skill]?.skill.toString()
                                                                    )
                                                                    setSkillsToViewModel(
                                                                        skillsForDisplayMap,
                                                                        equipmentViewModel
                                                                    )
                                                                })
                                                        }
                                                    })
                                            })
                                }
                        }
                    }
                }
            })
        }
    }

    private fun setSkillsToViewModel(
        skillsForDisplayMap: MutableLiveData<HashMap<Skills, SkillsForDisplay>>,
        equipmentViewModel: EquipmentViewModel
    ) {
        Log.i("TAGSkillFor", skillsForDisplayMap.value.toString())
        val skillsForDisplayArray: ArrayList<SkillsForDisplay> = ArrayList()
        for (x in skillsForDisplayMap.value!!) {
            Log.i("TAGSkillFor", x.value.toString())
            skillsForDisplayArray.add(x.value)
        }
        Log.i("TAGSkills", skillsForDisplayArray.toString())
        equipmentViewModel.setCurrentSkillsForDisplay(skillsForDisplayArray)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
