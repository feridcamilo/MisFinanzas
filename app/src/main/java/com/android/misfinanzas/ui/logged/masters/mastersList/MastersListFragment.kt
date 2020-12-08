package com.android.misfinanzas.ui.logged.masters.mastersList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.model.Master
import com.android.domain.utils.StringUtils
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.MasterClickListener
import com.android.misfinanzas.databinding.FragmentMastersListBinding
import java.util.*

class MastersListFragment : BaseFragment(), MasterClickListener {

    companion object {
        const val MASTERS_DATA: String = "Masters"
        const val LIST_TITLE: String = "Title"
    }

    private val TAG = this.javaClass.name

    private var masters: List<Master>? = null
    private var title: String = StringUtils.EMPTY

    private lateinit var binding: FragmentMastersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            masters = it.getParcelableArrayList(MASTERS_DATA)
            title = it.getString(LIST_TITLE, StringUtils.EMPTY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMastersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = title

        setupRecyclerView()
        masters?.let { setupRecyclerViewData(it) }
        setupSearchView()
        setupEvents()

    }

    private fun setupEvents() = with(binding) {
        btnAddMaster.setOnClickListener {
            navigateToDetails(null)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvMasters.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() = with(binding.rvMasters) {
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(masters: List<Master>) {
        binding.rvMasters.adapter = MastersAdapter(requireContext(), masters, this)
    }

    private fun setupSearchView() {
        binding.svSearch.setOnClickListener { binding.svSearch.isIconified = false }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement if you can to change when press search button
                if (masters != null) {
                    filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you want to change in every letter written
                if (masters != null) {
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
            val mastersFiltered = masters?.filter {
                it.name.toLowerCase(Locale.ROOT).contains(textToCompare)
            }
            setupRecyclerViewData(mastersFiltered!!)
        } else {
            setupRecyclerViewData(masters!!)
        }
        progressListener.hide()
    }

    private fun navigateToDetails(master: Master?) {
        //TODO pending to implement master detail fragment and functionality
        //val bundle = Bundle()
        //bundle.putParcelable(dataName, master)
        //findNavController().navigate(R.id., bundle)
    }

    override fun onMasterClicked(master: Master?) {
        navigateToDetails(master)
    }

}
