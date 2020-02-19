package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillWithRanks
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
                this, viewModelFactory
            ).get(EquipmentViewModel::class.java)
        }

        val binding: EquipmentFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.equipment_fragment, container, false)
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
                    for (i in it) {
                        Log.i("SkillsForDisplay", "Skill: " + i.value.skill.toString() + ", Active levels: " + i.value.activeLevels)
                    }
                }
            })
        }

        binding.setLifecycleOwner(this)


        return binding.root
    }

    private fun getSkillsForDisplayToViewModel(equipmentViewModel: EquipmentViewModel?) {
        equipmentViewModel?.currentArmorPieces?.observe(
            viewLifecycleOwner,
            Observer { selectedArmorArray ->
                if (selectedArmorArray == null) return@Observer

                equipmentViewModel.setCurrentSkillsForDisplay(HashMap())

                val skillRankIdList = ArrayList<Int>()
                selectedArmorArray.forEach { armor ->
                    armor.armorPiece.skillRankId?.forEach { armorPieceSkillRankId ->
                        skillRankIdList.add(armorPieceSkillRankId)
                    }
                    armor.decorations.forEach decorationLoop@{ decoration ->
                        if (decoration == null) return@decorationLoop

                        decoration.skillRankId?.forEach { decorationSkillRankId ->
                            skillRankIdList.add(decorationSkillRankId)
                        }
                    }
                }

                skillRankIdList.forEach { skillRankId ->
                    equipmentViewModel.getSkillRank(skillRankId).observe(viewLifecycleOwner, Observer { skillRank ->
                        equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                            .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                val skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                if (skillsForDisplayMap.containsKey(skillWithRanks.skill.name)) {
                                    val skillName = skillWithRanks.skill.name
                                    val skillsForDisplay = SkillsForDisplay(
                                        skillWithRanks.skill,
                                        skillsForDisplayMap[skillWithRanks.skill.name]!!.skillRanks,
                                        skillsForDisplayMap[skillName]!!.activeLevels + skillRank.level
                                    )
                                    skillsForDisplayMap.set(skillWithRanks.skill.name, skillsForDisplay)
                                } else {
                                    val arrayListSkillRank = skillWithRanks.skillRank as ArrayList<SkillRank>
                                    val skillsForDisplay = SkillsForDisplay(skillWithRanks.skill, arrayListSkillRank, skillRank.level.toInt())
                                    skillsForDisplayMap[skillWithRanks.skill.name] = skillsForDisplay
                                }
                                equipmentViewModel.setCurrentSkillsForDisplay(skillsForDisplayMap)
                            })
                    })

                }

            })
    }
}
