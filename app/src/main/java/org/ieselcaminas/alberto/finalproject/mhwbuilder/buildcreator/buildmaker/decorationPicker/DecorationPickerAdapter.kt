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

class DecorationPickerAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val equipmentViewModel: EquipmentViewModel,
    private val slotPosition: Int,
    private val armorType: String
) : RecyclerView.Adapter<DecorationPickerAdapter.ViewHolder>(){

    var data = listOf<Decoration>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount()= data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, equipmentViewModel, slotPosition, armorType)
    }


    class ViewHolder private constructor(val binding: ListItemDecorationPickerBinding) : RecyclerView.ViewHolder(binding.root){
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

            binding.decorationImageDecorationPickerImageView.setImageResource(when(item.slot.toInt()){
                1-> R.mipmap.gem_level_1
                2-> R.mipmap.gem_level_2
                3-> R.mipmap.gem_level_3
                4-> R.mipmap.gem_level_4
                else -> R.mipmap.gem_level_1
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
                    decorations.add(slotPosition,item)
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
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDecorationPickerBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}