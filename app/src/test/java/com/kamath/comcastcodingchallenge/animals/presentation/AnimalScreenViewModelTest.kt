package com.kamath.comcastcodingchallenge.animals.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.model.Bird
import com.kamath.comcastcodingchallenge.animals.domain.model.Bug
import com.kamath.comcastcodingchallenge.animals.domain.model.Dog
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import com.kamath.comcastcodingchallenge.animals.domain.usecase.GetAnimalsUseCase
import com.kamath.comcastcodingchallenge.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AnimalScreenViewModelTest {
    private lateinit var viewmodel: AnimalScreenViewModel
    private lateinit var getAnimalUseCase: GetAnimalsUseCase
    private lateinit var repository: AnimalRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testAnimals = listOf<Animal>(
        Dog(
            name = "Dog",
            slogan = "A good boy",
            scientificName = "Canis lupus familiaris",
            phylum = "Chordata",
            lifespan = "10 - 13 years¬"
        ),
        Bird(
            name = "Bird Of Paradise",
            scientificName = "Passer domesticus",
            phylum = "Chordata",
            wingspan = "7.8 to 47.2 inches (20 to 120 cm)",
            habitat = "Tropical forest tree tops"
        ),
        Bug(
            name = "Mealybug",
            phylum = "",
            scientificName = "Apis mellifera",
            prey = "Insects or blood",
            predators = "American cockroaches, cone-nosed insects, pharaoh ants"
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        getAnimalUseCase = GetAnimalsUseCase(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `initial state is loading then success`() = runTest(testScheduler) {
        whenever(getAnimalUseCase()).thenReturn(
            flowOf(Resource.Loading(), Resource.Success(testAnimals))
        )
        whenever(repository.refreshAnimals()).thenReturn(Unit)
        viewmodel = AnimalScreenViewModel(getAnimalUseCase)
        runCurrent()

        viewmodel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            assertThat(initialState.animals).isEmpty()

            runCurrent()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.animals).isEqualTo(testAnimals)
            assertThat(successState.error).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on search filters animal list`() = runTest(testScheduler) {
        // Arrange
        whenever(repository.getAnimals()).thenReturn(flowOf(Resource.Success(testAnimals)))
        whenever(repository.refreshAnimals()).thenReturn(Unit)
        viewmodel = AnimalScreenViewModel(getAnimalUseCase)

        viewmodel.uiState.test {
            skipItems(1)

            val initialState = awaitItem()
            assertThat(initialState.animals).hasSize(3)
            assertThat(initialState.isLoading).isFalse()

            viewmodel.onEvent(AnimalScreenEvent.OnAnimalSearch("dog"))

            val instantSearchState = awaitItem()
            assertThat(instantSearchState.searchQuery).isEqualTo("dog")
            assertThat(instantSearchState.animals).hasSize(3)

            advanceUntilIdle()

            val filteredState = awaitItem()
            assertThat(filteredState.searchQuery).isEqualTo("dog")
            assertThat(filteredState.animals).hasSize(1)
            assertThat(filteredState.animals.first().name).isEqualTo("Dog")
        }
    }
}
