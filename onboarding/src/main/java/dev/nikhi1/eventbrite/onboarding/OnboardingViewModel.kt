package dev.nikhi1.eventbrite.onboarding

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.nikhi1.eventbrite.core.BaseViewModel
import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.core.UIState
import dev.nikhi1.eventbrite.core.ViewState
import dev.nikhi1.eventbrite.core.exhaustive
import dev.nikhi1.eventbrite.onboarding.data.DataRepository
import kotlinx.coroutines.launch

data class OnboardingViewState(
    val uiState: UIState = UIState.Loading,
    val categories: List<String> = emptyList()
) : ViewState

class ViewStateLiveData<T: ViewState>(@NonNull data: T) : MutableLiveData<T>(data) {

    @NonNull
    override fun getValue(): T {
        return super.getValue()!!
    }
}

class OnboardingViewModel(private val dataRepository: DataRepository) : BaseViewModel<OnboardingViewState>() {

    private val _viewState = ViewStateLiveData(OnboardingViewState())

    val viewState: LiveData<OnboardingViewState> = _viewState

    fun fetchTopics() {

        viewModelScope.launch {

            when (val result = dataRepository.getTopics()) {

                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(uiState = UIState.Content, categories = result.data ?: emptyList())
                }

                is Result.Failure -> {
                    _viewState.value = _viewState.value.copy(uiState = UIState.Error)
                }
            }.exhaustive
        }
    }
}