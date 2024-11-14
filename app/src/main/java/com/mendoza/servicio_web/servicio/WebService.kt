package com.mendoza.servicio_web.servicio

import com.google.gson.GsonBuilder
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.entidad.idResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object AppConstantes{
    const val BASE_URL = "http://192.168.56.1:3000"
}

interface WebService {
    @GET("/items")
    suspend fun obtenerItems():Response<UsuariosResponse>

    @POST("/setUserId")
    suspend fun setUserId(@Body userIdRequest: UserIdRequest): Response<GenericResponse>

    @POST("/items/agregar")
    suspend fun agregarItem(@Body item: Item):Response<Response<String>>

    @GET("/items/{id}")
    suspend fun buscarItem(@Path("id") id:Int):Response<Item>

    @PUT("/items/actualizar/{id}")
    suspend fun actualizarItem(@Path("id") id:Int,@Body item: Item):Response<String>

    @DELETE("/items/eliminar/{id}")
    suspend fun eliminarItem(@Path("id") id:Int):Response<String>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Nuevo método para obtener el id del usuario
    @GET("/login/id/{username}")
    suspend fun obtenerIdPorUsername(@Path("username") username: String): Response<idResponse>

    @GET("/kardex")
    suspend fun obtenerKardex():Response<kardexResponse>
}

object RetrofitClient{
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}