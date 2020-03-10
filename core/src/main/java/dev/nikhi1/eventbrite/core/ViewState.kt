package dev.nikhi1.eventbrite.core

import androidx.lifecycle.ViewModel

interface ViewState

abstract class BaseViewModel<ViewState> : ViewModel()

sealed class UIState {
    object Loading : UIState()
    object Empty : UIState()
    object Error : UIState()
    object Content : UIState()
}
