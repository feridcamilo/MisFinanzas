package com.android.misfinanzas.ui.logged.movements

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMovementsBinding
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.sync.SyncState
import com.android.misfinanzas.ui.logged.movements.adapter.MovementsAdapter
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DESCRIPTIONS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.MOVEMENT_DATA
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.getEventBus
import com.android.misfinanzas.utils.hideLoader
import com.android.misfinanzas.utils.showLoader
import com.android.misfinanzas.utils.showShortToast
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MovementsFragment : Fragment(R.layout.fragment_movements) {

    private val viewModel by viewModel<MovementsViewModel>()
    private val binding by viewBinding<FragmentMovementsBinding>()
    private val movementsAdapter by lazy { MovementsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupViewModel()
        setupEvents()
        setupSyncObserver()
    }

    private fun setupRecyclerView() = with(binding.rvMovimientos) {
        adapter = movementsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        movementsAdapter.setOnActionItemListener(actionListener)
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementModel>) {
        if (movements.isEmpty()) {
            context?.showShortToast(R.string.info_no_movements)
            return
        }
        movementsAdapter.submitList(movements)
    }

    private fun setupSearchView() {
        binding.svSearch.setOnClickListener { binding.svSearch.isIconified = false }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you want to change when press search button
                viewModel.filter(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                viewModel.filter(newText.orEmpty())
                return false
            }
        })
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)

        getData()
    }

    private fun getData() {
        showLoader()
        viewModel.getMovements()
    }

    private val viewStateObserver = Observer<MovementsViewState> { state ->
        hideLoader()
        when (state) {
            is MovementsViewState.MovementsLoaded -> {
                loadMovements(state.movements)
            }
            is MovementsViewState.MovementsFiltered -> setupRecyclerViewData(state.movements)
        }
    }

    private fun setupSyncObserver() {
        getEventBus(EventSubject.SYNC).observe(viewLifecycleOwner, syncStateObserver)
    }

    private val syncStateObserver = Observer<Any> { state ->
        when (state) {
            SyncState.Success -> getData()
        }
    }

    private fun loadMovements(movements: List<MovementModel>) = with(binding) {
        if (svSearch.query.isNotEmpty()) {
            svSearch.setQuery(svSearch.query, true)
        } else {
            setupRecyclerViewData(movements)
        }
    }

    private fun setupEvents() = with(binding) {
        btnFiltros.setOnClickListener {
            findNavController().navigate(R.id.filtersFragment)
        }

        btnAddMovement.setOnClickListener {
            navigateToDetails(null)
        }
    }

    private fun navigateToDetails(movement: MovementModel?) {
        val bundle = Bundle()
        movement?.let { bundle.putSerializable(MOVEMENT_DATA, it) }
        bundle.putStringArrayList(DESCRIPTIONS_DATA, viewModel.descriptions as ArrayList<String>)
        findNavController().navigate(R.id.action_to_movementDetailFragment, bundle)
    }

    private val actionListener = object : MovementsAdapter.OnActionItemListener {

        override fun onMovementClicked(movement: MovementModel?) {
            navigateToDetails(movement)
        }

        override fun onDiscardMovementClicked(id: Int) {}

    }

}
