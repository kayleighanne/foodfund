package com.example.foodfund.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvailableProductsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is available products Fragment"
    }
    val text: LiveData<String> = _text
}