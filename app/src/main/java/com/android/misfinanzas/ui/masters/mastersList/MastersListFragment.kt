package com.android.misfinanzas.ui.masters.mastersList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.local.model.MasterVO
import com.android.data.utils.StringUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.OnMasterClickListener
import kotlinx.android.synthetic.main.fragment_masters_list.*
import java.util.*

class MastersListFragment : BaseFragment(), OnMasterClickListener {

    companion object {
        const val MASTERS_DATA: String = "Masters"
        const val LIST_TITLE: String = "Title"
    }

    private val TAG = this.javaClass.name

    private var masters: List<MasterVO>? = null
    private var title: String = StringUtils.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            masters = it.getParcelableArrayList(MASTERS_DATA)
            title = it.getString(LIST_TITLE, StringUtils.EMPTY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_masters_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = title

        setupRecyclerView()
        masters?.let { setupRecyclerViewData(it) }
        setupSearchView()

        btn_add_master.setOnClickListener {
            navigateToDetails(null)
        }
    }

    override fun onResume() {
        super.onResume()
        rv_masters.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        rv_masters.layoutManager = LinearLayoutManager(requireContext())
        rv_masters.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(masters: List<MasterVO>) {
        rv_masters.adapter = MastersAdapter(requireContext(), masters, this)
    }

    private fun setupSearchView() {
        sv_search.setOnClickListener { sv_search.isIconified = false }
        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun navigateToDetails(master: MasterVO?) {
        //TODO pending to implement master detail fragment and functionality
        val bundle = Bundle()
        //bundle.putParcelable(dataName, master)
        //findNavController().navigate(R.id., bundle)
    }

    override fun onMasterClicked(master: MasterVO?) {
        navigateToDetails(master)
    }
}
