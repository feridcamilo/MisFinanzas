package com.android.misfinanzas.ui.logged.masters.mastersList.masterDetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.domain.model.Master
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMasterDetailBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.sync.SyncType
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MasterDetailFragment : Fragment(R.layout.fragment_master_detail) {

    private val TAG = this.javaClass.name

    companion object {
        const val MASTER_TYPE: String = "MasterType"
        const val MASTER_DATA: String = "Master"
        const val DESCRIPTIONS_DATA: String = "Descriptions"
    }

    private val viewModel by viewModel<MasterDetailViewModel>()
    private val binding by viewBinding<FragmentMasterDetailBinding>()

    private var type: Int = 0
    private var master: MasterModel? = null
    private var descriptions: MutableList<String> = mutableListOf()
    private var isNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(MASTER_TYPE, 0)
            if (it.containsKey(MASTER_DATA)) {
                master = it.getSerializable(MASTER_DATA) as? MasterModel
            }
            if (it.containsKey(DESCRIPTIONS_DATA)) {
                descriptions = it.getStringArrayList(DESCRIPTIONS_DATA) ?: mutableListOf()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupViewModel()
        showMaster()
        setupEvents()
    }

    private fun setTitle() {
        if (type == 0) {
            activity?.onBackPressed()
        }

        var complement = EMPTY

        when (type) {
            Master.TYPE_PERSON -> complement = getString(R.string.md_person)
            Master.TYPE_PLACE -> complement = getString(R.string.md_place)
            Master.TYPE_CATEGORY -> complement = getString(R.string.md_category)
            Master.TYPE_DEBT -> complement = getString(R.string.md_debt)
        }

        isNew = master == null || (master?.id ?: 0) <= 0
        val title = if (isNew) getString(R.string.title_master_details_new, complement) else getString(R.string.title_master_details, complement)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = title
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private val viewStateObserver = Observer<MasterDetailViewState> { state ->
        hideLoader()
        when (state) {
            MasterDetailViewState.MasterSaved -> masterSaved()
            MasterDetailViewState.SaveFailed -> context?.showLongToast(getString(R.string.error_saving_master_no_info))
        }
    }

    private fun showMaster() = with(binding) {
        master?.let {
            etName.setText(it.name)
            swEnabled.isChecked = it.enabled
        }
    }

    private fun setupEvents() = with(binding) {
        tilName.setEndIconOnClickListener { etName.setText(EMPTY) }
        ibSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        try {
            val masterToSave = getMaster()

            if (context?.isConnected(getString(R.string.error_not_network_no_continue)) == true) {
                showLoader()
                viewModel.saveMaster(masterToSave, type)
            }
        } catch (e: Exception) {
            hideLoader()
            context?.showExceptionMessage(TAG, e.message.orEmpty(), ErrorType.TYPE_APP)
        }
    }

    private fun masterSaved() {
        backgroundSync(SyncType.SYNC_MASTERS)
        if (isNew) { //Is insert
            cleanFormAfterSave()
            context?.showShortToast(R.string.info_master_saved)
        } else { //Is update
            context?.showShortToast(R.string.info_master_updated)
            activity?.onBackPressed()
        }
    }

    @Throws(Exception::class)
    private fun getMaster(): MasterModel {
        return validateForm()
    }

    @Throws(Exception::class)
    private fun validateForm(): MasterModel = with(binding) {
        val name = etName.text.toString().trim()
        if (name.isEmpty()) {
            etName.requestFocus()
            throw Exception(getString(R.string.info_enter_name))
        }

        if (isNew && descriptions.contains(name)) {
            etName.requestFocus()
            throw Exception(getString(R.string.error_master_exists, name))
        }

        val idMaster = master?.id ?: 0

        return MasterModel(
            idMaster,
            name,
            swEnabled.isChecked
        )
    }

    private fun cleanFormAfterSave() = with(binding) {
        descriptions.add(etName.text.toString().trim())
        etName.setText(EMPTY)
        swEnabled.isChecked = true
    }

}
