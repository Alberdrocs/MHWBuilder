package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.BuildCreatorDirections
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece

class EquipmentAdapter(): RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {


    var data = listOf<SelectedArmor>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var mRecyclerView: RecyclerView

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_armor_piece, parent, false)

        return ViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.armorName.text = item.armorPiece.name
        holder.armorDecoration1.text = ""
        holder.armorDecoration2.text = ""
        holder.armorDecoration3.text = ""
        holder.cardView.setOnClickListener {
            Navigation.findNavController(it).navigate(BuildCreatorDirections.actionBuildCreatorToArmorPickerFragment2(item.armorPiece.type))
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.cardViewEquipmentArmorPiece)
        val armorName:TextView = itemView.findViewById(R.id.armorTextView)
        val armorDecoration1: TextView = itemView.findViewById(R.id.armorDecoration1TextView)
        val armorDecoration2: TextView = itemView.findViewById(R.id.armorDecoration2TextView)
        val armorDecoration3: TextView = itemView.findViewById(R.id.armorDecoration3TextView)

    }
}

class SelectedArmor(val armorPiece: ArmorPiece, val skillName1:String?, val skillName2:String?){
}