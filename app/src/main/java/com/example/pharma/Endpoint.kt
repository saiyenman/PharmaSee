package com.example.pharma

import com.example.pharma.Model.Password
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Endpoint {
    // Sending the generated password
    @POST("send")
    fun sendPassword(@Body password: Password): Call<String>
}