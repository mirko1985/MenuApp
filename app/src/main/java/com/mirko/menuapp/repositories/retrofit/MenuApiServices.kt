package com.mirko.menuapp.repositories.retrofit

import com.mirko.menuapp.data.directory.DirectorySearchRequest
import com.mirko.menuapp.data.directory.DirectorySearchResponse
import com.mirko.menuapp.data.login.LoginErrorResponse
import com.mirko.menuapp.data.login.LoginRequest
import com.mirko.menuapp.data.login.LoginResponse
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.POST

public interface LoginService {
    @POST("api/customers/login")
    suspend fun login(@Body loginBody: LoginRequest) : NetworkResponse<LoginResponse, LoginErrorResponse>
}

public interface DirectoryService {
    @POST("api/directory/search")
    suspend fun venuesList(@Body directorySearchBody: DirectorySearchRequest) : NetworkResponse<DirectorySearchResponse, Any>
}