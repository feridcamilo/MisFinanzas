package com.android.misfinanzas.ui.movementDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_movement_detail.*

class MovementDetailFragment : BaseFragment() {

    companion object {
        val MOVEMENT_DATA: String = "Movement"
        val DESCRIPTIONS_DATA: String = "Descriptions"
        val PEOPLE_DATA: String = "People"
        val PLACES_DATA: String = "Places"
        val CATEGORIES_DATA: String = "Categories"
        val DEBTS_DATA: String = "Debts"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModels<MovementDetailViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private var movement: MovementVO? = null
    private var descriptions: List<String>? = null
    private var people: List<PersonVO>? = null
    private var places: List<PlaceVO>? = null
    private var categories: List<CategoryVO>? = null
    private var debts: List<DebtVO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movement = it.getParcelable(MOVEMENT_DATA)
            descriptions = it.getStringArrayList(DESCRIPTIONS_DATA)
            people = it.getParcelableArrayList(PEOPLE_DATA)
            places = it.getParcelableArrayList(PLACES_DATA)
            categories = it.getParcelableArrayList(CATEGORIES_DATA)
            debts = it.getParcelableArrayList(DEBTS_DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movement_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMovement()
    }

    private fun showMovement() {
        val movementDetailView = movement_detail_view
        movementDetailView.initView(descriptions, people, places, categories, debts)
        if (movement != null) {
            movementDetailView.showMovement(movement!!)
        }
    }
}
