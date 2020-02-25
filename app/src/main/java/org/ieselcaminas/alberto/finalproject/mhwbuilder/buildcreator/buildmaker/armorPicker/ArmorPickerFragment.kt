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

    var filterApplied = false
    var armorListFiltered: ArrayList<ArmorPiece> = ArrayList()
    var isExpanded = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ArmorPickerFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.armor_picker_fragment, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceSkillRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceCharm = AppDatabase.getInstance(application).charmsDAO()
        val viewModelFactory = ArmorPickerViewModelFactory(application, dataSource, dataSourceSet, dataSourceSkill, dataSourceSkillRank, binding)
        val armorPickerViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArmorPickerViewModel::class.java)
        binding.armorPickerViewModel = armorPickerViewModel

        val equipmentViewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet, dataSourceSkillRank, dataSourceSkill, dataSourceCharm)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(this, equipmentViewModelFactory).get(EquipmentViewModel::class.java)
        }

        val args = ArmorPickerFragmentArgs.fromBundle(arguments!!)


        val adapter = equipmentViewModel?.let { ArmorPickerAdapter(dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner, it) }
        binding.armorRecyclerView.adapter = adapter
        armorPickerViewModel.getArmorPiecesOfType(args.armorType, 9, 12).observe(viewLifecycleOwner, Observer {
            it?.let {
                if (adapter != null) {
                    adapter.data = it
                }
            }
        })


        getQueriedArmors(armorPickerViewModel, adapter, args)


        val rarityLevelsList: ArrayList<String> = ArrayList()
        for (i in 1 until 13) rarityLevelsList.add("Level $i")
        setFilterSpinnersRarityAdapter(rarityLevelsList, binding)


        val skillNameList: ArrayList<String> = ArrayList()
        val skillIdList: HashMap<String, ArrayList<Int>> = HashMap()
        setFilterSkillAutoCompleteTextView(armorPickerViewModel, skillNameList, skillIdList, binding)


        val slotsList: ArrayList<String> = ArrayList()
        for (i in 1 until 4) if (i != 3) slotsList.add("$i or more") else slotsList.add("$i")
        setFiltersSpinnerDecorationsAdapter(binding, rarityLevelsList, slotsList)



        applyFilters(binding, armorPickerViewModel, args, skillIdList, adapter)



        binding.filterButton.setOnClickListener {
            expandCollapseAnimation(binding)
        }


        binding.lifecycleOwner = this
        return binding.root
    }

    private fun expandCollapseAnimation(binding: ArmorPickerFragmentBinding) {
        if (isExpanded) {
            binding.filterButton.text = "Hide filter options"
        } else {
            binding.filterButton.text = "Show filter options"
        }
        isExpanded = !toggleLayout(isExpanded, binding.filterDetails)
    }

    private fun setFiltersSpinnerDecorationsAdapter(
        binding: ArmorPickerFragmentBinding,
        rarityLevelsList: ArrayList<String>,
        slotsList: ArrayList<String>
    ) {
        binding.decorationSlotLevelSpinner.adapter = context?.let { context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(0, 4)
            )
        }

        binding.decorationSlotsNumberSpinner.adapter = context?.let { context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, slotsList
            )
        }
    }

    private fun applyFilters(
        binding: ArmorPickerFragmentBinding,
        armorPickerViewModel: ArmorPickerViewModel,
        args: ArmorPickerFragmentArgs,
        skillIdList: HashMap<String, ArrayList<Int>>,
        adapter: ArmorPickerAdapter?
    ) {
        binding.applyFiltersButton.setOnClickListener {
            armorListFiltered = ArrayList()
            armorPickerViewModel.getArmorPiecesOfType(
                args.armorType, binding.rarityFromSpinner.selectedItem.toString().substring(6).toInt(),
                binding.rarityToSpinner.selectedItem.toString().substring(6).toInt()
            ).observe(viewLifecycleOwner, Observer { armorPieceList ->
                armorPieceList?.let {
                    it.forEach { armorPiece ->
                        if (binding.armorSkillToSearchAutoCompleteTextView.text.toString() != "") {
                            if (armorPiece.skillRankId.toString() != "[]") {
                                try {
                                    if (skillIdList[binding.armorSkillToSearchAutoCompleteTextView.text.toString()]!!.contains(armorPiece.skillRankId!![0])) {
                                        checkAndAddDecorationsForFilter(armorPiece, binding)
                                    }
                                    if (armorPiece.skillRankId.size > 1) {
                                        if (skillIdList[binding.armorSkillToSearchAutoCompleteTextView.text.toString()]!!.contains(armorPiece.skillRankId[1])) {
                                            checkAndAddDecorationsForFilter(armorPiece, binding)
                                        }
                                    }
                                } catch (e: KotlinNullPointerException) {
                                    armorListFiltered = ArrayList()
                                }
                            }
                        } else {
                            checkAndAddDecorationsForFilter(armorPiece, binding)
                        }
                    }
                }
                if (adapter != null) {
                    adapter.data = armorListFiltered
                }
            })
            filterApplied = true
            expandCollapseAnimation(binding)
        }
    }

    private fun setFilterSkillAutoCompleteTextView(
        armorPickerViewModel: ArmorPickerViewModel,
        skillNameList: ArrayList<String>,
        skillIdList: HashMap<String, ArrayList<Int>>,
        binding: ArmorPickerFragmentBinding
    ) {
        armorPickerViewModel.getAllSkills().observe(viewLifecycleOwner, Observer {
            it?.let {
                it.forEach { skillWithRanks ->
                    skillNameList.add(skillWithRanks.skill.name)
                    val skillRankArrayList: ArrayList<Int> = ArrayList()
                    skillWithRanks.skillRank.forEach {skillRank ->
                        skillRankArrayList.add(skillRank.skillRankId)
                    }
                    skillIdList[skillWithRanks.skill.name] = skillRankArrayList
                }
                val autoCompleteAdapter = context?.let { context -> ArrayAdapter(context, android.R.layout.select_dialog_item, skillNameList) }
                binding.armorSkillToSearchAutoCompleteTextView.threshold = 2
                binding.armorSkillToSearchAutoCompleteTextView.setAdapter(autoCompleteAdapter)
            }
        })
    }

    private fun setFilterSpinnersRarityAdapter(
        rarityLevelsList: ArrayList<String>,
        binding: ArmorPickerFragmentBinding
    ) {
        val arrayAdapterFrom = context?.let { context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, rarityLevelsList
            )
        }

        val arrayAdapterTo = context?.let { context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                rarityLevelsList.subList(binding.rarityFromSpinner.selectedItemPosition + 1, rarityLevelsList.size - 1)
            )
        }

        binding.rarityFromSpinner.adapter = arrayAdapterFrom
        binding.rarityToSpinner.adapter = arrayAdapterTo

        binding.rarityFromSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                binding.rarityToSpinner.adapter = context?.let { context ->
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_dropdown_item, rarityLevelsList.subList(position, rarityLevelsList.size)
                    )
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
    }

    private fun getQueriedArmors(
        armorPickerViewModel: ArmorPickerViewModel,
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
                armorPickerViewModel.getArmorPiecesOfType(args.armorType, 9, 12).observe(viewLifecycleOwner, Observer {
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
        binding: ArmorPickerFragmentBinding
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
