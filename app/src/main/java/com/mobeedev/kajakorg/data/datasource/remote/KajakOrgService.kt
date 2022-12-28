package com.mobeedev.kajakorg.data.datasource.remote


import com.mobeedev.kajakorg.data.model.detail.PathEnvelope
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

    @GET("web/20220518221922if_/http://www.kajak.org.pl/splywww/xmlout.php")
    suspend fun getPathXML(@Query("idsz") id: String): Response<PathEnvelope>
}