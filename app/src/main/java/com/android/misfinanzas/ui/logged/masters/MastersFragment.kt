package com.android.misfinanzas.ui.logged.masters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.domain.model.Master
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.databinding.FragmentMastersBinding
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListFragment

class MastersFragment : BaseFragment() {

    private lateinit var binding: FragmentMastersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMastersBinding.inflate(inflater, container, false)
        return binding.root
    }

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
