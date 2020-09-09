package com.android.misfinanzas.ui.movements

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.UserSesion
import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.data.utils.DateUtils
import com.android.data.utils.MoneyUtils
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_movements.*
import java.util.*

class MovementsFragment : BaseFragment(), MovementsAdapter.OnMovementClickListener {

    private val TAG = this.javaClass.name

    private val viewModel by viewModels<MovementsViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private val maxMovementSize: Int = 1000
    private val minLengthToSearch: Int = 3
    private lateinit var movementsObserver: Observer<Result<List<MovementVO>>>
    private lateinit var peopleObserver: Observer<Result<List<PersonVO>>>
    private lateinit var placesObserver: Observer<Result<List<PlaceVO>>>
    private lateinit var categoryObserver: Observer<Result<List<CategoryVO>>>
    private lateinit var debtsObserver: Observer<Result<List<DebtVO>>>

    private var descriptions: List<String>? = null
    private var movements: List<MovementVO>? = null

    private var people: List<PersonVO>? = null
    private var peopleActive: List<PersonVO>? = null
    private var places: List<PlaceVO>? = null
    private var placesActive: List<PlaceVO>? = null
    private var categories: List<CategoryVO>? = null
    private var categoriesActive: List<CategoryVO>? = null
    private var debts: List<DebtVO>? = null
    private var debtsActive: List<DebtVO>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!UserSesion.hasUser()) {
            navigateToSync()
        } else {
            setupRecyclerView()
            setupSearchView()
            setupObservers()
            getLocalPeople()

            btn_filtros.setOnClickListener {
                findNavController().navigate(R.id.filtersFragment)
            }

            btn_add_movement.setOnClickListener {
                navigateToDetails(null)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rv_movimientos.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        rv_movimientos.layoutManager = LinearLayoutManager(requireContext())
        rv_movimientos.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementVO>) {
        rv_movimientos.adapter = MovementsAdapter(requireContext(), movements, this)
    }

    private fun setupSearchView() {
        sv_search.setOnClickListener { sv_search.isIconified = false }
        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you can to change when press search button
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                if (movements?.size!! > maxMovementSize && newText?.length!! != 0 && newText?.length!! <= minLengthToSearch) {
                    //if the list is big and if text length has less than minLengthToSearch it not search in every letter written
                    return false
                } else {
                    filter(newText)
                }
                return false
            }
        })
    }

    private fun filter(text: String?) {
        progressListener.show(false)
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
        progressListener.hide()
    }

    private fun setupObservers() {
        peopleObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    people = result.data
                    peopleActive = people?.filter { it.enabled }
                    getLocalPlaces()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_people, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        placesObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    places = result.data
                    placesActive = places?.filter { it.enabled }
                    getLocalCategories()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_places, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        categoryObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    categories = result.data
                    categoriesActive = categories?.filter { it.enabled }
                    getLocalDebts()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_categories, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        debtsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    debts = result.data
                    debtsActive = debts?.filter { it.enabled }
                    getLocalMovements()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_debts, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        movementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.info_no_movements, Toast.LENGTH_SHORT).show()
                    } else {
                        movements = result.data
                        descriptions = result.data.distinctBy { it.description }.map { it.description }
                        if (sv_search.query.isNotEmpty()) {
                            sv_search.setQuery(sv_search.query, true)
                        } else {
                            setupRecyclerViewData(movements!!)
                        }
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }
    }

    private fun navigateToSync() {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_MOVEMENTS, true)
        findNavController().navigate(R.id.action_movementsFragment_to_syncFragment, bundle)
    }

    private fun navigateToDetails(movement: MovementVO?) {
        val bundle = Bundle()
        bundle.putParcelable(MovementDetailFragment.MOVEMENT_DATA, movement)
        descriptions?.let { bundle.putStringArrayList(MovementDetailFragment.DESCRIPTIONS_DATA, it as ArrayList<String>) }
        peopleActive?.let { bundle.putParcelableArrayList(MovementDetailFragment.PEOPLE_DATA, it as ArrayList<out Parcelable>) }
        placesActive?.let { bundle.putParcelableArrayList(MovementDetailFragment.PLACES_DATA, it as ArrayList<out Parcelable>) }
        categoriesActive?.let { bundle.putParcelableArrayList(MovementDetailFragment.CATEGORIES_DATA, it as ArrayList<out Parcelable>) }
        debtsActive?.let { bundle.putParcelableArrayList(MovementDetailFragment.DEBTS_DATA, it as ArrayList<out Parcelable>) }
        findNavController().navigate(R.id.action_movementsFragment_to_movementDetailFragment, bundle)
    }

    private fun getLocalMovements() {
        viewModel.getLocalMovements().observe(viewLifecycleOwner, movementsObserver)
    }

    private fun getLocalPeople() {
        viewModel.getLocalPeople().observe(viewLifecycleOwner, peopleObserver)
    }

    private fun getLocalPlaces() {
        viewModel.getLocalPlaces().observe(viewLifecycleOwner, placesObserver)
    }

    private fun getLocalCategories() {
        viewModel.getLocalCategories().observe(viewLifecycleOwner, categoryObserver)
    }

    private fun getLocalDebts() {
        viewModel.getLocalDebts().observe(viewLifecycleOwner, debtsObserver)
    }

    override fun onMovementClicked(movement: MovementVO) {
        navigateToDetails(movement)
    }
}
