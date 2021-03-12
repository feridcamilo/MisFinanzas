package com.android.misfinanzas.ui.logged.widgets.movementDetail

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.android.domain.utils.DateUtils.Companion.getCurrentDateTime
import com.android.domain.utils.DateUtils.Companion.getDateFormat
import com.android.domain.utils.DateUtils.Companion.getDateTimeFormat
import com.android.domain.utils.DateUtils.Companion.getDateToWebService
import com.android.domain.utils.MoneyUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.domain.utils.StringUtils.Companion.ZERO
import com.android.misfinanzas.R
import com.android.misfinanzas.base.MovementType
import com.android.misfinanzas.databinding.CardViewMovementDetailBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.utils.gone
import com.android.misfinanzas.utils.setDateControl
import com.google.android.material.card.MaterialCardView
import java.math.BigDecimal
import java.util.*

class MovementDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    lateinit var binding: CardViewMovementDetailBinding

    init {
        inflate()
    }

    private fun inflate() {
        binding = CardViewMovementDetailBinding.inflate(LayoutInflater.from(context), this)
        setBackgroundColor(Color.TRANSPARENT)
    }

    private var movementTypes: List<MovementType> = emptyList()
    private var descriptions: List<String> = emptyList()
    private var people: List<MasterModel> = emptyList()
    private var places: List<MasterModel> = emptyList()
    private var categories: List<MasterModel> = emptyList()
    private var debts: List<MasterModel> = emptyList()

    private var movement: MovementModel? = null
    private var movementId: Int = 0
    private var selectedMovementType: Int = MovementType.NOT_SELECTED
    var shouldDiscard = false
    var idToDiscard = 0

    fun initView(
        movement: MovementModel?,
        descriptions: List<String>,
        people: List<MasterModel>,
        places: List<MasterModel>,
        categories: List<MasterModel>,
        debts: List<MasterModel>
    ) {
        this.movement = movement
        this.movementTypes = MovementType.getList(context)
        this.descriptions = descriptions
        this.people = people
        this.places = places
        this.categories = categories
        this.debts = debts
        movementId = movement?.idMovement ?: 0
        setMastersPickers()
        setupView()

        binding.etMovDate.setDateControl()
    }

    private fun setMastersPickers() = with(binding) {
        ivMovType.setImageResource(MovementType.getImage(selectedMovementType))
        ivMovType.setOnClickListener { etMovType.showDropDown() }

        val movementTypeAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, movementTypes)
        etMovType.setAdapter(movementTypeAdapter)
        etMovType.setOnClickListener { etMovType.showDropDown() }
        etMovType.setOnItemClickListener { _, _, position, _ ->
            val item = movementTypeAdapter.getItem(position)
            if (item != null) {
                selectedMovementType = item.id
                ivMovType.setImageResource(MovementType.getImage(selectedMovementType))
            }
        }

        val descriptionsAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, descriptions)
        etDescription.setAdapter(descriptionsAdapter)
        tilDescription.setEndIconOnClickListener { etDescription.setText(EMPTY) }

        tilValue.setEndIconOnClickListener { etValue.setText(EMPTY) }

        //TODO cerrar el teclado onitemselected en maestros (falta crear el método)

        val peopleAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, people)
        etPerson.setAdapter(peopleAdapter)
        etPerson.setOnClickListener { etPerson.showDropDown() }
        tilPerson.setEndIconOnClickListener { etPerson.showDropDown() }

        val placesAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, places)
        etPlace.setAdapter(placesAdapter)
        etPlace.setOnClickListener { etPlace.showDropDown() }
        tilPlace.setEndIconOnClickListener { etPlace.showDropDown() }

        val categoriesAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, categories)
        etCategory.setAdapter(categoriesAdapter)
        etCategory.setOnClickListener { etCategory.showDropDown() }
        tilCategory.setEndIconOnClickListener { etCategory.showDropDown() }

        val debtsAdapter = ArrayAdapter(context, R.layout.layout_dropdown_item, debts)
        etDebt.setAdapter(debtsAdapter)
        etDebt.setOnClickListener { etDebt.showDropDown() }
        tilDebt.setEndIconOnClickListener { etDebt.showDropDown() }

        val currentDate: Date = getCurrentDateTime()
        etMovDate.setText(getDateFormat().format(currentDate))
    }

    private fun setupView() = with(binding) {
        if (movementId <= 0) {
            tvFechaIngreso.gone()
            tvFechaIngresoValue.gone()
            tvFechaUpd.gone()
            tvFechaUpdValue.gone()
        }
        loadMovement()
    }

    private fun loadMovement() = with(binding) {
        movement?.let {
            val notAssociated = context.getString(R.string.view_md_not_associated_female)

            var movDate = getDateFormat().format(getCurrentDateTime())
            it.date?.let { date ->
                movDate = getDateFormat().format(date)
            }

            var entryDate = notAssociated
            it.dateEntry?.let { date ->
                entryDate = getDateTimeFormat().format(date)
            }

            var lastUpdate = notAssociated
            it.dateLastUpd?.let { date ->
                lastUpdate = getDateTimeFormat().format(date)
            }

            ivMovType.setImageResource(MovementType.getImage(it.idType))
            etMovType.setText(getMovementType(it.idType)?.name.orEmpty(), false)
            selectedMovementType = it.idType
            val value = MoneyUtils.getBigDecimalStringValue(it.value.toString())
            if (value != ZERO) {
                etValue.setText(value)
            }
            etDescription.setText(it.description, false)
            etPerson.setText(getPersonName(it.personId), false)
            etPlace.setText(getPlaceName(it.placeId), false)
            etCategory.setText(getCategoryName(it.categoryId), false)
            etMovDate.setText(movDate)
            etDebt.setText(getDebtName(it.debtId), false)
            tvFechaIngresoValue.text = entryDate
            if (lastUpdate == notAssociated) {
                tvFechaUpd.visibility = View.GONE
                tvFechaUpdValue.visibility = View.GONE
            } else {
                tvFechaUpdValue.text = lastUpdate
            }
        }
    }

    @Throws(Exception::class)
    fun getMovement(): MovementModel {
        return validateForm()
    }

    @Throws(Exception::class)
    private fun validateForm(): MovementModel = with(binding) {
        if (selectedMovementType == MovementType.NOT_SELECTED) {
            etMovType.requestFocus()
            throw Exception(context.getString(R.string.info_select_movement_type))
        }

        val debtId = getDebtId(etDebt.text.toString())
        if (selectedMovementType == MovementType.CREDIT_CARD_BUY && (debtId == null || debtId == 0)) {
            etMovType.requestFocus()
            throw Exception(context.getString(R.string.info_select_debt_by_cc_buy))
        }

        val strValue = etValue.text.toString()
        if (strValue.isEmpty()) {
            etValue.requestFocus()
            throw Exception(context.getString(R.string.info_enter_value))
        }
        val value: BigDecimal
        try {
            value = BigDecimal(strValue)
        } catch (e: Exception) {
            etValue.requestFocus()
            throw Exception(context.getString(R.string.info_only_numbers))
        }

        val description = etDescription.text.toString().trim()
        val personId = getPersonId(etPerson.text.toString().trim())
        val placeId = getPlaceId(etPlace.text.toString().trim())

        val categoryId = getCategoryId(etCategory.text.toString().trim())
        if ((categoryId ?: 0) == 0) {
            etCategory.requestFocus()
            throw Exception(context.getString(R.string.info_select_category))
        }

        val strDate = etMovDate.text.toString().trim()
        if (strDate.isEmpty()) {
            throw Exception(context.getString(R.string.info_select_date))
        }
        val moveDate: Date
        try {
            moveDate = getDateToWebService(getDateFormat().parse(strDate)!!)
        } catch (e: Exception) {
            etMovDate.requestFocus()
            throw Exception(context.getString(R.string.info_enter_valid_date))
        }

        val currentDateTime: Date = getCurrentDateTime()

        var dateLastUpdate: Date? = null
        var dateEntry: Date = currentDateTime
        if (movementId > 0) {
            dateLastUpdate = currentDateTime
            dateEntry = movement?.dateEntry ?: currentDateTime
        }

        if (movementId < 0) {
            shouldDiscard = true
            idToDiscard = movementId
            movementId = 0
        }

        return MovementModel(
            movementId,
            selectedMovementType,
            value,
            description,
            personId,
            null,
            placeId,
            null,
            categoryId!!,
            null,
            moveDate,
            debtId,
            null,
            dateEntry,
            dateLastUpdate
        )
    }

    fun cleanFormAfterSave() = with(binding) {
        etValue.setText(EMPTY)
        etValue.requestFocus()
        etPerson.setText(EMPTY)
        etDebt.setText(EMPTY)
    }

    private fun getMovementType(id: Int): MovementType? {
        return movementTypes.find { id == it.id }
    }

    private fun getPersonName(id: Int?): String? {
        return people.find { id == it.id }?.name
    }

    private fun getPersonId(name: String): Int? {
        return people.find { name == it.name }?.id
    }

    private fun getPlaceName(id: Int?): String? {
        return places.find { id == it.id }?.name
    }

    private fun getPlaceId(name: String): Int? {
        return places.find { name == it.name }?.id
    }

    private fun getCategoryName(id: Int?): String? {
        return categories.find { id == it.id }?.name
    }

    private fun getCategoryId(name: String): Int? {
        return categories.find { name == it.name }?.id
    }

    private fun getDebtName(id: Int?): String? {
        return debts.find { id == it.id }?.name
    }

    private fun getDebtId(name: String): Int? {
        return debts.find { name == it.name }?.id
    }

}
