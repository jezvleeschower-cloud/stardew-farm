package com.epico.stardewfarm.di

import android.content.Context
import com.epico.stardewfarm.BuildConfig
import com.epico.stardewfarm.data.local.SessionStorage
import com.epico.stardewfarm.data.local.SharedPrefsSessionStorage
import com.epico.stardewfarm.data.remote.AuthApi
import com.epico.stardewfarm.data.remote.AuthInterceptor
import com.epico.stardewfarm.data.remote.CultivoApi
import com.epico.stardewfarm.data.repository.AuthRepositoryImpl
import com.epico.stardewfarm.data.repository.CultivoRepositoryImpl
import com.epico.stardewfarm.domain.repository.AuthRepository
import com.epico.stardewfarm.domain.repository.CultivoRepository
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

class AppContainer(context: Context) {

    private val session: SessionStorage = SharedPrefsSessionStorage(context)

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(session))
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        redactHeader("Authorization")
                    }
                )
            }
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val authApi: AuthApi = retrofit.create()
    private val cultivoApi: CultivoApi = retrofit.create()

    val authRepository: AuthRepository = AuthRepositoryImpl(authApi, session)
    val cultivoRepository: CultivoRepository = CultivoRepositoryImpl(cultivoApi)
}