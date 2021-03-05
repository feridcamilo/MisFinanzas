package com.android.misfinanzas.ui.logged.movements

import android.os.Bundle
import android.os.Parcelable
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
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewState
import com.android.misfinanzas.ui.logged.movements.adapter.MovementsAdapter
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.CATEGORIES_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DEBTS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DESCRIPTIONS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.MOVEMENT_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PEOPLE_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PLACES_DATA
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
    private val mastersViewModel by viewModel<MastersListViewModel>()
    private val binding by viewBinding<FragmentMovementsBinding>()
    private val movementsAdapter by lazy { MovementsAdapter() }

    private var movementToAdd: MovementModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(MOVEMENT_DATA)) {
                movementToAdd = it.getSerializable(MOVEMENT_DATA) as? MovementModel
            }
        }
    }

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
                //implement if you can to change when press search button
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
        mastersViewModel.viewState.observe(viewLifecycleOwner, mastersViewStateObserver)
        getData()
    }

    private fun getData() {
        showLoader()
        mastersViewModel.getPeople()
    }

    private val mastersViewStateObserver = Observer<MastersListViewState> { state ->
        when (state) {
            is MastersListViewState.PeopleLoaded -> {
                viewModel.people = state.people
                mastersViewModel.getPlaces()
            }
            is MastersListViewState.PlacesLoaded -> {
                viewModel.places = state.places
                mastersViewModel.getCategories()
            }
            is MastersListViewState.CategoriesLoaded -> {
                viewModel.categories = state.categories
                mastersViewModel.getDebts()
            }
            is MastersListViewState.DebtsLoaded -> {
                viewModel.debts = state.debts
                viewModel.getLocalMovements()
            }
            else -> Unit
        }
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

        //if is an action to add a movement, navigate directly
        if (movementToAdd != null) {
            navigateToDetails(movementToAdd)
            movementToAdd = null
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
        bundle.putSerializable(PEOPLE_DATA, viewModel.peopleActive as ArrayList<out Parcelable>)
        bundle.putSerializable(PLACES_DATA, viewModel.placesActive as ArrayList<out Parcelable>)
        bundle.putSerializable(CATEGORIES_DATA, viewModel.categoriesActive as ArrayList<out Parcelable>)
        bundle.putSerializable(DEBTS_DATA, viewModel.debtsActive as ArrayList<out Parcelable>)

        findNavController().navigate(R.id.action_movementsFragment_to_movementDetailFragment, bundle)
    }

    private val actionListener = object : MovementsAdapter.OnActionItemListener {

        override fun onMovementClicked(movement: MovementModel?) {
            navigateToDetails(movement)
        }

        override fun onDiscardMovementClicked(id: Int) {}

    }

}
