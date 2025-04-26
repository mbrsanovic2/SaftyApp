package com.example.saftyapp.model.viewmodels

import android.app.Application
import android.content.Context
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

    private val _userState = MutableStateFlow(UserData(0,0,1, false))
    val userState: StateFlow<UserData> = _userState

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
    }

    suspend fun gainXP(amount: Int) {
        val current = _userState.value

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
}