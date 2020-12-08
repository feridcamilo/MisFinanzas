package com.android.misfinanzas.ui.logged.movements

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.local.model.converters.MovementConverter
import com.android.domain.AppConfig.Companion.MAX_MOVEMENTS_SIZE
import com.android.domain.AppConfig.Companion.MIN_LENGTH_SEARCH
import com.android.domain.model.*
import com.android.domain.result.Result
import com.android.domain.utils.DateUtils
import com.android.domain.utils.MoneyUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.MovementClickListener
import com.android.misfinanzas.databinding.FragmentMovementsBinding
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.CATEGORIES_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DEBTS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.DESCRIPTIONS_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.MOVEMENT_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PEOPLE_DATA
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment.Companion.PLACES_DATA
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MovementsFragment : BaseFragment(), MovementClickListener {

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<MovementsViewModel>()
    private lateinit var binding: FragmentMovementsBinding

    private lateinit var movementsObserver: Observer<Result<List<Movement>>>
    private lateinit var peopleObserver: Observer<Result<List<Person>>>
    private lateinit var placesObserver: Observer<Result<List<Place>>>
    private lateinit var categoryObserver: Observer<Result<List<Category>>>
    private lateinit var debtsObserver: Observer<Result<List<Debt>>>

    private var descriptions: List<String>? = null
    private var movements: List<Movement>? = null
    private var movementToAdd: Movement? = null

    private var people: List<Person>? = null
    private var peopleActive: List<Person>? = null
    private var places: List<Place>? = null
    private var placesActive: List<Place>? = null
    private var categories: List<Category>? = null
    private var categoriesActive: List<Category>? = null
    private var debts: List<Debt>? = null
    private var debtsActive: List<Debt>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(MOVEMENT_DATA)) {
                movementToAdd = MovementConverter().stringToMovement(it.getString(MOVEMENT_DATA, EMPTY))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupObservers()
        getLocalPeople()

        btnFiltros.setOnClickListener {
            findNavController().navigate(R.id.filtersFragment)
        }

        btnAddMovement.setOnClickListener {
            navigateToDetails(null)
        }
    }


    override fun onResume() {
        super.onResume()
        binding.rvMovimientos.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() = with(binding.rvMovimientos) {
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<Movement>) {
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
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    people = result.data
                    peopleActive = people?.filter { it.enabled }
                    getLocalPlaces()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_people, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        placesObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    places = result.data
                    placesActive = places?.filter { it.enabled }
                    getLocalCategories()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_places, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        categoryObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    categories = result.data
                    categoriesActive = categories?.filter { it.enabled }
                    getLocalDebts()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_categories, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        debtsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    debts = result.data
                    debtsActive = debts?.filter { it.enabled }
                    getLocalMovements()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_debts, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        movementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        context?.showShortToast(R.string.info_no_movements)
                    } else {
                        movements = result.data
                        descriptions = result.data.distinctBy { it.description }.map { it.description }
                        if (binding.svSearch.query.isNotEmpty()) {
                            binding.svSearch.setQuery(binding.svSearch.query, true)
                        } else {
                            setupRecyclerViewData(movements!!)
                        }

                        //if is an action to add a movement, navigate directly
                        if (movementToAdd != null) {
                            navigateToDetails(movementToAdd)
                            movementToAdd = null
                            progressListener.hide()
                        }
                    }
                    progressListener.hide()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_movements, result.exception), ErrorType.TYPE_ROOM)
            }
        }
    }

    private fun navigateToDetails(movement: Movement?) {
        val bundle = Bundle()
        movement?.let { bundle.putString(MOVEMENT_DATA, MovementConverter().movementToString(it)) }
        descriptions?.let { bundle.putStringArrayList(DESCRIPTIONS_DATA, it as ArrayList<String>) }
        peopleActive?.let { bundle.putParcelableArrayList(PEOPLE_DATA, it as ArrayList<out Parcelable>) }
        placesActive?.let { bundle.putParcelableArrayList(PLACES_DATA, it as ArrayList<out Parcelable>) }
        categoriesActive?.let { bundle.putParcelableArrayList(CATEGORIES_DATA, it as ArrayList<out Parcelable>) }
        debtsActive?.let { bundle.putParcelableArrayList(DEBTS_DATA, it as ArrayList<out Parcelable>) }
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

    override fun onMovementClicked(movement: Movement?) {
        navigateToDetails(movement)
    }

    override fun onDiscardMovementClicked(id: Int, position: Int) {
        TODO("Not yet implemented")
    }

}
