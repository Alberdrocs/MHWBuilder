package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.armorPicker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.ArmorPickerFragmentBinding

class ArmorPickerFragment : Fragment() {

    companion object {
        fun newInstance() = ArmorPickerFragment()
    }

    private lateinit var viewModel: ArmorPickerViewModel

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
        val viewModelFactory = ArmorPickerViewModelFactory(application, dataSource, dataSourceSet,dataSourceSkill, dataSourceSkillRank)

        val armorPickerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ArmorPickerViewModel::class.java)

        binding.armorPickerViewModel = armorPickerViewModel

        val adapter = ArmorPickerAdapter(dataSourceSkill, dataSourceSkillRank, viewLifecycleOwner)
        binding.armorRecyclerView.adapter = adapter
        armorPickerViewModel.getArmorPiecesOfType("head").observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArmorPickerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
