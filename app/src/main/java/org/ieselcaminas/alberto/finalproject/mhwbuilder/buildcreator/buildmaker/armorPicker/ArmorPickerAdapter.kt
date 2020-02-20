package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.SelectedArmor
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillWithRanks
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ListItemArmorPickerBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations


class ArmorPickerAdapter(private val clickListener: ArmorPieceListener,
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val viewLifecycleOwner: LifecycleOwner,
    private val equipmentViewModel: EquipmentViewModel
) : RecyclerView.Adapter<ArmorPickerAdapter.ArmorPickerViewHolder>() {


    var data = listOf<ArmorPiece>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorPickerViewHolder {
        return ArmorPickerViewHolder.from(parent)
    }
    private fun getSkill(skillRankId: Int): LiveData<SkillWithRanks> {
        return dataSourceSkillRank.getSkillWithRanks(skillRankId)
    }

    override fun onBindViewHolder(holder: ArmorPickerViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, clickListener,dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, equipmentViewModel)
    }



    class ArmorPickerViewHolder private constructor(val binding: ListItemArmorPickerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ArmorPiece, clickListener: ArmorPieceListener, dataSourceSkill: SkillsDAO, dataSourceSkillRank: SkillRankDAO, viewLifecycleOwner: LifecycleOwner, equipmentViewModel: EquipmentViewModel){
            binding.armorPiece = item
            binding.clickListener = clickListener

            binding.armorPickerName.text = item.name
            if (item.skillRankId.toString() != "[]") {
                Log.i("TAG", "" + item.skillRankId?.get(0)!!)
                dataSourceSkillRank.get(item.skillRankId?.get(0)).observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val skillRankLevel = it.level
                        dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer { it2 ->
                            it2?.let {
                                binding.armorPickerSkill1.text = it2.name + " x" + skillRankLevel
                            }
                        })
                    }
                })
                if (item.skillRankId?.size  > 1) {
                    dataSourceSkillRank.get(item.skillRankId[1]).observe(viewLifecycleOwner, Observer {
                        it?.let {
                            val skillRankLevel = it.level
                            dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer {it2 ->
                                it2?.let {
                                    binding.armorPickerSkill2.text = it2.name + " x" + skillRankLevel
                                }
                            })
                        }
                    })
                } else {
                    binding.armorPickerSkill2.text = ""
                }
            } else {
                binding.armorPickerSkill1.text = ""
                binding.armorPickerSkill2.text = ""
            }
            Log.i("TAGSlot", item.slots.toString())
            binding.armorPickerSlot1Image.setImageResource(android.R.color.transparent)
            binding.armorPickerSlot2Image.setImageResource(android.R.color.transparent)
            binding.armorPickerSlot3Image.setImageResource(android.R.color.transparent)
            if (item.slots != null){
                binding.armorPickerSlot1Image.setImageResource(when (item.slots[0]) {
                    1 -> R.mipmap.gem_level_1
                    2 -> R.mipmap.gem_level_2
                    3 -> R.mipmap.gem_level_3
                    4 -> R.mipmap.gem_level_4
                    else -> 0
                })
                if (item.slots.size > 1){
                    binding.armorPickerSlot2Image.setImageResource(when (item.slots[1]) {
                        1 -> R.mipmap.gem_level_1
                        2 -> R.mipmap.gem_level_2
                        3 -> R.mipmap.gem_level_3
                        4 -> R.mipmap.gem_level_4
                        else -> 0
                    })
                    if (item.slots.size > 2){
                        binding.armorPickerSlot3Image.setImageResource(when (item.slots[2]) {
                            1 -> R.mipmap.gem_level_1
                            2 -> R.mipmap.gem_level_2
                            3 -> R.mipmap.gem_level_3
                            4 -> R.mipmap.gem_level_4
                            else -> 0
                        })
                    }
                }
            }
            when (item.type){
                "head" -> {
                    binding.armorPickerImage.setImageResource(when (item.rarity.toInt()) {
                        9 -> R.mipmap.head9
                        10 -> R.mipmap.head10
                        11 -> R.mipmap.head11
                        12 -> R.mipmap.head12
                        else -> R.mipmap.head
                    })
                }
                "chest" -> {
                    binding.armorPickerImage.setImageResource(when (item.rarity.toInt()) {
                        9 -> R.mipmap.chest9
                        10 -> R.mipmap.chest10
                        11 -> R.mipmap.chest11
                        12 -> R.mipmap.chest12
                        else -> R.mipmap.chest
                    })
                }
                "gloves" -> {
                    binding.armorPickerImage.setImageResource(when (item.rarity.toInt()) {
                        9 -> R.mipmap.gloves9
                        10 -> R.mipmap.gloves10
                        11 -> R.mipmap.gloves11
                        12 -> R.mipmap.gloves12
                        else -> R.mipmap.gloves
                    })
                }
                "waist" -> {
                    binding.armorPickerImage.setImageResource(when (item.rarity.toInt()) {
                        9 -> R.mipmap.waist9
                        10 -> R.mipmap.waist10
                        11 -> R.mipmap.waist11
                        12 -> R.mipmap.waist12
                        else -> R.mipmap.waist
                    })
                }
                "legs" -> {
                    binding.armorPickerImage.setImageResource(when (item.rarity.toInt()) {
                        9 -> R.mipmap.legs9
                        10 -> R.mipmap.legs10
                        11 -> R.mipmap.legs11
                        12 -> R.mipmap.legs12
                        else -> R.mipmap.legs
                    })
                }
            }

            binding.defenseValueTextViewArmorPicker.text = item.defense[0].toString()
            binding.fireResistanceValueTextViewArmorPicker.text = item.resistances?.getValue("fire").toString()
            binding.waterResistanceValueTextViewArmorPicker.text = item.resistances?.getValue("water").toString()
            binding.thunderResistanceValueTextViewArmorPicker.text = item.resistances?.getValue("thunder").toString()
            binding.iceResistanceValueTextViewArmorPicker.text = item.resistances?.getValue("ice").toString()
            binding.dragonResistanceValueTextViewArmorPicker.text = item.resistances?.getValue("dragon").toString()

            binding.addArmorToEquipmentButton.setOnClickListener {
                val currentEquipment = equipmentViewModel.currentArmorPieces.value
                when (item.type) {
                    "head" -> {
                        currentEquipment?.set(
                            0, SelectedArmor(
                                item,
                                binding.armorPickerSkill1.text as String?, binding.armorPickerSkill2.text as String?
                                , arrayListOf(null, null, null)
                            )
                        )
                        if (currentEquipment != null) {
                            equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                        }
                    }
                    "chest" -> {
                        currentEquipment?.set(
                            1, SelectedArmor(
                                item,
                                binding.armorPickerSkill1.text as String?, binding.armorPickerSkill2.text as String?
                                , arrayListOf(null, null, null)
                            )
                        )
                        if (currentEquipment != null) {
                            equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                        }
                    }
                    "gloves" -> {
                        currentEquipment?.set(
                            2, SelectedArmor(
                                item,
                                binding.armorPickerSkill1.text as String?, binding.armorPickerSkill2.text as String?
                                , arrayListOf(null, null, null)
                            )
                        )
                        if (currentEquipment != null) {
                            equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                        }
                    }
                    "waist" -> {
                        currentEquipment?.set(
                            3, SelectedArmor(
                                item,
                                binding.armorPickerSkill1.text as String?, binding.armorPickerSkill2.text as String?
                                , arrayListOf(null, null, null)
                            )
                        )
                        if (currentEquipment != null) {
                            equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                        }
                    }
                    "legs" -> {
                        currentEquipment?.set(
                            4, SelectedArmor(
                                item,
                                binding.armorPickerSkill1.text as String?, binding.armorPickerSkill2.text as String?
                                , arrayListOf(null, null, null)
                            )
                        )
                        if (currentEquipment != null) {
                            equipmentViewModel.setCurrentArmorPieces(currentEquipment)
                        }
                    }
                }
                Navigation.findNavController(it).navigate(ArmorPickerFragmentDirections.actionArmorPickerFragment2ToBuildCreator())
            }
            binding.details.visibility = View.GONE

            var isExpanded = true
            binding.armorPickerLayout.setOnClickListener {
                val show = toggleLayout(isExpanded, binding.details)
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
            fun from(parent: ViewGroup): ArmorPickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemArmorPickerBinding.inflate(layoutInflater, parent, false)
                return ArmorPickerViewHolder(binding)
            }
        }
    }
}

class ArmorPieceDiffCallback: DiffUtil.ItemCallback<ArmorPiece>(){
    override fun areItemsTheSame(oldItem: ArmorPiece, newItem: ArmorPiece): Boolean {
        return oldItem.armorPieceId == newItem.armorPieceId
    }

    override fun areContentsTheSame(oldItem: ArmorPiece, newItem: ArmorPiece): Boolean {
        return  oldItem == newItem
    }

}

class ArmorPieceListener(val clickListener: (armorPieceId: Int) -> Unit){
    fun onClick(armorPiece: ArmorPiece) = clickListener(armorPiece.armorPieceId)
}