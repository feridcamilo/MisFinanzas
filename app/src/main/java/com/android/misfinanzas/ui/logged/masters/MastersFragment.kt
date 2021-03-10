package com.android.misfinanzas.ui.logged.masters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.domain.model.Master
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMastersBinding
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListFragment
import com.android.misfinanzas.utils.TabsAdapter
import com.android.misfinanzas.utils.setupWithViewPager2
import com.android.misfinanzas.utils.viewbinding.viewBinding

class MastersFragment : Fragment(R.layout.fragment_masters) {

    private val binding by viewBinding<FragmentMastersBinding>()
    private lateinit var tabAdapter: TabsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() = with(binding) {
        tabAdapter = TabsAdapter(this@MastersFragment)
        tabAdapter.addFragment(MastersListFragment(Master.TYPE_PERSON), getString(R.string.masters_people))
        tabAdapter.addFragment(MastersListFragment(Master.TYPE_PLACE), getString(R.string.masters_places))
        tabAdapter.addFragment(MastersListFragment(Master.TYPE_CATEGORY), getString(R.string.masters_categories))
        tabAdapter.addFragment(MastersListFragment(Master.TYPE_DEBT), getString(R.string.masters_debts))

        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager2(viewPager)
        //viewPager.offscreenPageLimit = 1
    }

}
