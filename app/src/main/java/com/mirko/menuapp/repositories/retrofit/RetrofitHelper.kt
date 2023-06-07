package com.mirko.menuapp.repositories.retrofit

import okhttp3.logging.HttpLoggingInterceptor
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import androidx.databinding.ObservableField
import com.mirko.menuapp.MenuApp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object RetrofitHelper {
    private const val BASE_URL = "https://api-qa.menu.app/"

    val networkCommunication = ObservableField(false)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->

                    networkCommunication.set(true)
                    val request = chain.request().newBuilder()
                        .addHeader("application", "mobile-application")
                        .addHeader("Content-Type", "application/json")
                        .addHeader(
                            "Device-UUID",
                            Settings.Secure.getString(MenuApp.instance.contentResolver, ANDROID_ID)
                        )
                        .addHeader("Api-Version", "3.7.0")
                        .build()

                    val resp: okhttp3.Response?
                    try {
                        resp = chain.proceed(request)
                    } finally {
                        networkCommunication.set(false)
                    }

                    resp!!
                }.build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkCallAdapterFactory())
        .build()

    val loginService: LoginService = retrofit.create(LoginService::class.java)
    val directoryService: DirectoryService = retrofit.create(DirectoryService::class.java)

}

sealed class NetworkResponse<out S : Any, out E : Any> {

    data class Success<S : Any>(val body: S) :
        NetworkResponse<S, Nothing>()

    data class ApiError<E : Any>(val body: E, val code: Int) :
        NetworkResponse<Nothing, E>()

    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}

class NetworkCallAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<NetworkResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S, E>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }
}

internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.ApiError(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("Not supported")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}

class NetworkCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<out S : Any, out E : Any>>"
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<out S : Any, out E : Any>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return NetworkCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }

}
