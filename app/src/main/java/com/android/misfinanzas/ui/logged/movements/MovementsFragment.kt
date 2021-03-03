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
import com.android.domain.AppConfig.Companion.MAX_MOVEMENTS_SIZE
import com.android.domain.AppConfig.Companion.MIN_LENGTH_SEARCH
import com.android.domain.utils.DateUtils
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.MovementClickListener
import com.android.misfinanzas.databinding.FragmentMovementsBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewState
import com.android.misfinanzas.ui.logged.movements.adapter.MovementsAdapter
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.CATEGORIES_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DEBTS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DESCRIPTIONS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.MOVEMENT_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PEOPLE_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PLACES_DATA
import com.android.misfinanzas.utils.hideLoader
import com.android.misfinanzas.utils.showLoader
import com.android.misfinanzas.utils.showShortToast
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MovementsFragment : Fragment(R.layout.fragment_movements), MovementClickListener {

    private val viewModel by viewModel<MovementsViewModel>()
    private val mastersViewModel by viewModel<MastersListViewModel>()
    private val binding by viewBinding<FragmentMovementsBinding>()

    private var movements: List<MovementModel>? = null
    private var descriptions: List<String>? = null

    private var people: List<MasterModel>? = null
    private var peopleActive: List<MasterModel>? = null
    private var places: List<MasterModel>? = null
    private var placesActive: List<MasterModel>? = null
    private var categories: List<MasterModel>? = null
    private var categoriesActive: List<MasterModel>? = null
    private var debts: List<MasterModel>? = null
    private var debtsActive: List<MasterModel>? = null

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
    }

    override fun onResume() {
        super.onResume()
        binding.rvMovimientos.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() = with(binding.rvMovimientos) {
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementModel>) {
        binding.rvMovimientos.adapter = MovementsAdapter(requireContext(), movements, this)
    }

    private fun setupSearchView() {
        binding.svSearch.setOnClickListener { binding.svSearch.isIconified = false }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you can to change when press search button
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                if (movements != null) {
                    if (movements?.size!! > MAX_MOVEMENTS_SIZE && newText?.length!! != 0 && newText.length <= MIN_LENGTH_SEARCH) {
                        //if the list is big and if text length has less than minLengthToSearch it not search in every letter written
                        return false
                    } else {
                        filter(newText)
                    }
                }
                return false
            }
        })
    }

    private fun filter(text: String?) {
        showLoader()
        if (!text.isNullOrEmpty()) {
            val textToCompare = text.toLowerCase(Locale.ROOT)
            val valueToCompare = MoneyUtils.getBigDecimalStringValue(text)
            val personToCompare = people?.firstOrNull { it.name.toLowerCase(Locale.ROOT).contains(textToCompare) }
            val placeToCompare = places?.firstOrNull { it.name.toLowerCase(Locale.ROOT).contains(textToCompare) }
            val categoryToCompare = categories?.firstOrNull { it.name.toLowerCase(Locale.ROOT).contains(textToCompare) }
            val debtToCompare = debts?.firstOrNull { it.name.toLowerCase(Locale.ROOT).contains(textToCompare) }

            val movementsFiltered = movements?.filter {
                it.description.toLowerCase(Locale.ROOT).contains(textToCompare) ||
                        (valueToCompare.isNotEmpty() && it.value.toString().contains(valueToCompare)) ||
                        DateUtils.getDateFormat().format(it.date!!).toString().contains(textToCompare) ||
                        (personToCompare != null && it.personId == personToCompare.id) ||
                        (placeToCompare != null && it.placeId == placeToCompare.id) ||
                        (categoryToCompare != null && it.categoryId == categoryToCompare.id) ||
                        (debtToCompare != null && it.debtId == debtToCompare.id)
            }
            setupRecyclerViewData(movementsFiltered!!)
        } else {
            setupRecyclerViewData(movements!!)
        }
        hideLoader()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        mastersViewModel.viewState.observe(viewLifecycleOwner, mastersViewStateObserver)

        showLoader()
        mastersViewModel.getPeople()
    }

    private val mastersViewStateObserver = Observer<MastersListViewState> { state ->
        when (state) {
            is MastersListViewState.PeopleLoaded -> {
                people = state.people
                peopleActive = people?.filter { it.enabled }
                mastersViewModel.getPlaces()
            }
            is MastersListViewState.PlacesLoaded -> {
                places = state.places
                placesActive = places?.filter { it.enabled }
                mastersViewModel.getCategories()
            }
            is MastersListViewState.CategoriesLoaded -> {
                categories = state.categories
                categoriesActive = categories?.filter { it.enabled }
                mastersViewModel.getDebts()
            }
            is MastersListViewState.DebtsLoaded -> {
                debts = state.debts
                debtsActive = debts?.filter { it.enabled }
                viewModel.getLocalMovements()
            }
        }
    }

    private val viewStateObserver = Observer<MovementsViewState> { state ->
        when (state) {
            is MovementsViewState.MovementsLoaded -> {
                movements = state.movements
                descriptions = movements!!.distinctBy { it.description }.map { it.description }
                loadMovements()
                hideLoader()
            }
        }
    }

    private fun loadMovements() = with(binding) {
        if (movements!!.isEmpty()) {
            context?.showShortToast(R.string.info_no_movements)
        } else {
            if (svSearch.query.isNotEmpty()) {
                svSearch.setQuery(svSearch.query, true)
            } else {
                setupRecyclerViewData(movements!!)
            }
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
        descriptions?.let { bundle.putStringArrayList(DESCRIPTIONS_DATA, it as ArrayList<String>) }
        peopleActive?.let { bundle.putSerializable(PEOPLE_DATA, it as ArrayList<out Parcelable>) }
        placesActive?.let { bundle.putParcelableArrayList(PLACES_DATA, it as ArrayList<out Parcelable>) }
        categoriesActive?.let { bundle.putParcelableArrayList(CATEGORIES_DATA, it as ArrayList<out Parcelable>) }
        debtsActive?.let { bundle.putParcelableArrayList(DEBTS_DATA, it as ArrayList<out Parcelable>) }
        findNavController().navigate(R.id.action_movementsFragment_to_movementDetailFragment, bundle)
    }

    override fun onMovementClicked(movement: MovementModel?) {
        navigateToDetails(movement)
    }

    override fun onDiscardMovementClicked(id: Int) {
        TODO("Not yet implemented")
    }

}
