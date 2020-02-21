package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModelFactory
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ArmorPickerFragmentBinding
import org.ieselcaminas.alberto.finalproject.mhwbuilder.util.Animations
import android.widget.AdapterView.OnItemSelectedListener




class ArmorPickerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ArmorPickerFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.armor_picker_fragment,  container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceSkillRank = AppDatabase.getInstance(application).skillRankDAO()
        val viewModelFactory = ArmorPickerViewModelFactory(application, dataSource, dataSourceSet,dataSourceSkill, dataSourceSkillRank,binding)
        val armorPickerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ArmorPickerViewModel::class.java)

        val equipmentViewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet, dataSourceSkillRank, dataSourceSkill, viewLifecycleOwner)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(
                this, equipmentViewModelFactory).get(EquipmentViewModel::class.java) }

        binding.armorPickerViewModel = armorPickerViewModel

        val args = ArmorPickerFragmentArgs.fromBundle(arguments!!)

        val adapter = equipmentViewModel?.let {
            ArmorPickerAdapter(ArmorPieceListener {
                armorPieceId -> armorPickerViewModel.startNavigationToArmorPiece(armorPieceId)
            }, dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, it)
        }
        binding.armorRecyclerView.adapter = adapter
        armorPickerViewModel.getArmorPiecesOfType(args.armorType).observe(viewLifecycleOwner, Observer {
            it?.let {
                if (adapter != null) {
                    adapter.data = it
                }
            }
        })

        armorPickerViewModel.armorSearchQuery.observe(viewLifecycleOwner, Observer { query ->
            val armorListQueried = ArrayList<ArmorPiece>()
            armorPickerViewModel.getArmorPiecesOfType(args.armorType).observe(viewLifecycleOwner, Observer {
                it?.let {
                    for (i in it)
                        if (i.name.toLowerCase().contains(query.toLowerCase()))
                            armorListQueried.add(i)
                            adapter?.data = armorListQueried
                }
            })
        })

        val rarityLevelsList: ArrayList<String> = ArrayList()
        for (i in 1 until 13) rarityLevelsList.add("Level $i")

        val arrayAdapterFrom = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList
            )
        }

        val arrayAdapterTo = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(binding.rarityFromSpinner.selectedItemPosition + 1, rarityLevelsList.size-1)
            )
        }

        binding.rarityFromSpinner.adapter = arrayAdapterFrom
        binding.rarityToSpinner.adapter = arrayAdapterTo

        binding.rarityFromSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                binding.rarityToSpinner.adapter = context?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(position, rarityLevelsList.size)
                    )
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }

        var isExpanded = true
        binding.filterButton.setOnClickListener {
            val show = toggleLayout(isExpanded, binding.filterDetails)
            isExpanded = !show
        }


        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun toggleLayout(isExpanded: Boolean, layoutExpand: ConstraintLayout): Boolean {
        if (isExpanded) {
            Animations.expand(layoutExpand)
        } else {
            Animations.collapse(layoutExpand)
        }
        return isExpanded
    }

}
