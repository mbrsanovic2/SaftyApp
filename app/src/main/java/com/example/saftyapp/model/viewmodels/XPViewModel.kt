package com.example.saftyapp.model.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.objects.UserData
import com.example.saftyapp.model.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class XPViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _userState = MutableStateFlow(UserData(-1,-1,-1, false))
    val userState: StateFlow<UserData> = _userState

    private val _xpGainToAnimate = MutableStateFlow(0)
    val xpGainToAnimate: StateFlow<Int> = _xpGainToAnimate

    private val _displayedXP = MutableStateFlow(-1)
    val displayedXP: StateFlow<Int> = _displayedXP

    private val _displayedXPTarget = MutableStateFlow(-1)
    val displayedXPTarget: StateFlow<Int> = _displayedXPTarget

    private val _displayedLevel = MutableStateFlow(-1)
    val displayedLevel: StateFlow<Int> = _displayedLevel

    private val _juicyUnlocked = MutableSharedFlow<Unit>(replay = 0)
    val juicyUnlocked: SharedFlow<Unit> = _juicyUnlocked

    init {
        viewModelScope.launch {
            loadUserData()
        }
    }

    private suspend fun loadUserData() {
        val userData = repository.UserFunctions().getUserData()
        _userState.value = UserData(
            currentXP = userData.currentXP,
            targetXP = userData.targetXP,
            level = userData.level,
            isJUICY = userData.isJUICY
        )
        if(_displayedXP.value == -1) refreshDisplayState(userData)
    }

    suspend fun gainXP(amount: Int) {
        val current = _userState.value
        refreshDisplayState(current)
        _xpGainToAnimate.value = amount

        var newXP = current.currentXP + amount
        val targetXP = current.targetXP
        val currentJuicy = (current.level) >= 5

        if (newXP >= targetXP) {
            newXP -= targetXP

            repository.UserFunctions().increaseLvL()
            repository.UserFunctions().increaseXP(newXP)
            if(!currentJuicy && (current.level + 1) >= 5){
                repository.UserFunctions().setTitle(true)
                emitJuicyUnlocked()
            }
        } else {
            repository.UserFunctions().increaseXP(amount)
        }

        loadUserData()
    }
    private fun refreshDisplayState(userData: UserData) {
        _displayedXP.value = userData.currentXP
        _displayedXPTarget.value = userData.targetXP
        _displayedLevel.value = userData.level
    }

    fun incrementXP() {
        _displayedXP.value++
        if(_displayedXP.value == _displayedXPTarget.value){
            _displayedXP.value = 0
            _displayedLevel.value += 1
        }
    }

    fun reduceGainAnim(amount: Int){
        if(_xpGainToAnimate.value > 0) {
            _xpGainToAnimate.value -= amount
        }
    }

    fun emitJuicyUnlocked() {
        viewModelScope.launch {
            _juicyUnlocked.emit(Unit)
        }
    }
}