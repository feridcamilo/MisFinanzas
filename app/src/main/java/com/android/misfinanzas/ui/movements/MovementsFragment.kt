package com.android.misfinanzas.ui.movements

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.local.RoomDataSource
import com.android.data.local.model.MovementVO
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.local.repository.UserSesion
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_movements.*

class MovementsFragment : BaseFragment(), MovementsAdapter.OnMovementClickListener {

    val TAG = this.javaClass.name

    private val viewModel by viewModels<MovementsViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (UserSesion.getUser() == null) {
            navigateToSync()
        } else {
            setupRecyclerView()
            setupSearchView()
            getLocalMovements()

            btn_filtros.setOnClickListener {
                findNavController().navigate(R.id.filtersFragment)
            }

            btn_add_movement.setOnClickListener {
                findNavController().navigate(R.id.action_movementsFragment_to_movementDetailFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        rv_movimientos.layoutManager = LinearLayoutManager(requireContext())
        rv_movimientos.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupSearchView() {
        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //implement if you can to change in every letter written
                return false
            }
        })
    }

    private fun navigateToSync() {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_MOVEMENTS, true)
        findNavController().navigate(R.id.action_movementsFragment_to_syncFragment, bundle)
    }

    private fun getLocalMovements() {
        viewModel.getLocalMovements.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.info_no_movements, Toast.LENGTH_SHORT).show()
                    } else {
                        rv_movimientos.adapter = MovementsAdapter(requireContext(), result.data, this)
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        })
    }

    override fun onMovementClicked(movement: MovementVO) {
        val bundle = Bundle()
        bundle.putParcelable(MovementDetailFragment.MOVEMENT_DATA, movement)
        findNavController().navigate(R.id.action_movementsFragment_to_movementDetailFragment, bundle)
    }
}
