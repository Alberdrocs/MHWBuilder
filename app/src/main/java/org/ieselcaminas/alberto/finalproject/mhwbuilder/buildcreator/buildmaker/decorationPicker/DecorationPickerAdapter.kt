package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ListItemDecorationPickerBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations
import java.lang.IndexOutOfBoundsException

class DecorationPickerAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val equipmentViewModel: EquipmentViewModel,
    private val slotPosition: Int,
    private val armorType: String
) : RecyclerView.Adapter<DecorationPickerAdapter.DecorationPickerViewHolder>(){

    var data = listOf<Decoration>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecorationPickerViewHolder {
        return DecorationPickerViewHolder.from(parent)
    }

    override fun getItemCount()= data.size

    override fun onBindViewHolder(holder: DecorationPickerViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, equipmentViewModel, slotPosition, armorType)
    }


    class DecorationPickerViewHolder private constructor(val binding: ListItemDecorationPickerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Decoration,
            dataSourceSkill: SkillsDAO,
            dataSourceSkillRank: SkillRankDAO,
            viewLifecycleOwner: LifecycleOwner,
            equipmentViewModel: EquipmentViewModel,
            slotPosition: Int,
            armorType: String
        ){
            binding.decoration = item

            binding.detailsDecorationPicker.visibility = View.GONE

                equipmentViewModel.getSkillRank(item.skillRankId!![0]).observe(viewLifecycleOwner, Observer { skillRank ->
                    equipmentViewModel.getSkillRankSkill(skillRank.skillId).observe(viewLifecycleOwner, Observer { skill ->
                        binding.decorationImageDecorationPickerImageView.setImageResource(when(item.slot.toInt()){
                            1 -> {when(skill.color){
                                "white" -> R.mipmap.gem_level_1_white
                                "red" -> R.mipmap.gem_level_1_red
                                "blue" -> R.mipmap.gem_level_1_blue
                                "green" -> R.mipmap.gem_level_1_green
                                "dark green" -> R.mipmap.gem_level_1_dark_green
                                "yellow" -> R.mipmap.gem_level_1_yellow
                                "orange" -> R.mipmap.gem_level_1_orange
                                "light yellow" -> R.mipmap.gem_level_1_light_yellow
                                "purple" -> R.mipmap.gem_level_1_purple
                                "dark purple" -> R.mipmap.gem_level_1_dark_purple
                                else -> R.mipmap.gem_level_1_white
                            }}
                            2 -> {when(skill.color){
                                "white" -> R.mipmap.gem_level_2_white
                                "red" -> R.mipmap.gem_level_2_red
                                "blue" -> R.mipmap.gem_level_2_blue
                                "green" -> R.mipmap.gem_level_2_green
                                "dark green" -> R.mipmap.gem_level_2_dark_green
                                "yellow" -> R.mipmap.gem_level_2_yellow
                                "orange" -> R.mipmap.gem_level_2_orange
                                "light yellow" -> R.mipmap.gem_level_2_light_yellow
                                "purple" -> R.mipmap.gem_level_2_purple
                                "dark purple" -> R.mipmap.gem_level_2_dark_purple
                                else -> R.mipmap.gem_level_2_white
                            }}
                            3 -> {when(skill.color){
                                "white" -> R.mipmap.gem_level_3_white
                                "red" -> R.mipmap.gem_level_3_red
                                "blue" -> R.mipmap.gem_level_3_blue
                                "green" -> R.mipmap.gem_level_3_green
                                "dark green" -> R.mipmap.gem_level_3_dark_green
                                "yellow" -> R.mipmap.gem_level_3_yellow
                                "orange" -> R.mipmap.gem_level_3_orange
                                "light yellow" -> R.mipmap.gem_level_3_light_yellow
                                "purple" -> R.mipmap.gem_level_3_purple
                                "dark purple" -> R.mipmap.gem_level_3_dark_purple
                                else -> R.mipmap.gem_level_3_white
                            }}
                            4 -> {when(skill.color){
                                "white" -> R.mipmap.gem_level_4_white
                                "red" -> R.mipmap.gem_level_4_red
                                "blue" -> R.mipmap.gem_level_4_blue
                                "green" -> R.mipmap.gem_level_4_green
                                "dark green" -> R.mipmap.gem_level_4_dark_green
                                "yellow" -> R.mipmap.gem_level_4_yellow
                                "orange" -> R.mipmap.gem_level_4_orange
                                "light yellow" -> R.mipmap.gem_level_4_light_yellow
                                "purple" -> R.mipmap.gem_level_4_purple
                                "dark purple" -> R.mipmap.gem_level_4_dark_purple
                                else -> R.mipmap.gem_level_4_white
                            }}
                            else -> R.mipmap.gem_level_1_green
                        })

                    })
                })

            binding.decorationNameDecorationPickerTextView.text = item.name
            item.skillRankId?.get(0)?.let { dataSourceSkillRank.get(it) }
                ?.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val level = it.level
                        dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer { it2 ->
                            it2?.let {
                                binding.decorationSkillName1DecorationPickerTextView.text = it2.name + " x" + level
                                binding.decorationDescription1DecorationPicker.text = it2.description
                            }
                        })
                    }
                })
            if (item.slot.toInt() != 4){
                binding.decorationSkillName2DecorationPickerTextView.text = ""
                binding.decorationDescription2DecorationPicker.text = ""
            } else {
                if (item.skillRankId!!.size > 1){
                    dataSourceSkillRank.get(item.skillRankId[1]).observe(viewLifecycleOwner, Observer {
                        it?.let {
                            val level = it.level
                            dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer { it2 ->
                                it2?.let {
                                    binding.decorationSkillName2DecorationPickerTextView.text = it2.name + " x" + level
                                    binding.decorationDescription2DecorationPicker.text = it2.description
                                }
                            })
                        }
                    })
                }
            }

            binding.addDecorationDecorationPickerButton.setOnClickListener {
                val currentEquipment = equipmentViewModel.currentArmorPieces.value
                val equipmentIndex = when(armorType){
                    "head" -> 0
                    "chest" -> 1
                    "gloves" -> 2
                    "waist" -> 3
                    "legs" -> 4
                    else -> 0
                }
                val decorations = currentEquipment?.get(equipmentIndex)?.decorations
                if (decorations != null) {
                    try {
                        decorations[slotPosition] = item
                    } catch (e: IndexOutOfBoundsException){
                        decorations.add(slotPosition,item)
                    }
                }
                var armorPiece = currentEquipment?.get(equipmentIndex)
                if (decorations != null) {
                    armorPiece?.decorations = decorations
                }
                armorPiece?.let { it1 -> currentEquipment?.set(equipmentIndex, it1) }
                if (currentEquipment != null) {
                    equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                }
                Navigation.findNavController(it).navigate(DecorationPickerFragmentDirections.actionDecorationPickerFragmentToBuildCreator())
            }

            var isExpanded = true
            binding.decorationPickerLayout.setOnClickListener {
                val show = toggleLayout(isExpanded, binding.detailsDecorationPicker)
                isExpanded = !show
            }

        }

        private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
            if (isExpanded) {
                Animations.expand(layoutExpand)
            } else {
                Animations.collapse(layoutExpand)
            }
            return isExpanded
        }

        companion object{
            fun from(parent: ViewGroup): DecorationPickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDecorationPickerBinding.inflate(layoutInflater, parent, false)
                return DecorationPickerViewHolder(binding)
            }
        }
    }
}