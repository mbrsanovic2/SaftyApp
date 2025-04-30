package com.example.saftyapp.model.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.Objects.UserData
import com.example.saftyapp.model.SaftyExpression
import com.example.saftyapp.model.database.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random

class XPViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository = Repository.getInstance(application.applicationContext)

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
        val title : Boolean

        if (newXP >= targetXP) {
            newXP -= targetXP
            title = (current.level + 1) >= 5

            repository.UserFunctions().increaseLvL()
            repository.UserFunctions().resetXP()
            repository.UserFunctions().increaseXP(newXP)
            repository.UserFunctions().updateTargetXP()
            repository.UserFunctions().setTitle(title)
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
}