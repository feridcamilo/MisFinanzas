package com.android.misfinanzas.ui.masters

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.data.UserSesion
import com.android.data.local.model.*
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

    private lateinit var peopleObserver: Observer<Result<List<PersonVO>>>
    private lateinit var placesObserver: Observer<Result<List<PlaceVO>>>
    private lateinit var categoryObserver: Observer<Result<List<CategoryVO>>>
    private lateinit var debtsObserver: Observer<Result<List<DebtVO>>>

    private var people: List<PersonVO>? = null
    private var places: List<PlaceVO>? = null
    private var categories: List<CategoryVO>? = null
    private var debts: List<DebtVO>? = null

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
            navigateToMastersList(people?.map { MasterVO(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_places.setOnClickListener {
            navigateToMastersList(places?.map { MasterVO(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_categories.setOnClickListener {
            navigateToMastersList(categories?.map { MasterVO(it.id, it.name, it.enabled) }, (it as Button).text.toString())
        }

        btn_debts.setOnClickListener {
            navigateToMastersList(debts?.map { MasterVO(it.id, it.name, it.enabled) }, (it as Button).text.toString())
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

    private fun navigateToMastersList(masterList: List<MasterVO>?, title: String) {
        val bundle = Bundle()
        masterList?.let { bundle.putParcelableArrayList(MastersListFragment.MASTERS_DATA, it as ArrayList<out Parcelable>) }
        bundle.putString(MastersListFragment.LIST_TITLE, title)
        findNavController().navigate(R.id.action_mastersFragment_to_mastersListFragment, bundle)
    }
}
