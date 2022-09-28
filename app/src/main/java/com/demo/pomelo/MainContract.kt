package com.demo.pomelo

import com.pomelo.identity.Configuration
import com.pomelo.networking.resource.ErrorEntity


sealed class MainState {
    object Init : MainState()
    object Finish : MainState()
    data class InitIdentity(val config: Configuration) : MainState()
}

sealed class MainEffect {
    sealed class Loading : MainEffect() {
        object Show : Loading()
        object Hide : Loading()
    }

    data class SessionError(val error: ErrorEntity) : MainEffect()
}

sealed class MainEvent {
    data class InitIdentity(val email: String, val country: OperationCountry) : MainEvent()
}


enum class OperationCountry(val value: String) {
    ARG("Argentina")
}
