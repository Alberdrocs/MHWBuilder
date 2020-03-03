package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.BuildCreatorDirections
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations

class EquipmentAdapter(private val activity: FragmentActivity?) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {


    var data = listOf<SelectedArmor>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var mRecyclerView: RecyclerView


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_armor_piece, parent, false)

        return EquipmentViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val item = data[position]
        holder.armorName.text = item.armorPiece.name
        holder.armorDecoration1.text = ""
        holder.armorDecoration2.text = ""
        holder.armorDecoration3.text = ""
        holder.skill1.text = item.skillName1
        holder.skill2.text = item.skillName2

        when (item.armorPiece.type){
            "head" -> {
                holder.armorImage.setImageResource(when (item.armorPiece.rarity.toInt()) {
                    9 -> R.mipmap.head9
                    10 -> R.mipmap.head10
                    11 -> R.mipmap.head11
                    12 -> R.mipmap.head12
                    else -> R.mipmap.head
                })
            }
            "chest" -> {
                holder.armorImage.setImageResource(when (item.armorPiece.rarity.toInt()) {
                    9 -> R.mipmap.chest9
                    10 -> R.mipmap.chest10
                    11 -> R.mipmap.chest11
                    12 -> R.mipmap.chest12
                    else -> R.mipmap.chest
                })
            }
            "gloves" -> {
                holder.armorImage.setImageResource(when (item.armorPiece.rarity.toInt()) {
                    9 -> R.mipmap.gloves9
                    10 -> R.mipmap.gloves10
                    11 -> R.mipmap.gloves11
                    12 -> R.mipmap.gloves12
                    else -> R.mipmap.gloves
                })
            }
            "waist" -> {
                holder.armorImage.setImageResource(when (item.armorPiece.rarity.toInt()) {
                    9 -> R.mipmap.waist9
                    10 -> R.mipmap.waist10
                    11 -> R.mipmap.waist11
                    12 -> R.mipmap.waist12
                    else -> R.mipmap.waist
                })
            }
            "legs" -> {
                holder.armorImage.setImageResource(when (item.armorPiece.rarity.toInt()) {
                    9 -> R.mipmap.legs9
                    10 -> R.mipmap.legs10
                    11 -> R.mipmap.legs11
                    12 -> R.mipmap.legs12
                    else -> R.mipmap.legs
                })
            }
        }
        if(item.armorPiece.slots != null){
            holder.changeDecorationsButton.visibility = View.VISIBLE
            if (item.armorPiece.slots.size > 0){
                holder.armorDecoration1Details.visibility = View.VISIBLE
                holder.armorDecoration1Details.setCompoundDrawablesWithIntrinsicBounds(checkDecorationSlot(item.armorPiece.slots[0],holder),null,null,null)
                if (item.armorPiece.slots.size > 1){
                    holder.armorDecoration2Details.visibility = View.VISIBLE
                    holder.armorDecoration2Details.setCompoundDrawablesWithIntrinsicBounds(checkDecorationSlot(item.armorPiece.slots[1],holder),null,null,null)
                    if (item.armorPiece.slots.size > 2){
                        holder.armorDecoration3Details.visibility = View.VISIBLE
                        holder.armorDecoration3Details.setCompoundDrawablesWithIntrinsicBounds(checkDecorationSlot(item.armorPiece.slots[2],holder),null,null,null)
                    }
                }
            }
        } else {
            holder.changeDecorationsButton.visibility = View.GONE
        }

        if (item.decorations.toString() != "[]"){
            holder.armorDecoration1Details.text = "  " + (item.decorations[0]?.name ?: "Skill 1")
            if (item.decorations.size > 1){
                holder.armorDecoration2Details.text = "  " + (item.decorations[1]?.name ?: "Skill 2")
                if (item.decorations.size > 2){
                    holder.armorDecoration3Details.text = "  " + (item.decorations[2]?.name ?: "Skill 3")
                }
            }
        }

        holder.changeDecorationsButton.setOnClickListener {view ->
            val alertDialog = activity?.let {
                val builder = AlertDialog.Builder(it)
                val array = Array(item.armorPiece.slots!!.size) { i -> "Decoration " + (i+1) }
                var selectedItem = 0
                builder.setTitle("Choose the desired Decoration")
                    .setSingleChoiceItems(array, 0
                    ) { _, i ->
                        selectedItem = i
                    }
                    .setPositiveButton("OK"
                    ) { _, _ ->
                        Navigation.findNavController(view).navigate(BuildCreatorDirections.actionBuildCreatorToDecorationPickerFragment(
                            item.armorPiece.slots[selectedItem],selectedItem, item.armorPiece.type))
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")


            alertDialog.show()
        }


        holder.changeArmorButton.setOnClickListener {
            Navigation.findNavController(it).navigate(BuildCreatorDirections.actionBuildCreatorToArmorPickerFragment2(item.armorPiece.type))
        }

        var isExpanded = true
        holder.cardView.setOnClickListener {
            val show = toggleLayout(isExpanded, holder.details)
            isExpanded = !show
        }
    }

    private fun checkDecorationSlot(slot: Int, holder:EquipmentViewHolder): Drawable? {
        return when (slot){
            1 ->   holder.itemView.context.resources.getDrawable(R.mipmap.gem_level_1)
            2 ->   holder.itemView.context.resources.getDrawable(R.mipmap.gem_level_2)
            3 ->   holder.itemView.context.resources.getDrawable(R.mipmap.gem_level_3)
            4 ->   holder.itemView.context.resources.getDrawable(R.mipmap.gem_level_4)
            else -> holder.itemView.context.resources.getDrawable(R.mipmap.gem_level_1)
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

    class EquipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        val cardView: CardView = itemView.findViewById(R.id.cardViewEquipmentArmorPiece)
        val details: LinearLayout = itemView.findViewById(R.id.armorPieceDetails)
        val armorName: TextView = itemView.findViewById(R.id.armorTextView)
        val armorDecoration1: TextView = itemView.findViewById(R.id.armorDecoration1TextView)
        val armorDecoration2: TextView = itemView.findViewById(R.id.armorDecoration2TextView)
        val armorDecoration3: TextView = itemView.findViewById(R.id.armorDecoration3TextView)
        val armorImage: ImageView = itemView.findViewById(R.id.armorImageView)
        val skill1: TextView = itemView.findViewById(R.id.armorSkill1TextView)
        val skill2: TextView = itemView.findViewById(R.id.armorSkill2TextView)
        val changeArmorButton: Button = itemView.findViewById(R.id.changeArmorPieceButton)
        val changeDecorationsButton: Button = itemView.findViewById(R.id.changeDecorationsArmorPieceButton)
        val armorDecoration1Details: TextView = itemView.findViewById(R.id.detailsArmorDecoration1TextView)
        val armorDecoration2Details: TextView = itemView.findViewById(R.id.detailsArmorDecoration2TextView)
        val armorDecoration3Details: TextView = itemView.findViewById(R.id.detailsArmorDecoration3TextView)
    }
}




