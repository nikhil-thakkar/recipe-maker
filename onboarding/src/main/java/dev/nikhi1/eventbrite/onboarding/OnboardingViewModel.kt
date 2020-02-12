package dev.nikhi1.eventbrite.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.nikhi1.eventbrite.core.Success
import kotlinx.coroutines.launch

class OnboardingViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _topics = MutableLiveData<List<String>>()

    val topics = _topics

    fun fetchTopics() {
        viewModelScope.launch {
            when (val result = dataRepository.getTopics()) {
                is Success -> {
                    _topics.value = result.data
                }
            }
        }
    }
}