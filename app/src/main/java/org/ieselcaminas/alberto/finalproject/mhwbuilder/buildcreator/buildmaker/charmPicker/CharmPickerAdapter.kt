package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.charmPicker

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
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker.DecorationPickerFragmentDirections
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.Charms
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ListItemDecorationPickerBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations

class CharmPickerAdapter(
private val viewLifecycleOwner: LifecycleOwner,
private val dataSourceSkill: SkillsDAO,
private val dataSourceSkillRank: SkillRankDAO,
private val equipmentViewModel: EquipmentViewModel
) : RecyclerView.Adapter<CharmPickerAdapter.CharmPickerViewHolder>(){

    var data = listOf<Charms>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharmPickerViewHolder {
        return CharmPickerViewHolder.from(parent)
    }

    override fun getItemCount()= data.size

    override fun onBindViewHolder(holder: CharmPickerViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, equipmentViewModel)
    }


    class CharmPickerViewHolder private constructor(val binding: ListItemDecorationPickerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Charms,
            dataSourceSkill: SkillsDAO,
            dataSourceSkillRank: SkillRankDAO,
            viewLifecycleOwner: LifecycleOwner,
            equipmentViewModel: EquipmentViewModel
        ){

            binding.detailsDecorationPicker.visibility = View.GONE
            binding.addDecorationDecorationPickerButton.text = "ADD CHARM"

            binding.decorationNameDecorationPickerTextView.text = item.name
            binding.decorationImageDecorationPickerImageView.setImageResource(
                when (item.rarity.toInt()) {
                    6 -> R.mipmap.charm_level_6
                    7 -> R.mipmap.charm_level_7
                    8 -> R.mipmap.charm_level_8
                    9 -> R.mipmap.charm_level_9
                    else -> R.mipmap.charm_level_6
                }
            )
            item.skillRankId[0].let { dataSourceSkillRank.get(it) }
                .observe(viewLifecycleOwner, Observer {
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
            if (item.skillRankId.size == 1){
                binding.decorationSkillName2DecorationPickerTextView.text = ""
                binding.decorationDescription2DecorationPicker.text = ""
            } else {
                if (item.skillRankId.size > 1){
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
                equipmentViewModel.setCurrentCharm(item)
                Navigation.findNavController(it).navigate(CharmPickerFragmentDirections.actionCharmPickerFragmentToBuildCreator())
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
            fun from(parent: ViewGroup): CharmPickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDecorationPickerBinding.inflate(layoutInflater, parent, false)
                return CharmPickerViewHolder(binding)
            }
        }
    }
}