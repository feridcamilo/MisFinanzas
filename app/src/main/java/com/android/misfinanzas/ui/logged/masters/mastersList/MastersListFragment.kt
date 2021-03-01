package com.android.misfinanzas.ui.logged.masters.mastersList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.model.Master
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.MasterClickListener
import com.android.misfinanzas.databinding.FragmentMastersListBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.utils.showLongToast
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MastersListFragment : BaseFragment(), MasterClickListener {

    companion object {
        const val MASTERS_TYPE: String = "MastersType"
    }

    private val viewModel by viewModel<MastersListViewModel>()
    private var items: List<MasterModel>? = null
    private var type: Int = 0

    private lateinit var binding: FragmentMastersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(MASTERS_TYPE, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMastersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupViewModel()
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
        progressListener.show()
        when (type) {
            Master.TYPE_PERSON -> viewModel.getPeople()
            Master.TYPE_PLACE -> viewModel.getPlaces()
            Master.TYPE_CATEGORY -> viewModel.getCategories()
            Master.TYPE_DEBT -> viewModel.getDebts()
        }
    }

    private val viewStateObserver = Observer<MastersListViewState> { state ->
        progressListener.hide()
        when (state) {
            is MastersListViewState.PeopleLoaded -> setupRecyclerViewData(state.people)
            is MastersListViewState.PlacesLoaded -> setupRecyclerViewData(state.places)
            is MastersListViewState.CategoriesLoaded -> setupRecyclerViewData(state.categories)
            is MastersListViewState.DebtsLoaded -> setupRecyclerViewData(state.debts)
        }
    }

    private fun setupRecyclerView() = with(binding.rvMasters) {
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(items: List<MasterModel>) {
        if (items.isEmpty()) {
            context?.showLongToast(R.string.info_no_data)
            return
        }
        binding.rvMasters.adapter = MastersAdapter(requireContext(), items, this)
    }

    private fun setupSearchView() {
        binding.svSearch.setOnClickListener { binding.svSearch.isIconified = false }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you can to change when press search button
                if (items != null) {
                    filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                if (items != null) {
                    filter(newText)
                }
                return false
            }
        })
    }

    private fun filter(text: String?) {
        progressListener.show(false)
        if (!text.isNullOrEmpty()) {
            val textToCompare = text.toLowerCase(Locale.ROOT)
            val mastersFiltered = items?.filter {
                it.name.toLowerCase(Locale.ROOT).contains(textToCompare)
            }
            setupRecyclerViewData(mastersFiltered!!)
        } else {
            setupRecyclerViewData(items!!)
        }
        progressListener.hide()
    }

    private fun setupEvents() = with(binding) {
        btnAddMaster.setOnClickListener {
            navigateToDetails(null)
        }
    }

    private fun navigateToDetails(master: MasterModel?) {
        //TODO pending to implement master detail fragment and functionality
        //val bundle = Bundle()
        //bundle.putParcelable(dataName, master)
        //findNavController().navigate(R.id., bundle)
    }

    override fun onMasterClicked(master: MasterModel?) {
        navigateToDetails(master)
    }

}
