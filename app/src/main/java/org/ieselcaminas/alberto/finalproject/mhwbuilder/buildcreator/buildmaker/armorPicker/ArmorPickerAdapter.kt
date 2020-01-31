package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece

class ArmorPickerAdapter : RecyclerView.Adapter<ArmorPickerAdapter.ViewHolder>() {

    var data = listOf<ArmorPiece>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_armor_picker, parent, false)

        return ViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.armorName.text = item.name
        if (item.skillRankId?.get(0) != null){
            holder.armorSkill1.text = item.skillRankId[0].toString()
            if (item.skillRankId?.get(1) != null) {
                holder.armorSkill2.text = item.skillRankId[1].toString()
            }
        }

//        holder.armorImage.setImageResource(when (item.rarity.toInt()) {
//            0 -> R.drawable.ic_sleep_0
//
//        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val armorName: TextView = itemView.findViewById(R.id.armorPickerName)
        val armorSkill1: TextView = itemView.findViewById(R.id.armorPickerSkill1)
        val armorSkill2: TextView = itemView.findViewById(R.id.armorPickerSkill2)
        val armorImage: ImageView = itemView.findViewById(R.id.armorPickerImage)
        val armorSlot1Image: ImageView = itemView.findViewById(R.id.armorPickerSlot1Image)
        val armorSlot2Image: ImageView = itemView.findViewById(R.id.armorPickerSlot2Image)
        val armorSlot3Image: ImageView = itemView.findViewById(R.id.armorPickerSlot3Image)
    }
}