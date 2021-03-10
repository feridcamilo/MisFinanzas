package com.android.misfinanzas.utils

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItemCount() = mFragmentList.size

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        if (!mFragmentList.contains(fragment)) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }

    fun getPageTitle(position: Int): String {
        return mFragmentTitleList[position]
    }

    fun enabledTab(tabLayout: TabLayout, index: Int, enabled: Boolean) {
        getViewGroup(tabLayout).getChildAt(index)?.isEnabled = enabled
    }

    private fun getViewGroup(tabLayout: TabLayout): ViewGroup {
        return (tabLayout.getChildAt(0) as ViewGroup)
    }
}
