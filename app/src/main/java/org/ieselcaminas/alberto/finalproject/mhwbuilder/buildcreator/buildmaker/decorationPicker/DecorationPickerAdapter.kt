package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ListItemDecorationPickerBinding

class DecorationPickerAdapter(
    val viewLifecycleOwner: LifecycleOwner,
    val dataSourceSkill: SkillsDAO,
    val dataSourceSkillRank: SkillRankDAO
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
        holder.bind(item, dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner)
    }


    class ViewHolder private constructor(val binding: ListItemDecorationPickerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Decoration, dataSourceSkill: SkillsDAO, dataSourceSkillRank: SkillRankDAO, viewLifecycleOwner: LifecycleOwner){
            binding.decoration = item

            binding.decorationNameDecorationPickerTextView.text = item.name
            item.skillRankId?.get(0)?.let { dataSourceSkillRank.get(it) }
                ?.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        val level = it.level
                        dataSourceSkill.get(it.skillId).observe(viewLifecycleOwner, Observer { it2 ->
                            it2?.let {
                                binding.decorationSkillName1DecorationPickerTextView.text = it2.name + " x" + level
                            }
                        })
                    }
                })

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