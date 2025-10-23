package com.kamath.comcastcodingchallenge.animals.presentation.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.kamath.comcastcodingchallenge.animals.domain.model.Dog
import com.kamath.comcastcodingchallenge.animals.presentation.AnimalScreenEvent
import com.kamath.comcastcodingchallenge.animals.presentation.AnimalScreenState
import org.junit.Rule
import org.junit.Test


class AnimalScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val testAnimal = Dog(
        name = "Buddy",
        slogan = "A good boy",
        scientificName = "Canis lupus familiaris",
        phylum = "Chordata",
        lifespan = "12 years"
    )
    private val fakeUiState = AnimalScreenState(
        isLoading = false,
        animals = listOf(testAnimal),
        searchQuery = "",
        error = null
    )
    private val animals = listOf(
        Dog("Spot", "Spots a lot", "Canis familiaris", "Chordata", "10 years"),
        Dog("Max", "Big bark", "Canis lupus familiaris", "Chordata", "8 years"),
        Dog("Rex", "Loyal", "Canis lupus", "Chordata", "12 years")
    )

    @Test
    fun animalScreenContent_displaysAnimalCardWithName() {
        composeTestRule.setContent {
            AnimalScreenContent(
                uiState = fakeUiState,
                onEvent = {},
                modifier = Modifier
            )
        }
        composeTestRule.onNodeWithText(testAnimal.name).assertExists()
        composeTestRule.onNodeWithText(testAnimal.slogan).assertExists()
        composeTestRule.onNodeWithText(testAnimal.scientificName).assertExists()
    }

    @Test
    fun animalScreenContent_showsLoadingIndicator_whenLoading() {
        val loadingState = fakeUiState.copy(isLoading = true, animals = emptyList())
        composeTestRule.setContent {
            AnimalScreenContent(
                uiState = loadingState,
                onEvent = {},
                modifier = Modifier
            )
        }
        composeTestRule
            .onNode(
                hasTestTag("progress"),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun animalScreenContent_filtersList_whenSearch() {
        var searchQuery by mutableStateOf("")
        var filteredAnimals by mutableStateOf(animals)

        composeTestRule.setContent {
            AnimalScreenContent(
                uiState = AnimalScreenState(
                    isLoading = false,
                    animals = filteredAnimals,
                    searchQuery = searchQuery
                ),
                onEvent = { event ->
                    if (event is AnimalScreenEvent.OnAnimalSearch) {
                        searchQuery = event.query
                        filteredAnimals = if (searchQuery.isBlank()) {
                            animals
                        } else {
                            animals.filter { it.name.contains(searchQuery, ignoreCase = true) }
                        }
                    }
                },
                modifier = Modifier
            )
        }
        composeTestRule
            .onNodeWithTag("searchBar", useUnmergedTree = true)
            .performTextInput("Max")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("animalCard_Max").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Spot").assertDoesNotExist()
        composeTestRule.onNodeWithTag("Rex").assertDoesNotExist()
    }

}