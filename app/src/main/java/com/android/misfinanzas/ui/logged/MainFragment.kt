package com.android.misfinanzas.ui.logged

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.misfinanzas.MainActivity
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.balanceFragment,
                R.id.movementsFragment,
                R.id.mastersFragment,
                R.id.syncFragment
            )
        )

        val navHostFragment = childFragmentManager.findFragmentById(R.id.logged_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        (activity as MainActivity).navController = navController
        setupActionBarWithNavController((activity as AppCompatActivity), navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
    }

}
