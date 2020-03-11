package dev.nikhi1.eventbrite.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.nikhi1.eventbrite.core.BaseViewModel
import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.core.UIState
import dev.nikhi1.eventbrite.core.ViewState
import dev.nikhi1.eventbrite.core.ViewStateLiveData
import dev.nikhi1.eventbrite.core.exhaustive
import dev.nikhi1.eventbrite.onboarding.data.DataRepository
import dev.nikhi1.eventbrite.onboarding.data.model.SubCategoryResponse
import dev.nikhi1.eventbrite.onboarding.data.model.Subcategory
import kotlinx.coroutines.launch

data class OnboardingViewState(
    val uiState: UIState = UIState.Loading,
    val categories: List<Subcategory> = emptyList()
) : ViewState

class OnboardingViewModel(private val dataRepository: DataRepository) : BaseViewModel<OnboardingViewState>() {

    private val _viewState = ViewStateLiveData(OnboardingViewState())

    override val viewState: LiveData<OnboardingViewState> = _viewState

    fun fetchTopics() {

        viewModelScope.launch {

            when (val result = dataRepository.getTopics(null)) {

                is Result.Success -> {
                    val data = result.data ?: SubCategoryResponse.EMPTY
                    if (data.subcategories.isNotEmpty()) {
                        _viewState.value = _viewState.value.copy(uiState = UIState.Content, categories = data.subcategories)
                    } else {
                        _viewState.value = _viewState.value.copy(uiState = UIState.Empty)
                    }
                }

                is Result.Failure -> {
                    _viewState.value = _viewState.value.copy(uiState = UIState.Error)
                }
            }.exhaustive
        }
    }
}