package dev.nikhi1.eventbrite.onboarding

import io.mockk.mockkClass
import org.junit.Test

class OnboardingViewModelTest {

    @Test
    fun `fetch topics or interests`() {
        val viewModel = OnboardingViewModel(mockkClass(DataRepository::class))

    }
}

class Mock : DataRepository {
    override fun getTopics() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}