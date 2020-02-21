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
import android.widget.LinearLayout


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


        var armorListFiltered: ArrayList<ArmorPiece> = ArrayList()
        var filterApplied = false


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

        getQueriedArmors(armorPickerViewModel, filterApplied, armorListFiltered, adapter, args)

        val rarityLevelsList: ArrayList<String> = ArrayList()
        for (i in 1 until 13) rarityLevelsList.add("Level $i")

        val arrayAdapterFrom = context?.let {context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList
            )
        }

        val arrayAdapterTo = context?.let {context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                rarityLevelsList.subList(binding.rarityFromSpinner.selectedItemPosition + 1, rarityLevelsList.size-1))
        }

        binding.rarityFromSpinner.adapter = arrayAdapterFrom
        binding.rarityToSpinner.adapter = arrayAdapterTo

        binding.rarityFromSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                binding.rarityToSpinner.adapter = context?.let {context ->
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(position, rarityLevelsList.size))
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        val skillNameList: ArrayList<String> = ArrayList()
        val skillIdList: HashMap<String, ArrayList<Int>> = HashMap()
        armorPickerViewModel.getAllSkills().observe(viewLifecycleOwner, Observer {
            it?.let {
                it.forEach {skillWithRanks ->
                    skillNameList.add(skillWithRanks.skill.name)
                    val skillRankArrayList: ArrayList<Int> = ArrayList()
                    skillWithRanks.skillRank.forEach {
                            skillRankArrayList.add(it.skillRankId)
                        }
                    skillIdList[skillWithRanks.skill.name] = skillRankArrayList
                }
                val autoCompleteAdapter = context?.let { context -> ArrayAdapter(context, android.R.layout.select_dialog_item, skillNameList) }
                binding.armorSkillToSearchAutoCompleteTextView.threshold = 2
                binding.armorSkillToSearchAutoCompleteTextView.setAdapter(autoCompleteAdapter)
            }
        })

        val slotsList: ArrayList<String> = ArrayList()
        for (i in 1 until 4) if (i != 3) slotsList.add("$i or more") else slotsList.add("$i")

        binding.decorationSlotLevelSpinner.adapter = context?.let {context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(0, 4))
        }

        binding.decorationSlotsNumberSpinner.adapter = context?.let {context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, slotsList)
        }



        binding.applyFiltersButton.setOnClickListener {
            armorListFiltered = ArrayList()
            armorPickerViewModel.getArmorPiecesOfType(args.armorType).observe(viewLifecycleOwner, Observer { armorPieceList ->
                armorPieceList?.let {
                    it.forEach { armorPiece ->
                        if (armorPiece.rarity >= binding.rarityFromSpinner.selectedItem.toString().substring(6).toInt() &&
                            armorPiece.rarity <= binding.rarityToSpinner.selectedItem.toString().substring(6).toInt()
                        ) {
                            if (binding.armorSkillToSearchAutoCompleteTextView.text.toString() != "") {
                                if (armorPiece.skillRankId.toString() != "[]") {
                                    try {
                                        if (skillIdList.get(binding.armorSkillToSearchAutoCompleteTextView.text.toString())!!.contains(armorPiece.skillRankId!![0])) {
                                            checkAndAddDecorationsForFilter(armorPiece, binding, armorListFiltered)
                                        }
                                        if (armorPiece.skillRankId.size > 1) {
                                            if (skillIdList.get(binding.armorSkillToSearchAutoCompleteTextView.text.toString())!!.contains(armorPiece.skillRankId[1])) {
                                                checkAndAddDecorationsForFilter(armorPiece, binding, armorListFiltered)
                                            }
                                        }
                                    } catch (e: KotlinNullPointerException) {
                                        armorListFiltered = ArrayList()
                                    }
                                }
                            } else {
                                checkAndAddDecorationsForFilter(armorPiece, binding, armorListFiltered)
                            }
                        }
                    }
                }
                if (adapter != null) {
                    adapter.data = armorListFiltered
                }
            })
            filterApplied = true
        }


        var isExpanded = true
        binding.filterButton.setOnClickListener {
            val show = toggleLayout(isExpanded, binding.filterDetails)
            if (isExpanded){
                binding.filterButton.text = "Hide filter options"
            } else{
                binding.filterButton.text = "Show filter options"
            }
            isExpanded = !show
        }


        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun getQueriedArmors(
        armorPickerViewModel: ArmorPickerViewModel,
        filterApplied: Boolean,
        armorListFiltered: ArrayList<ArmorPiece>,
        adapter: ArmorPickerAdapter?,
        args: ArmorPickerFragmentArgs
    ) {
        armorPickerViewModel.armorSearchQuery.observe(viewLifecycleOwner, Observer { query ->
            val armorListQueried = ArrayList<ArmorPiece>()
            if (filterApplied) {
                Log.i("TAG", "Entra")
                Log.i("TAG", "Entra2 " + armorListFiltered.toString())
                for (i in armorListFiltered)
                    if (i.name.toLowerCase().contains(query.toLowerCase()))
                        armorListQueried.add(i)
                adapter?.data = armorListQueried
            } else {
                armorPickerViewModel.getArmorPiecesOfType(args.armorType).observe(viewLifecycleOwner, Observer {
                    it?.let {
                        for (i in it)
                            if (i.name.toLowerCase().contains(query.toLowerCase()))
                                armorListQueried.add(i)
                        adapter?.data = armorListQueried
                    }
                })
            }

        })
    }

    private fun checkAndAddDecorationsForFilter(
        armorPiece: ArmorPiece,
        binding: ArmorPickerFragmentBinding,
        armorListFiltered: ArrayList<ArmorPiece>
    ) {
        if (armorPiece.slots?.size ?: 0 > binding.decorationSlotsNumberSpinner.selectedItemPosition) {
            var canBeAdded = false
            armorPiece.slots!!.forEach {
                if (it > binding.decorationSlotLevelSpinner.selectedItemPosition.toString().toInt()) {
                    canBeAdded = true
                }
            }
            if (canBeAdded) armorListFiltered.add(armorPiece)
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

}
