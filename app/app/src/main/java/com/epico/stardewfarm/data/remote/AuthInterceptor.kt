package com.epico.stardewfarm.data.remote

import com.epico.stardewfarm.data.local.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val session: SessionStorage,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val path = original.url.encodedPath
        val esAuth = path.endsWith("/login") || path.endsWith("/register")
        val token = session.obtenerToken()

        val request = if (token != null && !esAuth) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}