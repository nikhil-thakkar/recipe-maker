package dev.nikhi1.eventbrite.core

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface ViewState

abstract class BaseViewModel<ViewState> : ViewModel()

sealed class UIState {
    object Loading : UIState()
    object Empty : UIState()
    object Error : UIState()
    object Content : UIState()
}

class ViewStateLiveData<T : ViewState>(@NonNull data: T) : MutableLiveData<T>(data) {

    @NonNull
    override fun getValue(): T {
        return super.getValue()!!
    }
}
