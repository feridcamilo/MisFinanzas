package com.android.misfinanzas.ui.movementDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.widgets.movementDetail.MovementDetailView
import kotlinx.android.synthetic.main.fragment_movement_detail.*

class MovementDetailFragment : BaseFragment() {

    companion object {
        const val MOVEMENT_DATA: String = "Movement"
        const val DESCRIPTIONS_DATA: String = "Descriptions"
        const val PEOPLE_DATA: String = "People"
        const val PLACES_DATA: String = "Places"
        const val CATEGORIES_DATA: String = "Categories"
        const val DEBTS_DATA: String = "Debts"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModels<MovementDetailViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private lateinit var movementDetailView: MovementDetailView
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

        initMovementDetailView()

        ib_save.setOnClickListener {
            save()
        }

        ib_delete.setOnClickListener {
            delete()
        }
    }

    private fun save() {
        try {
            val movementToSave = movementDetailView.getMovement()
            viewModel.insertLocalMovement(movementToSave)
            if (movementToSave.dateLastUpd == null) { //Is insert
                movementDetailView.cleanFormAfterSave()
                Toast.makeText(requireContext(), getString(R.string.info_movement_saved), Toast.LENGTH_SHORT).show()
            } else { //Is update
                activity?.onBackPressed()
                Toast.makeText(requireContext(), getString(R.string.info_movement_updated), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun delete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cd_title_delete))
        builder.setMessage(getString(R.string.cd_desc_delete))

        builder.setPositiveButton(R.string.cd_yes) { dialog, which ->
            movement?.let {
                viewModel.deleteLocalMovement(it)
            }
            activity?.onBackPressed()
            Toast.makeText(requireContext(), R.string.info_movement_deleted, Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton(R.string.cd_no) { dialog, which -> }
        builder.show()
    }

    private fun initMovementDetailView() {
        movementDetailView = movement_detail_view
        movementDetailView.initView(descriptions, people, places, categories, debts)
        movementDetailView.showMovement(movement)
        if (movement == null) {
            ib_delete.visibility = View.GONE
        }
    }
}
