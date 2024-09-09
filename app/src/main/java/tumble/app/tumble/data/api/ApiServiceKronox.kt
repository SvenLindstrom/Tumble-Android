package tumble.app.tumble.datasource.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NetworkResponse.KronoxUserBookingElement
import tumble.app.tumble.domain.models.network.NewsItems

interface ApiServiceKronox {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
    )
    @GET("/api/misc/news")
    fun getNews(): Call<NewsItems>

    @GET()
    fun getProgramme(@Url endpoint: String): Call<NetworkResponse.Search>

    @GET()
    fun getSchedule(@Url endpoint: String): Call<NetworkResponse.Schedule>

    fun put(toString: String, token: String?, body: RequestBody): Response<NetworkResponse>

    @GET()
    fun getKronoxCompleteUserEvent(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<NetworkResponse.KronoxCompleteUserEvent>

    @GET()
    fun getKronoxUserBookings(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<List<KronoxUserBookingElement>>

    @GET
    fun getAllResources(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<List<NetworkResponse.KronoxResourceElement>>

    @GET()
    suspend fun <T> get(
        @Url url: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?,
    ): Call<T>

}
