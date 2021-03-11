package com.android.misfinanzas.ui.logged.movements.movementDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMovementDetailBinding
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.sync.SyncType
import com.android.misfinanzas.ui.logged.config.ConfigViewModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewState
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MovementDetailFragment : Fragment(R.layout.fragment_movement_detail) {

    companion object {
        const val MOVEMENT_DATA: String = "Movement"
        const val DESCRIPTIONS_DATA: String = "Descriptions"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<MovementDetailViewModel>()
    private val mastersViewModel by viewModel<MastersListViewModel>()
    private val configViewModel by viewModel<ConfigViewModel>()

    private val binding by viewBinding<FragmentMovementDetailBinding>()

    private var movement: MovementModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(MOVEMENT_DATA)) {
                movement = it.getSerializable(MOVEMENT_DATA) as? MovementModel
            }
            viewModel.descriptions = it.getStringArrayList(DESCRIPTIONS_DATA) ?: emptyList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupEvents()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        mastersViewModel.viewState.observe(viewLifecycleOwner, mastersViewStateObserver)
        showLoader()
        if (viewModel.descriptions.isEmpty()) {
            viewModel.getMovementsDescriptions()
        } else {
            mastersViewModel.getPeople()
        }
    }

    private val viewStateObserver = Observer<MovementDetailViewState> { state ->
        hideLoader()
        when (state) {
            is MovementDetailViewState.DescriptionsLoaded -> mastersViewModel.getPeople()
        }
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
                hideLoader()
                initMovementDetailView()
            }
            else -> Unit
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
            setToolbarTitle(R.string.title_movements_details_new)
            ibDelete.visibility = View.GONE
        }

        movementDetailView.initView(
            viewModel.descriptions,
            viewModel.peopleActive,
            viewModel.placesActive,
            viewModel.categoriesActive,
            viewModel.debtsActive
        )
        movementDetailView.showMovement(movement)
    }

    private fun save() = with(binding) {
        try {
            val movementToSave = movementDetailView.getMovement()
            viewModel.insertLocalMovement(movementToSave)
            if (movementDetailView.shouldDiscard) {
                viewModel.insertDiscardedMovement(movementDetailView.idToDiscard)
            }

            syncWithWeb()

            if (movementToSave.dateLastUpd == null) { //Is insert
                movementDetailView.cleanFormAfterSave()
                context?.showShortToast(R.string.info_movement_saved)
            } else { //Is update
                context?.showShortToast(R.string.info_movement_updated)
                activity?.onBackPressed()
            }
        } catch (e: Exception) {
            hideLoader()
            context?.showExceptionMessage(TAG, e.message!!, ErrorType.TYPE_APP)
        }
    }

    private fun syncWithWeb() {
        if (configViewModel.isAutoSyncOnEdit()) {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync_detail)) == true) {
                backgroundSync(SyncType.SYNC_MOVEMENTS)
            }
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
            syncWithWeb()
            context?.showShortToast(R.string.info_movement_deleted)
            activity?.onBackPressed()
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

}
