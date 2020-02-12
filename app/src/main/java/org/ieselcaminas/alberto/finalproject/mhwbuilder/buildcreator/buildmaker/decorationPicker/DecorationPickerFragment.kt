package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class DecorationPickerFragment : Fragment() {

    companion object {
        fun newInstance() = DecorationPickerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DecorationPickerFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.decoration_picker_fragment,  container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).decorationDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceSkillRank = AppDatabase.getInstance(application).skillRankDAO()
        val viewModelFactory = DecorationPickerViewModelFactory(application, dataSource)
        val decorationPickerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(DecorationPickerViewModel::class.java)

        binding.decorationPickerViewModel = decorationPickerViewModel

        val adapter = DecorationPickerAdapter(viewLifecycleOwner, dataSourceSkill, dataSourceSkillRank)

        binding.decorationRecyclerView.adapter = adapter

        val args = DecorationPickerFragmentArgs.fromBundle(arguments!!)
        Log.i("TAGArg", args.slot1.toString())
        decorationPickerViewModel.getDecorationsOfSlot(args.slot1).observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("TAGDecoration", it.toString())
                adapter.data = it
            }
        })

        return binding.root
    }


}
