package dev.nikhi1.recipe.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.nikhi1.recipe.core.BaseViewModel
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.core.UIState
import dev.nikhi1.recipe.core.ViewState
import dev.nikhi1.recipe.core.ViewStateLiveData
import dev.nikhi1.recipe.core.exhaustive
import dev.nikhi1.recipe.onboarding.data.DietRepository
import dev.nikhi1.recipe.onboarding.ui.model.DietViewType
import kotlinx.coroutines.launch

data class OnboardingViewState(
    val uiState: UIState = UIState.Loading,
    val diets: List<DietViewType> = emptyList()
) : ViewState

class OnboardingViewModel(private val dietPresenter: DietPresenter, private val dataRepository: DietRepository) : BaseViewModel<OnboardingViewState>() {

    private val _viewState = ViewStateLiveData(
        OnboardingViewState()
    )

    override val viewState: LiveData<OnboardingViewState> = _viewState

    fun fetchTopics() {

        viewModelScope.launch {

            when (val result = dataRepository.getDiets()) {

                is Result.Success -> {
                    val data = result.data ?: emptyList()
                    if (data.isNotEmpty()) {
                        _viewState.value =
                            _viewState.value.copy(
                                uiState = UIState.Content,
                                diets = dietPresenter.map(data)
                            )
                    } else {
                        _viewState.value = _viewState.value.copy(uiState = UIState.Empty)
                    }
                }

                is Result.Failure -> {
                    _viewState.value = _viewState.value.copy(uiState = UIState.Error(result.error))
                }
            }.exhaustive
        }
    }
}