package org.ieselcaminas.alberto.finalproject.mhwbuilder


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.BuildCreatorDirections
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTitleBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_title,  container, false)
        binding.createBuildButton.setOnClickListener {view: View ->
            Navigation.findNavController(view).navigate(TitleFragmentDirections.actionTitleFragmentToBuildCreator())
        }
        return binding.root
    }

}
