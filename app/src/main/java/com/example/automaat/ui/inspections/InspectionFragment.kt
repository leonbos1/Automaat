package com.example.automaat.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.automaat.R

class InspectionFragment : Fragment() {
    private var inspectionViewModel: InspectionViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_inspection, container, false)

        inspectionViewModel = ViewModelProvider(this).get(InspectionViewModel::class.java)

        return view
    }
}