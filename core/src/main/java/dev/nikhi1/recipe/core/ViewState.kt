package dev.nikhi1.recipe.core

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface ViewState

abstract class BaseViewModel<VS: ViewState> : ViewModel() {
    abstract val viewState: LiveData<VS>
}

sealed class UIState {
    object Loading : UIState()
    object Empty : UIState()
    data class Error(val error: Throwable? = null) : UIState()
    object Content : UIState()
}

class ViewStateLiveData<T : ViewState>(@NonNull data: T) : MutableLiveData<T>(data) {

    @NonNull
    override fun getValue(): T {
        return super.getValue()!!
    }
}
