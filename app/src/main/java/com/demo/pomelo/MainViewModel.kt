package com.demo.pomelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pomelo.BuildConfig.*
import com.demo.pomelo.data.domain.usecase.AuthTokenUseCase
import com.demo.pomelo.data.domain.usecase.SessionUseCase
import com.demo.pomelo.data.entities.ClientTokenBody
import com.demo.pomelo.data.entities.UserTokenBody
import com.pomelo.identity.Configuration
import com.pomelo.identity.Result.*
import com.pomelo.networking.resource.Resource
import com.pomelo.networking.utils.handleException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val authTokenUseCase: AuthTokenUseCase,
    private val sessionUseCase: SessionUseCase,
) : ViewModel() {

    var viewState = MutableStateFlow<MainState>(MainState.Init)
        private set

    var effect = MutableSharedFlow<MainEffect>()
        private set

    private val tokenBody by lazy {
        ClientTokenBody(CLIENT_ID, CLIENT_SECRET, AUDIENCE, GRANT_TYPE)
    }

    fun handleEvents(event: MainEvent) =
        when (event) {
            is MainEvent.InitIdentity -> initIdentity(
                email = event.email,
                country = event.country.name
            )
        }

    private fun initIdentity(email: String, country: String) = viewModelScope.launch {
        authTokenUseCase.getClientToken(tokenBody).onStart {
            effect.emit(MainEffect.Loading.Show)
        }.onCompletion {
            effect.emit(MainEffect.Loading.Hide)
        }.catch { error ->
            effect.emit(MainEffect.Loading.Hide)
            effect.emit(MainEffect.SessionError(handleException(error)))
        }.collect { clientToken ->
            sessionUseCase.getSessionID(email, country, clientToken)
                .catch { error ->
                    effect.emit(MainEffect.SessionError(handleException(error)))
                }.collect { session ->
                    when (session) {
                        is Resource.Success -> {
                            val configuration = Configuration(
                                sessionId = session.data.sessionId,
                                onResultListener = { result, _ ->
                                    when (result) {
                                        NOT_VALIDATED -> {}
                                        SESSION_EXPIRED -> {}
                                        REJECTED -> {}
                                        SUCCESS -> {}
                                        FINISHED_BY_USER -> {}
                                    }
                                },
                                authTokenListener = {
                                    authTokenUseCase.getUserToken(
                                        UserTokenBody(session.data.userId),
                                        clientToken
                                    )
                                }
                            )
                            viewState.value = MainState.InitIdentity(configuration)
                        }
                        is Resource.Error -> effect.emit(MainEffect.SessionError(session.error))
                    }

                }
        }
    }

    companion object {
        const val GRANT_TYPE = "client_credentials"
    }
}
