package com.kamath.comcastcodingchallenge.animals.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.usecase.GetAnimalsUseCase
import com.kamath.comcastcodingchallenge.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AnimalScreenState(
    val isLoading: Boolean = false,
    val animals: List<Animal> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

sealed interface AnimalScreenEvent {
    data class OnAnimalSearch(val query: String) : AnimalScreenEvent
}

@OptIn(FlowPreview::class)
@HiltViewModel
class AnimalScreenViewModel @Inject constructor(
    private val getAnimalsUseCase: GetAnimalsUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _animalDataState: StateFlow<Resource<List<Animal>>> = getAnimalsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )
    val uiState: StateFlow<AnimalScreenState> = combine(
        _animalDataState,
        _searchQuery,
        _searchQuery.debounce(400L)
    ) { animalResource, instantQuery, debouncedQuery ->

        val allAnimals = animalResource.data ?: emptyList()
        val filteredAnimals = if (debouncedQuery.isBlank()) {
            allAnimals
        } else {
            allAnimals.filter { animal ->
                animal.searchableContent().any { it.contains(debouncedQuery, ignoreCase = true) }
            }
        }

        AnimalScreenState(
            isLoading = animalResource is Resource.Loading && allAnimals.isEmpty(),
            animals = filteredAnimals,
            searchQuery = instantQuery,
            error = if (animalResource is Resource.Error) animalResource.message else null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimalScreenState(isLoading = true)
    )
    fun onEvent(event: AnimalScreenEvent) {
        when (event) {
            is AnimalScreenEvent.OnAnimalSearch -> {
                _searchQuery.value = event.query
            }
        }
    }
}