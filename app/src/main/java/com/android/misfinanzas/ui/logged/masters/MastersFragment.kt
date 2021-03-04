package com.android.misfinanzas.ui.logged.masters

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.domain.model.Master
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMastersBinding
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListFragment
import com.android.misfinanzas.utils.viewbinding.viewBinding

class MastersFragment : Fragment(R.layout.fragment_masters) {

    private val binding by viewBinding<FragmentMastersBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
    }

    private fun setupEvents() = with(binding) {
        btnPeople.setOnClickListener { navigateToMastersList(Master.Companion.TYPE_PERSON) }
        btnPlaces.setOnClickListener { navigateToMastersList(Master.Companion.TYPE_PLACE) }
        btnCategories.setOnClickListener { navigateToMastersList(Master.Companion.TYPE_CATEGORY) }
        btnDebts.setOnClickListener { navigateToMastersList(Master.Companion.TYPE_DEBT) }
    }

    private fun navigateToMastersList(type: Int) {
        val bundle = bundleOf(
            Pair(MastersListFragment.MASTERS_TYPE, type)
        )
        findNavController().navigate(R.id.action_mastersFragment_to_mastersListFragment, bundle)
    }

}
