package com.androidcourse.laboratorio10_ejercicio.data

import com.androidcourse.laboratorio10_ejercicio.Model.ProductModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun selectProducts():ArrayList<ProductModel>

    @GET("products/{id}")
    suspend fun selectProduct(@Path("id") id:String): Response<ProductModel>

    @Headers("Content-Type: application/json")
    @POST("products")
    suspend fun insertProduct(@Body serie: ProductModel): Response<ProductModel>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id:String, @Body serie: ProductModel): Response<ProductModel>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id:String): Response<ProductModel>
}

