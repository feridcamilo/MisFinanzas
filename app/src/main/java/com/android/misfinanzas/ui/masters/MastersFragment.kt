package com.android.misfinanzas.ui.masters

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.domain.UserSesion
import com.android.domain.model.*
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.ui.masters.mastersList.MastersListFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_masters.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MastersFragment : BaseFragment() {

    private val TAG = this.javaClass.name

    private lateinit var peopleObserver: Observer<Result<List<Person>>>
    private lateinit var placesObserver: Observer<Result<List<Place>>>
    private lateinit var categoryObserver: Observer<Result<List<Category>>>
    private lateinit var debtsObserver: Observer<Result<List<Debt>>>

    private var people: List<Person>? = null
    private var places: List<Place>? = null
    private var categories: List<Category>? = null
    private var debts: List<Debt>? = null

    private val viewModel by viewModel<MastersViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_masters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!UserSesion.hasUser()) {
            navigateToSync()
        } else {
            setupObservers()
            getLocalPeople()
            setupEvents()
        }
    }

    private fun setupEvents() {
        btn_people.setOnClickListener {
            navigateToMastersList(people?.map { Master(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_places.setOnClickListener {
            navigateToMastersList(places?.map { Master(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_categories.setOnClickListener {
            navigateToMastersList(categories?.map { Master(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_debts.setOnClickListener {
            navigateToMastersList(debts?.map { Master(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }
    }

    private fun setupObservers() {
        peopleObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    people = result.data
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
                    progressListener.hide()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_debts, result.exception), ErrorType.TYPE_ROOM)
            }
        }
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

    private fun navigateToSync() {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_MOVEMENTS, true)
        findNavController().navigate(R.id.action_mastersFragment_to_syncFragment, bundle)
    }

    private fun navigateToMastersList(masterList: List<Master>?, title: String) {
        val bundle = Bundle()
        masterList?.let { bundle.putParcelableArrayList(MastersListFragment.MASTERS_DATA, it as ArrayList<out Parcelable>) }
        bundle.putString(MastersListFragment.LIST_TITLE, title)
        findNavController().navigate(R.id.action_mastersFragment_to_mastersListFragment, bundle)
    }
}
