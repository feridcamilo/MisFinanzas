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
import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.UserSesion
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_movements.*

class MovementsFragment : BaseFragment(), MovementsAdapter.OnMovementClickListener {

    private val TAG = this.javaClass.name

    private val viewModel by viewModels<MovementsViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private lateinit var movementsObserver: Observer<Result<List<MovementVO>>>
    private lateinit var peopleObserver: Observer<Result<List<PersonVO>>>
    private lateinit var placesObserver: Observer<Result<List<PlaceVO>>>
    private lateinit var categoryObserver: Observer<Result<List<CategoryVO>>>
    private lateinit var debtsObserver: Observer<Result<List<DebtVO>>>

    private var descriptions: List<String>? = null
    private var people: List<PersonVO>? = null
    private var places: List<PlaceVO>? = null
    private var categories: List<CategoryVO>? = null
    private var debts: List<DebtVO>? = null

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

    private fun setupSearchView() {
        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you can to change in every letter written
                return false
            }
        })
    }

    private fun setupObservers() {
        peopleObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    people = result.data
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
                        descriptions = result.data.distinctBy { it.description }.map { it.description }
                        rv_movimientos.adapter = MovementsAdapter(requireContext(), result.data, this)
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
        bundle.putStringArrayList(MovementDetailFragment.DESCRIPTIONS_DATA, descriptions as ArrayList<String>)
        bundle.putParcelableArrayList(MovementDetailFragment.PEOPLE_DATA, people as ArrayList<out Parcelable>)
        bundle.putParcelableArrayList(MovementDetailFragment.PLACES_DATA, places as ArrayList<out Parcelable>)
        bundle.putParcelableArrayList(MovementDetailFragment.CATEGORIES_DATA, categories as ArrayList<out Parcelable>)
        bundle.putParcelableArrayList(MovementDetailFragment.DEBTS_DATA, debts as ArrayList<out Parcelable>)
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
