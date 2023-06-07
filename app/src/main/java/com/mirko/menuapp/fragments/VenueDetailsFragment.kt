package com.mirko.menuapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mirko.menuapp.viewmodels.DirectoryViewModel
import com.mirko.menuapp.viewmodels.LoginViewModel
import com.mirko.menuapp.MenuApp
import com.mirko.menuapp.R
import com.mirko.menuapp.databinding.FragmentVenueDetailsBinding
import kotlinx.coroutines.launch

class VenueDetailsFragment : Fragment() {

    private lateinit var binding: FragmentVenueDetailsBinding
    lateinit var directoryViewModel: DirectoryViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryViewModel = ViewModelProvider(requireActivity()).get()
        loginViewModel = ViewModelProvider(requireActivity()).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_venue_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val venueIndex = VenueDetailsFragmentArgs.fromBundle(requireArguments()).detailsIndex
        val venueDetails = directoryViewModel.venueDetailsList.value?.get(venueIndex).let {
            DirectoryViewModel.VenueDetails(
                it?.venue?.name,
                it?.venue?.description,
                if (it?.venue?.is_open == true) MenuApp.instance.getString(R.string.opened)
                else MenuApp.instance.getString(R.string.closed),
                it?.venue?.welcome_message,
                it?.venue?.image?.thumbnail_medium
            )
        }
        binding.venueDetails = venueDetails

        binding.btLogout.setOnClickListener {
            lifecycleScope.launch {
                loginViewModel.logOut()
                findNavController().navigate(VenueDetailsFragmentDirections.actionVenueDetailsFragmentToLoginFragment())
            }
        }
    }
}