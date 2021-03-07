package com.android.misfinanzas.ui.logged.widgets.movementDetail

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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

    private lateinit var movementTypes: List<MovementType>
    private var descriptions: List<String>? = null
    private var people: List<MasterModel>? = null
    private var places: List<MasterModel>? = null
    private var categories: List<MasterModel>? = null
    private var debts: List<MasterModel>? = null

    private var movement: MovementModel? = null
    private var selectedMovementType: Int = MovementType.NOT_SELECTED
    var shouldDiscard = false
    var idToDiscard = 0

    fun initView(
        descriptions: List<String>?,
        people: List<MasterModel>?,
        places: List<MasterModel>?,
        categories: List<MasterModel>?,
        debts: List<MasterModel>?
    ) {
        this.descriptions = descriptions
        this.people = people
        this.places = places
        this.categories = categories
        this.debts = debts
        setMastersPickers()
        binding.etFechaMovimiento.setDateControl()
    }

    private fun setMastersPickers() = with(binding) {
        movementTypes = MovementType.getList(context)
        val movementTypeAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, movementTypes)
        movementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoMovimiento.adapter = movementTypeAdapter

        spTipoMovimiento.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val movementType: MovementType = parent.getItemAtPosition(pos) as MovementType
                selectedMovementType = movementType.id
                ivMovType.setImageResource(MovementType.getImage(selectedMovementType))
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        descriptions?.let {
            val descriptionsAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
            tvDescripcionValue.setAdapter(descriptionsAdapter)
        }

        people?.let {
            val peopleAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
            tvPersonaValue.setAdapter(peopleAdapter)
            tvPersonaValue.setOnClickListener { tvPersonaValue.showDropDown() }
        }

        places?.let {
            val placesAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
            tvLugarValue.setAdapter(placesAdapter)
            tvLugarValue.setOnClickListener { tvLugarValue.showDropDown() }
        }

        categories?.let {
            val categoriesAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
            tvCategoriaValue.setAdapter(categoriesAdapter)
            tvCategoriaValue.setOnClickListener { tvCategoriaValue.showDropDown() }
        }

        debts?.let {
            val debtsAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
            tvDeudaValue.setAdapter(debtsAdapter)
            tvDeudaValue.setOnClickListener { tvDeudaValue.showDropDown() }
        }

        val currentDate: Date = getCurrentDateTime()
        etFechaMovimiento.setText(getDateFormat().format(currentDate))
    }

    fun showMovement(movement: MovementModel?) {
        this.movement = movement

        if (movement == null || movement.idMovement <= 0) {
            binding.tvFechaIngreso.visibility = View.GONE
            binding.tvFechaIngresoValue.visibility = View.GONE
            binding.tvFechaUpd.visibility = View.GONE
            binding.tvFechaUpdValue.visibility = View.GONE
        }

        movement?.let {
            val notAssociated = context.getString(R.string.view_md_not_associated)
            val date = if (it.date == null) getDateFormat().format(getCurrentDateTime()) else getDateFormat().format(checkNotNull(it.date)).toString()
            val entryDate =
                if (it.dateEntry == null) notAssociated else getDateTimeFormat().format(checkNotNull(it.dateEntry)).toString()
            val lastUpdate =
                if (it.dateLastUpd == null) notAssociated else getDateTimeFormat().format(checkNotNull(it.dateLastUpd)).toString()

            binding.ivMovType.setImageResource(MovementType.getImage(it.idType))
            binding.spTipoMovimiento.setSelection(getMovementTypeIndex(it.idType))
            val value = MoneyUtils.getBigDecimalStringValue(it.value.toString())
            if (value != ZERO) {
                binding.etValor.setText(value)
            }
            binding.tvDescripcionValue.setText(it.description)
            binding.tvPersonaValue.setText(getPersonName(it.personId))
            binding.tvLugarValue.setText(getPlaceName(it.placeId))
            binding.tvCategoriaValue.setText(getCategoryName(it.categoryId))
            binding.etFechaMovimiento.setText(date)
            binding.tvDeudaValue.setText(getDebtName(it.debtId))
            binding.tvFechaIngresoValue.text = entryDate
            if (lastUpdate == notAssociated) {
                binding.tvFechaUpd.visibility = View.GONE
                binding.tvFechaUpdValue.visibility = View.GONE
            } else {
                binding.tvFechaUpdValue.text = lastUpdate
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
            throw Exception(context.getString(R.string.info_select_movement_type))
        }

        val debtId = getDebtId(tvDeudaValue.text.toString())
        if (selectedMovementType == MovementType.CREDIT_CARD_BUY && (debtId == null || debtId == 0)) {
            spTipoMovimiento.requestFocus()
            throw Exception(context.getString(R.string.info_select_debt_by_cc_buy))
        }

        val strValue = etValor.text.toString()
        if (strValue.isEmpty()) {
            etValor.requestFocus()
            throw Exception(context.getString(R.string.info_enter_value))
        }
        val value: BigDecimal
        try {
            value = BigDecimal(strValue)
        } catch (e: Exception) {
            etValor.requestFocus()
            throw Exception(context.getString(R.string.info_only_numbers))
        }

        val description = tvDescripcionValue.text.toString()
        val personId = getPersonId(tvPersonaValue.text.toString())
        val placeId = getPlaceId(tvLugarValue.text.toString())

        val categoryId = getCategoryId(tvCategoriaValue.text.toString())
        if ((categoryId ?: 0) == 0) {
            tvCategoriaValue.requestFocus()
            throw Exception(context.getString(R.string.info_select_category))
        }

        val strDate = etFechaMovimiento.text.toString()
        if (strDate.isEmpty()) {
            throw Exception(context.getString(R.string.info_select_date))
        }
        val date: Date
        try {
            date = getDateToWebService(getDateFormat().parse(strDate)!!)
        } catch (e: Exception) {
            etFechaMovimiento.requestFocus()
            throw Exception(context.getString(R.string.info_enter_valid_date))
        }

        val currentDateTime: Date = getCurrentDateTime()

        var dateLastUpdate: Date? = null
        var dateEntry: Date? = currentDateTime
        if (movement != null && movement?.idMovement!! > 0) {
            dateLastUpdate = currentDateTime
            dateEntry = movement?.dateEntry
        }

        var idMovement = movement?.idMovement ?: 0

        if (movement != null && movement?.idMovement!! <= 0) {
            shouldDiscard = true
            idToDiscard = movement?.idMovement!!
            idMovement = 0
        }

        return MovementModel(
            idMovement,//Autoincrement
            selectedMovementType,
            value,
            description,
            personId,
            null,
            placeId,
            null,
            categoryId!!,
            null,
            date,
            debtId,
            null,
            dateEntry,
            dateLastUpdate
        )
    }

    fun cleanFormAfterSave() = with(binding) {
        etValor.setText(EMPTY)
        etValor.requestFocus()
        tvPersonaValue.setText(EMPTY)
        tvDeudaValue.setText(EMPTY)
    }

    private fun getMovementType(id: Int): MovementType? {
        return movementTypes.find { id == it.id }
    }

    private fun getMovementTypeIndex(id: Int): Int {
        return movementTypes.indexOf(getMovementType(id))
    }

    private fun getPersonName(id: Int?): String? {
        return people?.find { id == it.id }?.name
    }

    private fun getPersonId(name: String): Int? {
        return people?.find { name == it.name }?.id
    }

    private fun getPlaceName(id: Int?): String? {
        return places?.find { id == it.id }?.name
    }

    private fun getPlaceId(name: String): Int? {
        return places?.find { name == it.name }?.id
    }

    private fun getCategoryName(id: Int?): String? {
        return categories?.find { id == it.id }?.name
    }

    private fun getCategoryId(name: String): Int? {
        return categories?.find { name == it.name }?.id
    }

    private fun getDebtName(id: Int?): String? {
        return debts?.find { id == it.id }?.name
    }

    private fun getDebtId(name: String): Int? {
        return debts?.find { name == it.name }?.id
    }
}
