package com.example.misfinanzas.ui.movements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.misfinanzas.R

class MovementsFragment : Fragment() {

    private lateinit var movementsViewModel: MovementsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        movementsViewModel =
                ViewModelProviders.of(this).get(MovementsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_movements, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        movementsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
