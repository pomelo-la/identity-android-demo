package com.demo.pomelo.di

import com.demo.pomelo.BuildConfig.*
import com.demo.pomelo.MainViewModel
import com.demo.pomelo.data.domain.usecase.AuthTokenUseCase
import com.demo.pomelo.data.domain.usecase.SessionUseCase
import com.demo.pomelo.data.remote.ClientTokenService
import com.demo.pomelo.data.remote.SessionService
import com.demo.pomelo.data.remote.UserService
import com.demo.pomelo.data.remote.UserTokenService
import com.demo.pomelo.data.repository.ClientTokenRepository
import com.demo.pomelo.data.repository.SessionRepository
import com.demo.pomelo.data.repository.UserRepository
import com.demo.pomelo.data.repository.UserTokenRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object MainModule {
    private const val MAIN = "main"
    private const val CLIENT_TOKEN = "client_token"
    private const val USER_TOKEN = "user_token"
    private const val SESSION = "session"
    private const val USER = "user"

    fun initModule(): Module {
        val remoteModule = module {
            single(named(MAIN)) { provideMoshi() }
            single(named(MAIN)) { provideOkHttpClient() }

            single(named(CLIENT_TOKEN)) {
                provideRetrofit(CLIENT_TOKEN_BASE_URL, get(named(MAIN)), get(named(MAIN)))
            }
            single(named(USER_TOKEN)) {
                provideRetrofit(USER_TOKEN_BASE_URL, get(named(MAIN)), get(named(MAIN)))
            }
            single(named(USER)) {
                provideRetrofit(USER_BASE_URL, get(named(MAIN)), get(named(MAIN)))
            }
            single(named(SESSION)) {
                provideRetrofit(SESSION_BASE_URL, get(named(MAIN)), get(named(MAIN)))
            }

            single { get<Retrofit>(named(USER_TOKEN)).create(UserTokenService::class.java) }
            single { get<Retrofit>(named(CLIENT_TOKEN)).create(ClientTokenService::class.java) }
            single { get<Retrofit>(named(USER)).create(UserService::class.java) }
            single { get<Retrofit>(named(SESSION)).create(SessionService::class.java) }

            single { ClientTokenRepository(get()) }
            single { UserTokenRepository(get()) }
            single { UserRepository(get()) }
            single { SessionRepository(get()) }

            single { SessionUseCase(get(), get()) }
            single { AuthTokenUseCase(get(), get()) }

            viewModel { MainViewModel(get(), get()) }

        }
        return remoteModule
    }

    private fun provideMoshi(): Moshi? {
        val builder = Moshi.Builder()
        builder.add(KotlinJsonAdapterFactory())
        return builder.build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(
                Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                }
            )
            .addInterceptor(loggingInterceptor)

        return okHttpBuilder.build()
    }

    private fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    private const val DEFAULT_CONNECT_TIMEOUT = 15000L
    private const val DEFAULT_READ_TIMEOUT = 15000L
    private const val DEFAULT_WRITE_TIMEOUT = 15000L
}
