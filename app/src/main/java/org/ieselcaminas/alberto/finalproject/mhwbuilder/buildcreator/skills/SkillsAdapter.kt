package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.SkillsForDisplay
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ListItemSkillsBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations

class SkillsAdapter(): RecyclerView.Adapter<SkillsAdapter.SkillsViewHolder>(){

    var data = listOf<SkillsForDisplay>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
        return SkillsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class SkillsViewHolder private constructor(val binding: ListItemSkillsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: SkillsForDisplay){
            binding.skillNameTextView.text = item.skill.name
            binding.skillActiveLevelTextView.text = "Level " + item.activeLevels
            for (i in 0 until binding.skillLevelsLinearLayout.childCount){
                val image = binding.skillLevelsLinearLayout.getChildAt(i) as ImageView
                val levelLayout = binding.linearLayoutLevelsDescriptions.getChildAt(i) as LinearLayout
                if (item.skillRanks.size <= i){
                    image.setImageResource(android.R.color.transparent)
                    levelLayout.visibility = View.GONE
                } else {
                    image.setImageResource(if (item.activeLevels > i) R.mipmap.skill_active else R.mipmap.skill_unactive)
                    val textLevel = levelLayout.getChildAt(0) as TextView
                    val textLevelDescription = levelLayout.getChildAt(1) as TextView
                    textLevelDescription.text = item.skillRanks[i].skillDescription
                    if (i < item.activeLevels){
                        textLevel.setTextColor(Color.parseColor("#F0810B"))
                        textLevelDescription.setTextColor(Color.parseColor("#F0810B"))
                    }
                }
            }
            Log.i("ColorTAG",item.skill.color.toString())
            binding.skillImage.setImageResource(when(item.skill.color){
                "white" -> R.mipmap.skill_white
                "red" -> R.mipmap.skill_red
                "blue" -> R.mipmap.skill_blue
                "green" -> R.mipmap.skill_green
                "dark green" -> R.mipmap.skill_dark_green
                "yellow" -> R.mipmap.skill_yellow
                "orange" -> R.mipmap.skill_orange
                "light yellow" -> R.mipmap.skill_light_yellow
                "purple" -> R.mipmap.skill_purple
                "dark purple" -> R.mipmap.skill_dark_purple
                else -> R.mipmap.skill_white
            })

            var isExpanded = true
            binding.mainLayout.setOnClickListener {
                val show = toggleLayout(isExpanded, binding.linearLayoutLevelsDescriptions)
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
            fun from(parent: ViewGroup): SkillsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSkillsBinding.inflate(layoutInflater, parent, false)
                return SkillsViewHolder(binding)
            }
        }
    }

}