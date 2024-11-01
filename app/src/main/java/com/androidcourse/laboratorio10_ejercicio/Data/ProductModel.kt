package com.androidcourse.laboratorio10_ejercicio.Data

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("id") var id:Int,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("Descripcion") var descricion:String,
    @SerializedName("precio") var precio:Int,
    @SerializedName("categoria") var categoria:String
)
