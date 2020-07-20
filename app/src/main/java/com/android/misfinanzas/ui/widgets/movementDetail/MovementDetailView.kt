package com.android.misfinanzas.ui.widgets.movementDetail

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.cardview.widget.CardView
import com.android.data.UserSesion
import com.android.data.local.model.*
import com.android.misfinanzas.R
import com.android.misfinanzas.base.MovementType
import kotlinx.android.synthetic.main.movement_detail_view.view.*
import java.math.BigDecimal
import java.util.*

class MovementDetailView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.movement_detail_view, this)
    }

    private lateinit var movementTypes: List<MovementType>
    private var descriptions: List<String>? = null
    private var people: List<PersonVO>? = null
    private var places: List<PlaceVO>? = null
    private var categories: List<CategoryVO>? = null
    private var debts: List<DebtVO>? = null

    private var movement: MovementVO? = null
    private var selectedMovementType: Int = MovementType.NOT_SELECTED

    fun initView(
        descriptions: List<String>?,
        people: List<PersonVO>?,
        places: List<PlaceVO>?,
        categories: List<CategoryVO>?,
        debts: List<DebtVO>?
    ) {
        this.descriptions = descriptions
        this.people = people
        this.places = places
        this.categories = categories
        this.debts = debts
        setMastersPickers()
        setControlsRules()
    }

    private fun setMastersPickers() {
        movementTypes = MovementType.getList(context)
        val movementTypeAdapter = ArrayAdapter<MovementType>(context, android.R.layout.simple_spinner_item, movementTypes)
        movementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_tipo_movimiento.adapter = movementTypeAdapter

        sp_tipo_movimiento.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val movementType: MovementType = parent.getItemAtPosition(pos) as MovementType
                selectedMovementType = movementType.id
                iv_tipo_movimiento.setImageResource(MovementType.getImage(selectedMovementType))
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        descriptions?.let {
            val descriptionsAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, it)
            tv_descripcion_value.setAdapter(descriptionsAdapter)
        }

        people?.let {
            val peopleAdapter = ArrayAdapter<PersonVO>(context, android.R.layout.simple_list_item_1, it)
            tv_persona_value.setAdapter(peopleAdapter)
        }

        places?.let {
            val placesAdapter = ArrayAdapter<PlaceVO>(context, android.R.layout.simple_list_item_1, it)
            tv_lugar_value.setAdapter(placesAdapter)
        }

        categories?.let {
            val categoriesAdapter = ArrayAdapter<CategoryVO>(context, android.R.layout.simple_list_item_1, it)
            tv_categoria_value.setAdapter(categoriesAdapter)
        }

        debts?.let {
            val debtsAdapter = ArrayAdapter<DebtVO>(context, android.R.layout.simple_list_item_1, it)
            tv_deuda_value.setAdapter(debtsAdapter)
        }

        val currentDate: Date = UserSesion.getCurrentDateTime()
        et_fecha_movimiento.setText(UserSesion.getDateFormat().format(currentDate))
    }

    private fun setControlsRules() {
        et_fecha_movimiento.inputType = InputType.TYPE_NULL
        et_fecha_movimiento.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            val picker = DatePickerDialog(
                context, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val date = context.getString(R.string.md_date, dayOfMonth.toString(), (monthOfYear + 1).toString(), year.toString())
                    et_fecha_movimiento.setText(date)
                }, year, month, day
            )
            picker.show()
        }
    }

    fun showMovement(movement: MovementVO?) {
        this.movement = movement

        if (movement == null) {
            tv_fecha_ingreso.visibility = View.GONE
            tv_fecha_ingreso_value.visibility = View.GONE
            tv_fecha_upd.visibility = View.GONE
            tv_fecha_upd_value.visibility = View.GONE
        }

        movement?.let {
            val notAssociated = context.getString(R.string.md_view_not_associated)
            val date = if (it.date == null) notAssociated else UserSesion.getDateFormat().format(checkNotNull(it.date)).toString()
            val entryDate =
                if (it.dateEntry == null) notAssociated else UserSesion.getDateTimeFormat().format(checkNotNull(it.dateEntry)).toString()
            val lastUpdate =
                if (it.dateLastUpd == null) notAssociated else UserSesion.getDateTimeFormat().format(checkNotNull(it.dateLastUpd)).toString()

            iv_tipo_movimiento.setImageResource(MovementType.getImage(it.idType))
            sp_tipo_movimiento.setSelection(getMovementTypeIndex(it.idType))
            et_valor.setText(it.value.toString())
            tv_descripcion_value.setText(it.description)
            tv_persona_value.setText(getPersonName(it.personId))
            tv_lugar_value.setText(getPlaceName(it.placeId))
            tv_categoria_value.setText(getCategoryName(it.categoryId))
            et_fecha_movimiento.setText(date)
            tv_deuda_value.setText(getDebtName(it.debtId))
            tv_fecha_ingreso_value.text = entryDate
            if (lastUpdate == notAssociated) {
                tv_fecha_upd.visibility = View.GONE
                tv_fecha_upd_value.visibility = View.GONE
            } else {
                tv_fecha_upd_value.text = lastUpdate
            }
        }
    }

    @Throws(Exception::class)
    fun getMovement(): MovementVO {
        return validateForm()
    }

    @Throws(Exception::class)
    private fun validateForm(): MovementVO {
        if (selectedMovementType == MovementType.NOT_SELECTED) {
            throw Exception(context.getString(R.string.info_select_movement_type))
        }

        val debtId = getDebtId(tv_deuda_value.text.toString())
        if (selectedMovementType == MovementType.CREDIT_CARD_BUY && debtId == 0) {
            sp_tipo_movimiento.requestFocus()
            throw Exception(context.getString(R.string.info_select_movement_type))
        }

        val strValue = et_valor.text.toString()
        if (strValue.isEmpty()) {
            et_valor.requestFocus()
            throw Exception(context.getString(R.string.info_enter_value))
        }
        val value: BigDecimal
        try {
            value = BigDecimal(strValue)
        } catch (e: Exception) {
            et_valor.requestFocus()
            throw Exception(context.getString(R.string.info_only_numbers))
        }

        val description = tv_descripcion_value.text.toString()
        val personId = getPersonId(tv_persona_value.text.toString())
        val placeId = getPlaceId(tv_lugar_value.text.toString())

        val categoryId = getCategoryId(tv_categoria_value.text.toString())
        if ((categoryId ?: 0) == 0) {
            tv_categoria_value.requestFocus()
            throw Exception(context.getString(R.string.info_select_category))
        }

        val strDate = et_fecha_movimiento.text.toString()
        if (strDate.isEmpty()) {
            throw Exception(context.getString(R.string.info_select_date))
        }
        val date: Date
        try {
            date = UserSesion.getDateFormat().parse(strDate)
        } catch (e: Exception) {
            et_fecha_movimiento.requestFocus()
            throw Exception(context.getString(R.string.info_enter_valid_date))
        }

        val currentDateTime: Date = UserSesion.getCurrentDateTime()

        var dateLastUpdate: Date? = null
        var dateEntry: Date? = currentDateTime
        if (movement != null) {
            dateLastUpdate = currentDateTime
            dateEntry = movement?.dateEntry
        }

        return MovementVO(
            movement?.idMovement ?: 0,//Autoincrement
            selectedMovementType,
            value,
            description,
            personId,
            placeId,
            categoryId!!,
            date,
            debtId,
            dateEntry,
            dateLastUpdate
        )
    }

    fun cleanFormAfterSave() {
        et_valor.setText("")
        et_valor.requestFocus()
        tv_persona_value.setText("")
        tv_deuda_value.setText("")
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
