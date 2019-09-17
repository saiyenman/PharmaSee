package com.example.pharma.Services

import com.example.pharma.Endpoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    val endpoint : Endpoint by lazy {
        Retrofit.Builder().baseUrl("https://mypharmacy-app.herokuapp.com/").
            addConverterFactory(GsonConverterFactory.create()).
            build().create(Endpoint::class.java)
    }
}