package com.android.misfinanzas.ui.movements

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.local.RoomDataSource
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.LocalRepositoryImp
import com.android.data.remote.repository.WebRepositoryImp
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_movements.*

class MovementsFragment : Fragment() {

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

        setupRecyclerView()
        setupObservers()
        btn_filtros.setOnClickListener {
            findNavController().navigate(R.id.filtersFragment)
        }
        /*
        btn_detalles.setOnClickListener {
            findNavController().navigate(R.id.movementsDetailsFragment)
        }
        */
    }

    private fun setupRecyclerView() {
        rv_movimientos.layoutManager = LinearLayoutManager(requireContext())
        rv_movimientos.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupObservers() {
        viewModel.getMovements.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    rv_movimientos.adapter = MovementsAdapter(requireContext(), result.data)
                    progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error al consultar los movimientos: ${result.exception}", Toast.LENGTH_SHORT).show()
                    Log.e("MovementsFragment", "onRetrofitRequest: ${result.exception}")
                }
            }
        })
    }
}
