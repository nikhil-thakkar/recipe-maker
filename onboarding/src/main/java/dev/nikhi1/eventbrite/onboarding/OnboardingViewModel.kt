package dev.nikhi1.eventbrite.onboarding

class OnboardingViewModel(private val dataRepository: DataRepository) {

    fun fetchTopics() {
        dataRepository.getTopics()
    }
}