package com.android.misfinanzas.ui.logged.masters.mastersList

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.model.Master
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMastersListBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.sync.SyncState
import com.android.misfinanzas.ui.logged.masters.mastersList.adapter.MastersAdapter
import com.android.misfinanzas.ui.logged.masters.mastersList.masterDetail.MasterDetailFragment
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.getEventBus
import com.android.misfinanzas.utils.hideLoader
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.showLoader
import com.android.misfinanzas.utils.showLongToast
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MastersListFragment : Fragment(R.layout.fragment_masters_list) {

    companion object {
        const val MASTERS_TYPE: String = "MastersType"
    }

    private val viewModel by viewModel<MastersListViewModel>()
    private val binding by viewBinding<FragmentMastersListBinding>()

    private val mastersAdapter by lazy { MastersAdapter() }

    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(MASTERS_TYPE, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupViewModel()
        setupSyncObserver()
        setupRecyclerView()
        setupSearchView()
        setupEvents()
    }

    override fun onResume() {
        super.onResume()
        binding.rvMasters.adapter?.notifyDataSetChanged()
    }

    private fun setTitle() {
        if (type == 0) {
            activity?.onBackPressed()
        }

        var title = EMPTY

        when (type) {
            Master.TYPE_PERSON -> title = getString(R.string.masters_people)
            Master.TYPE_PLACE -> title = getString(R.string.masters_places)
            Master.TYPE_CATEGORY -> title = getString(R.string.masters_categories)
            Master.TYPE_DEBT -> title = getString(R.string.masters_debts)
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = title
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        getData()
    }

    private fun getData() {
        showLoader()
        when (type) {
            Master.TYPE_PERSON -> viewModel.getPeople()
            Master.TYPE_PLACE -> viewModel.getPlaces()
            Master.TYPE_CATEGORY -> viewModel.getCategories()
            Master.TYPE_DEBT -> viewModel.getDebts()
        }
    }

    private fun setupSyncObserver() {
        getEventBus(EventSubject.SYNC).observe(viewLifecycleOwner, syncStateObserver)
    }

    private val syncStateObserver = Observer<Any> { state ->
        when (state) {
            SyncState.Success -> getData()
        }
    }

    private val viewStateObserver = Observer<MastersListViewState> { state ->
        hideLoader()
        when (state) {
            is MastersListViewState.PeopleLoaded -> setupRecyclerViewData(state.people)
            is MastersListViewState.PlacesLoaded -> setupRecyclerViewData(state.places)
            is MastersListViewState.CategoriesLoaded -> setupRecyclerViewData(state.categories)
            is MastersListViewState.DebtsLoaded -> setupRecyclerViewData(state.debts)
            is MastersListViewState.MastersFiltered -> setupRecyclerViewData(state.masters)
        }
    }

    private fun setupRecyclerView() = with(binding.rvMasters) {
        adapter = mastersAdapter
        layoutManager = LinearLayoutManager(requireContext())
        mastersAdapter.setOnActionItemListener(actionListener)
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(items: List<MasterModel>) {
        if (items.isEmpty()) {
            context?.showLongToast(R.string.info_no_data)
            return
        }
        mastersAdapter.submitList(items)
    }

    private fun setupSearchView() {
        binding.svSearch.setOnClickListener { binding.svSearch.isIconified = false }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you want to change when press search button
                viewModel.filter(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                viewModel.filter(newText.orEmpty())
                return false
            }
        })
    }

    private fun setupEvents() = with(binding) {
        btnAddMaster.setOnClickListener {
            navigateToDetails(null)
        }
    }

    private fun navigateToDetails(master: MasterModel?) {
        if (context?.isConnected(getString(R.string.error_not_network_no_continue)) == true) {
            val bundle = Bundle()
            bundle.putInt(MasterDetailFragment.MASTER_TYPE, type)
            bundle.putStringArrayList(MasterDetailFragment.DESCRIPTIONS_DATA, viewModel.descriptions as ArrayList<String>)
            master?.let { bundle.putSerializable(MasterDetailFragment.MASTER_DATA, it) }
            findNavController().navigate(R.id.action_mastersListFragment_to_masterDetailFragment, bundle)
        }
    }

    private val actionListener = object : MastersAdapter.OnActionItemListener {

        override fun onMasterClicked(master: MasterModel?) {
            navigateToDetails(master)
        }

    }

}
