package com.android.misfinanzas.ui.movements.movementDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.data.local.model.converters.MovementConverter
import com.android.domain.model.*
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.databinding.FragmentMovementDetailBinding
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel

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

    private val viewModel by viewModel<MovementDetailViewModel>()
    private lateinit var binding: FragmentMovementDetailBinding

    private var movement: Movement? = null
    private var descriptions: List<String>? = null
    private var people: List<Person>? = null
    private var places: List<Place>? = null
    private var categories: List<Category>? = null
    private var debts: List<Debt>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(MOVEMENT_DATA)) {
                movement = MovementConverter().stringToMovement(it.getString(MOVEMENT_DATA, EMPTY))
            }
            descriptions = it.getStringArrayList(DESCRIPTIONS_DATA)
            people = it.getParcelableArrayList(PEOPLE_DATA)
            places = it.getParcelableArrayList(PLACES_DATA)
            categories = it.getParcelableArrayList(CATEGORIES_DATA)
            debts = it.getParcelableArrayList(DEBTS_DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovementDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMovementDetailView()
        setupEvents()
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
                activity?.onBackPressed()
                context?.showShortToast(R.string.info_movement_updated)
            }
        } catch (e: Exception) {
            showExceptionMessage(TAG, e.message!!, ErrorType.TYPE_APP)
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
            activity?.onBackPressed()
            context?.showShortToast(R.string.info_movement_deleted)
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

}
