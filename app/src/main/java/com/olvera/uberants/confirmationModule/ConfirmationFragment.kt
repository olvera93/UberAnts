package com.olvera.uberants.confirmationModule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.transition.MaterialSharedAxis
import com.olvera.uberants.R
import com.olvera.uberants.databinding.FragmentConfirmationBinding

class ConfirmationFragment: Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()

    }

    private fun setupButtons() {
        binding.btnDone.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_confirmation_to_tracking)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}