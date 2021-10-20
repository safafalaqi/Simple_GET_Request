package com.example.simplegetrequest


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonListItem(
    @SerializedName("name")
    val name: String?
):Serializable