package dev.nikhi1.eventbrite.clone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _navigate = MutableLiveData<String>()

    val navigate: LiveData<String> = _navigate

    init {
        viewModelScope.launch {
            delay(500)
            _navigate.value = "Go"
        }
    }
}
