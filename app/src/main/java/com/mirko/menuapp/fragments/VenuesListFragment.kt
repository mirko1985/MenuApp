package com.mirko.menuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.mirko.menuapp.viewmodels.DirectoryViewModel
import com.mirko.menuapp.R
import com.mirko.menuapp.databinding.FragmentVenuesListBinding

class VenuesListFragment : Fragment() {

    lateinit var binding: FragmentVenuesListBinding
    lateinit var directoryViewModel: DirectoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryViewModel = ViewModelProvider(requireActivity()).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_venues_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVenueList()

        directoryViewModel.venuesInfoList.observe(viewLifecycleOwner) { venuesList ->
            if (venuesList != null) {
                (binding.rvVenues.adapter as VenuesAdapter).submitList(venuesList)
            }
        }

        directoryViewModel.venuesList()
    }

    private fun initVenueList() {

        binding.rvVenues.adapter = VenuesAdapter { venueIndex ->
            findNavController().navigate(
                VenuesListFragmentDirections.actionVenuesListFragmentToVenueDetailsFragment(
                    venueIndex
                )
            )
        }
        binding.rvVenues.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

}