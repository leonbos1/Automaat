package com.example.automaat.ui.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is filter Fragment"
    }
    val text: LiveData<String> = _text
}