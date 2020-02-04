package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillWithRanks
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import androidx.transition.TransitionManager




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
        val skillName: LiveData<SkillWithRanks> = dataSourceSkillRank.getSkillWithRanks(skillRankId)
        return skillName
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.armorName.text = item.name
        val size = item.skillRankId?.size
        //Log.i("TAG", "Tamaño " + size)
        if (item.skillRankId.toString() != "[]"){
            Log.i("TAG", "" + item.skillRankId?.get(0)!!)
            dataSourceSkillRank.get(item.skillRankId?.get(0)!!).observe(viewLifecycleOwner, Observer {
                it?.let {
                    val skillRankLevel = it.level
                    dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer {it2 ->
                        it2?.let {
                            holder.armorSkill1.text = it2.name + " x" + skillRankLevel
                        }
                    })
                }
            })
            if (item.skillRankId?.size!! > 1) {
                dataSourceSkillRank.get(item.skillRankId?.get(1)!!).observe(viewLifecycleOwner, Observer {
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

//        holder.armorImage.setImageResource(when (item.rarity.toInt()) {
//            0 -> R.drawable.ic_sleep_0
//
//        })

        val isExpanded = position === mExpandedPosition
        holder.details.setVisibility(if (isExpanded) View.VISIBLE else View.GONE)
        holder.itemView.isActivated = isExpanded
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(mRecyclerView)
            notifyDataSetChanged()
        }

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
    }
}