package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.BuildCreatorDirections
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
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
        val dataSourceCharm = AppDatabase.getInstance(application).charmsDAO()
        val viewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet, dataSourceRank, dataSourceSkill,dataSourceCharm)
        val equipmentViewModel = activity?.run { ViewModelProviders.of(this, viewModelFactory).get(EquipmentViewModel::class.java) }

        val binding: EquipmentFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.equipment_fragment, container, false)
        binding.equipmentViewModel = equipmentViewModel
        val adapter = EquipmentAdapter(activity)
        binding.armorPieceRecyclerView.adapter = adapter


        equipmentViewModel?.currentArmorPieces?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        equipmentViewModel?.currentCharm?.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.charmTextView.text = it.name
                binding.charmImageView.setImageResource(when(it.rarity.toInt()){
                    6 -> R.mipmap.charm_level_6
                    7 -> R.mipmap.charm_level_7
                    8 -> R.mipmap.charm_level_8
                    9 -> R.mipmap.charm_level_9
                    else -> R.mipmap.charm_level_6
                })
                binding.charmSkill2TextView.text = ""
                var counter = 0
                it.skillRankId.forEach {skillRankId ->
                    equipmentViewModel.getSkillRank(skillRankId).observe(viewLifecycleOwner, Observer { skillRank ->
                        equipmentViewModel.getSkillRankSkill(skillRank.skillId).observe(viewLifecycleOwner, Observer { skill ->
                            if (counter == 0){
                                binding.charmSkill1TextView.text = skill.name + " x" + skillRank.level
                            } else {
                                binding.charmSkill2TextView.text = skill.name + " x" + skillRank.level
                            }
                            counter++
                        })
                    })
                }
                binding.cardViewEquipmentCharm.setOnClickListener {
                    Navigation.findNavController(it).navigate(BuildCreatorDirections.actionBuildCreatorToCharmPickerFragment())
                }
            }
        })

        setSkillsForDisplayToViewModel(equipmentViewModel)

        binding.lifecycleOwner = this


        return binding.root
    }

    private fun setSkillsForDisplayToViewModel(equipmentViewModel: EquipmentViewModel?) {
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

                equipmentViewModel.currentCharm.value?.skillRankId?.forEach {
                    skillRankIdList.add(it)
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
                                    skillsForDisplayMap[skillWithRanks.skill.name] = skillsForDisplay
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
