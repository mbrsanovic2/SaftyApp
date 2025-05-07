package com.example.saftyapp.model.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun initializeApp() {
        viewModelScope.launch {
            // Erfuelle deine Traeume herr Konzetti
            repository.loadDefaultData()
            repository.loadTestRecipes()
        }
    }

    fun test(){
        viewModelScope.launch {

        }
    }
}