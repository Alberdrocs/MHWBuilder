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
                var skillsForDisplayMap: HashMap<String, SkillsForDisplay>
                selectedArmorArray.forEach { armor ->
                    if (armor.decorations.size > 0) {
                        if (armor.armorPiece.skillRankId?.size == 2) {
                            val armorPieceSkillRankIdList = listOf(armor.armorPiece.skillRankId[0], armor.armorPiece.skillRankId[1])
                            var decorationCounter = 0
                            armor.decorations.forEach { decoration ->
                                    if (decoration?.skillRankId?.size == 2){
                                        val decorationSkillRankIdList = listOf(decoration.skillRankId[0], decoration.skillRankId[1])
                                        equipmentViewModel.getListOf4SkillRank(armorPieceSkillRankIdList[0], armorPieceSkillRankIdList[1], decorationSkillRankIdList[0], decorationSkillRankIdList[1])
                                            .observe(viewLifecycleOwner, Observer { skillRankList ->
                                                skillRankList.forEach { skillRank ->
                                                    equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                                                        .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                                            skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                                            checkAndSaveSkillsForDisplay(skillsForDisplayMap, equipmentViewModel, skillWithRanks, skillRank, decorationCounter)
                                                        })
                                                }
                                            })
                                    } else if(decoration?.skillRankId?.size == 1){
                                        equipmentViewModel.getListOf3SkillRank(armorPieceSkillRankIdList[0], armorPieceSkillRankIdList[1], decoration?.skillRankId[0])
                                            .observe(viewLifecycleOwner, Observer { skillRankList ->
                                                skillRankList.forEach { skillRank ->
                                                    equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                                                        .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                                            skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                                            checkAndSaveSkillsForDisplay(skillsForDisplayMap, equipmentViewModel, skillWithRanks, skillRank, decorationCounter)
                                                        })
                                                }

                                            })
                                    }
                                decorationCounter++
                            }
                        } else if (armor.armorPiece.skillRankId?.size == 1) {
                            var decorationCounter = 0
                            armor.decorations.forEach { decoration ->
                                if (decoration?.skillRankId?.size == 2){
                                    val decorationSkillRankIdList = listOf(decoration.skillRankId[0], decoration.skillRankId[1])
                                    equipmentViewModel.getListOf3SkillRank(armor.armorPiece.skillRankId[0], decorationSkillRankIdList[0], decorationSkillRankIdList[1])
                                        .observe(viewLifecycleOwner, Observer { skillRankList ->
                                            skillRankList.forEach { skillRank ->
                                                equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                                                    .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                                        skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                                        checkAndSaveSkillsForDisplay(skillsForDisplayMap, equipmentViewModel, skillWithRanks, skillRank, decorationCounter)
                                                    })
                                            }

                                        })
                                } else if (decoration?.skillRankId?.size == 1){
                                    equipmentViewModel.getListOf2SkillRank(armor.armorPiece.skillRankId[0], decoration.skillRankId[0])
                                        .observe(viewLifecycleOwner, Observer { skillRankList ->
                                            skillRankList.forEach { skillRank ->
                                                equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                                                    .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                                        skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                                        checkAndSaveSkillsForDisplay(skillsForDisplayMap, equipmentViewModel, skillWithRanks, skillRank, decorationCounter)
                                                    })
                                            }

                                        })
                                }
                                decorationCounter++
                            }
                        }
                    } else {
                        armor.armorPiece.skillRankId?.forEach { rankId ->
                            equipmentViewModel.getSkillRank(rankId)
                                .observe(viewLifecycleOwner, Observer { skillRank ->
                                    equipmentViewModel.getSkillWithRanks(skillRank.skillId)
                                        .observe(viewLifecycleOwner, Observer { skillWithRanks ->
                                            skillsForDisplayMap = equipmentViewModel.currentSkillsForDisplay.value!!
                                            checkAndSaveSkillsForDisplay(skillsForDisplayMap, equipmentViewModel, skillWithRanks, skillRank, 0)
                                        })
                                })
                        }
                    }
                }
            })
    }

    private fun checkAndSaveSkillsForDisplay(
        skillsForDisplayMap: HashMap<String, SkillsForDisplay>,
        equipmentViewModel: EquipmentViewModel,
        skillWithRanks: SkillWithRanks,
        skillRank: SkillRank,
        counter: Int
    ) {
        var skillsForDisplayMap1 = skillsForDisplayMap
        skillsForDisplayMap1 = equipmentViewModel.currentSkillsForDisplay.value!!
        if (skillsForDisplayMap1.containsKey(skillWithRanks.skill.name)) {
            val skillsForDisplay = SkillsForDisplay(
                skillWithRanks.skill,
                skillsForDisplayMap1[skillWithRanks.skill.name]!!.skillRanks,
                if (counter > 0 ) skillsForDisplayMap1[skillWithRanks.skill.name]!!.activeLevels else skillsForDisplayMap1[skillWithRanks.skill.name]!!.activeLevels + skillRank.level
            )
            skillsForDisplayMap1.set(skillWithRanks.skill.name, skillsForDisplay)
        } else {
            val arrayListSkillRank = skillWithRanks.skillRank as ArrayList<SkillRank>
            val skillsForDisplay = SkillsForDisplay(skillWithRanks.skill, arrayListSkillRank, skillRank.level.toInt())
            skillsForDisplayMap1[skillWithRanks.skill.name] = skillsForDisplay
        }
        equipmentViewModel.setCurrentSkillsForDisplay(skillsForDisplayMap1)
    }


}
