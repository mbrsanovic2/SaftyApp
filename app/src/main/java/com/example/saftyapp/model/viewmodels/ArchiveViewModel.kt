package com.example.saftyapp.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.model.database.Repository
import com.example.saftyapp.model.objects.ArchiveEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val repository: Repository
): ViewModel()  {
    // Recipes from database
    private val _archiveEntries = MutableStateFlow<List<ArchiveEntry>>(emptyList())
    val archiveEntries = _archiveEntries.asStateFlow()

    init {
        viewModelScope.launch {
            loadEntries()
        }
    }

    private suspend fun loadEntries() {
        _archiveEntries.value = repository.ArchiveFunctions().getArchive()
    }

    // Warte auf ArchiveEntryAnpassung
    suspend fun addEntry(recipeName: String, ingredients: List<String>, preparation: String){
        TODO()
        loadEntries()
    }
}