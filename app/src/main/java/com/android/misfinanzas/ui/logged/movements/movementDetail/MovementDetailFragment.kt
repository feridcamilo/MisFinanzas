package com.android.misfinanzas.ui.logged.movements.movementDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMovementDetailBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.ui.logged.config.ConfigViewModel
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MovementDetailFragment : Fragment(R.layout.fragment_movement_detail) {

    companion object {
        const val MOVEMENT_DATA: String = "Movement"
        const val DESCRIPTIONS_DATA: String = "Descriptions"
        const val PEOPLE_DATA: String = "People"
        const val PLACES_DATA: String = "Places"
        const val CATEGORIES_DATA: String = "Categories"
        const val DEBTS_DATA: String = "Debts"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<MovementDetailViewModel>()
    private val configViewModel by viewModel<ConfigViewModel>()
    private val binding by viewBinding<FragmentMovementDetailBinding>()

    private var movement: MovementModel? = null
    private var descriptions: List<String>? = null
    private var people: List<MasterModel>? = null
    private var places: List<MasterModel>? = null
    private var categories: List<MasterModel>? = null
    private var debts: List<MasterModel>? = null

    private var shouldBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(MOVEMENT_DATA)) {
                movement = it.getSerializable(MOVEMENT_DATA) as? MovementModel
            }
            descriptions = it.getStringArrayList(DESCRIPTIONS_DATA)
            people = it.getParcelableArrayList(PEOPLE_DATA)
            places = it.getParcelableArrayList(PLACES_DATA)
            categories = it.getParcelableArrayList(CATEGORIES_DATA)
            debts = it.getParcelableArrayList(DEBTS_DATA)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        initMovementDetailView()
        setupEvents()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private val viewStateObserver = Observer<MovementDetailViewState> { state ->
        hideLoader()
        when (state) {
            is MovementDetailViewState.SynchronizedData -> syncMovementsResult()
        }
    }

    private fun setupEvents() = with(binding) {
        ibSave.setOnClickListener {
            save()
        }

        ibDelete.setOnClickListener {
            delete()
        }
    }

    private fun initMovementDetailView() = with(binding) {
        val isNewMovement = movement == null || movement?.idMovement!! <= 0

        if (isNewMovement) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.title_movements_details_new)
            ibDelete.visibility = View.GONE
        }

        movementDetailView.initView(descriptions, people, places, categories, debts)
        movementDetailView.showMovement(movement)
    }

    private fun save() = with(binding) {
        try {
            val movementToSave = movementDetailView.getMovement()
            viewModel.insertLocalMovement(movementToSave)
            if (movementDetailView.shouldDiscard) {
                viewModel.insertDiscardedMovement(movementDetailView.idToDiscard)
            }
            if (movementToSave.dateLastUpd == null) { //Is insert
                movementDetailView.cleanFormAfterSave()
                context?.showShortToast(R.string.info_movement_saved)
            } else { //Is update
                shouldBack = true
                context?.showShortToast(R.string.info_movement_updated)
            }

            syncWithWeb()
        } catch (e: Exception) {
            hideLoader()
            context?.showExceptionMessage(TAG, e.message!!, ErrorType.TYPE_APP)
        }
    }

    private fun syncWithWeb() {
        var hasRights = false
        if (configViewModel.isAutoSyncOnEdit()) {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync_detail)) == true) {
                hasRights = true
            }
        }

        if (hasRights) {
            showLoader()
            viewModel.sync()
        } else {
            syncMovementsResult()
        }
    }

    private fun syncMovementsResult() {
        if (shouldBack) {
            activity?.onBackPressed()
        }
    }

    private fun delete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cd_title_delete))
        builder.setMessage(getString(R.string.cd_desc_delete))

        builder.setPositiveButton(R.string.cd_yes) { _, _ ->
            movement?.let {
                viewModel.deleteLocalMovement(it)
            }
            shouldBack = true
            context?.showShortToast(R.string.info_movement_deleted)
            syncWithWeb()
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

}
