package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillWithRanks
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations


class ArmorPickerAdapter(
    private val dataSourceSkill: SkillsDAO,
    private val dataSourceSkillRank: SkillRankDAO,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ArmorPickerAdapter.ViewHolder>() {


    var data = listOf<ArmorPiece>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var mRecyclerView: RecyclerView
    var mExpandedPosition = -1

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_armor_picker, parent, false)

        return ViewHolder(
            view
        )
    }
    private fun getSkill(skillRankId: Int): LiveData<SkillWithRanks> {
        return dataSourceSkillRank.getSkillWithRanks(skillRankId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.armorName.text = item.name
        if (item.skillRankId.toString() != "[]") {
            Log.i("TAG", "" + item.skillRankId?.get(0)!!)
            dataSourceSkillRank.get(item.skillRankId?.get(0)).observe(viewLifecycleOwner, Observer {
                it?.let {
                    val skillRankLevel = it.level
                    dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer { it2 ->
                        it2?.let {
                            holder.armorSkill1.text = it2.name + " x" + skillRankLevel
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
                                holder.armorSkill2.text = it2.name + " x" + skillRankLevel
                            }
                        })
                    }
                })
            } else {
                holder.armorSkill2.text = ""
            }
        } else {
            holder.armorSkill1.text = ""
            holder.armorSkill2.text = ""
        }
        Log.i("TAGSlot", item.slots.toString())
        if (item.slots != null){
            holder.armorSlot1Image.setImageResource(when (item.slots[0]) {
                1 -> R.mipmap.gem_level_1
                2 -> R.mipmap.gem_level_2
                3 -> R.mipmap.gem_level_3
                4 -> R.mipmap.gem_level_4
                else -> 0
            })
            if (item.slots.size > 1){
                holder.armorSlot2Image.setImageResource(when (item.slots[1]) {
                    1 -> R.mipmap.gem_level_1
                    2 -> R.mipmap.gem_level_2
                    3 -> R.mipmap.gem_level_3
                    4 -> R.mipmap.gem_level_4
                    else -> 0
                })
                if (item.slots.size > 2){
                    holder.armorSlot3Image.setImageResource(when (item.slots[2]) {
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
                holder.armorImage.setImageResource(when (item.rarity.toInt()) {
                10 -> R.mipmap.head10
                11 -> R.mipmap.head11
                12 -> R.mipmap.head12
                else -> R.mipmap.head
            })
            }
            "chest" -> {
                holder.armorImage.setImageResource(when (item.rarity.toInt()) {
                    10 -> R.mipmap.chest10
                    11 -> R.mipmap.chest11
                    12 -> R.mipmap.chest12
                    else -> R.mipmap.chest
                })
            }
            "gloves" -> {
                holder.armorImage.setImageResource(when (item.rarity.toInt()) {
                    10 -> R.mipmap.gloves10
                    11 -> R.mipmap.gloves11
                    12 -> R.mipmap.gloves12
                    else -> R.mipmap.gloves
                })
            }
            "waist" -> {
                holder.armorImage.setImageResource(when (item.rarity.toInt()) {
                    10 -> R.mipmap.waist10
                    11 -> R.mipmap.waist11
                    12 -> R.mipmap.waist12
                    else -> R.mipmap.waist
                })
            }
            "legs" -> {
                holder.armorImage.setImageResource(when (item.rarity.toInt()) {
                    10 -> R.mipmap.legs10
                    11 -> R.mipmap.legs11
                    12 -> R.mipmap.legs12
                    else -> R.mipmap.legs
                })
            }
        }

        holder.defenseValue.text = item.defense[0].toString()
        holder.fireResistanceValue.text = item.resistances?.getValue("fire").toString()
        holder.waterResistanceValue.text = item.resistances?.getValue("water").toString()
        holder.thunderResistanceValue.text = item.resistances?.getValue("thunder").toString()
        holder.iceResistanceValue.text = item.resistances?.getValue("ice").toString()
        holder.dragonResistanceValue.text = item.resistances?.getValue("dragon").toString()

        holder.button.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_armorPickerFragment2_to_buildCreator)
        }

        var isExpanded = true
        holder.itemView.setOnClickListener {
            val show = toggleLayout(isExpanded, holder.details)
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val armorName: TextView = itemView.findViewById(R.id.armorPickerName)
        val armorSkill1: TextView = itemView.findViewById(R.id.armorPickerSkill1)
        val armorSkill2: TextView = itemView.findViewById(R.id.armorPickerSkill2)
        val armorImage: ImageView = itemView.findViewById(R.id.armorPickerImage)
        val armorSlot1Image: ImageView = itemView.findViewById(R.id.armorPickerSlot1Image)
        val armorSlot2Image: ImageView = itemView.findViewById(R.id.armorPickerSlot2Image)
        val armorSlot3Image: ImageView = itemView.findViewById(R.id.armorPickerSlot3Image)
        val details:LinearLayout = itemView.findViewById(R.id.details)
        val button: Button = itemView.findViewById(R.id.addArmorToEquipmentButton)
        val defenseValue:TextView = itemView.findViewById(R.id.defenseValueTextViewArmorPicker)
        val fireResistanceValue:TextView = itemView.findViewById(R.id.fireResistanceValueTextViewArmorPicker)
        val waterResistanceValue:TextView = itemView.findViewById(R.id.waterResistanceValueTextViewArmorPicker)
        val thunderResistanceValue:TextView = itemView.findViewById(R.id.thunderResistanceValueTextViewArmorPicker)
        val iceResistanceValue:TextView = itemView.findViewById(R.id.iceResistanceValueTextViewArmorPicker)
        val dragonResistanceValue:TextView = itemView.findViewById(R.id.dragonResistanceValueTextViewArmorPicker)
    }
}