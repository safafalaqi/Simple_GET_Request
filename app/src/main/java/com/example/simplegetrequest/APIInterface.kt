package com.example.simplegetrequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {

    @GET("custom-people/")
  fun getUsersInfo(): Call<People?>?

    @POST("custom-people/")
    fun addUsersInfo(@Body requestBody: PersonListItem?): Call<PersonListItem>

}
