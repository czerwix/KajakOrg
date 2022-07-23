package com.mobeedev.kajakorg.data.datasource.remote

import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.detail.TripDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//todo add http://kajak.org.pl/ as base url to retrofit
interface KajakOrgService {

    @Headers(
        "Cookie: PHPSESSID=8lutjjbb8djpm9klg1pom5qm97",
        "Accept-Encoding: gzip, deflate",
        "Accept: text/html",
        "Host: kajak.org.pl"
    )
    @GET("splywww/tabelaszlakow.php")
    suspend fun getPathList(): Response<String>

    @GET("splywww/xmlout.php")
    suspend fun getPathXML(@Query("idsz") id:String):Response<TripDto>
}