package com.mobeedev.kajakorg.data.datasource.remote


import com.mobeedev.kajakorg.data.model.detail.PathEnvelope
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

//todo add http://kajak.org.pl/ as base url to retrofit
interface KajakOrgService {

//    @Headers(
//        "Cookie: PHPSESSID=8lutjjbb8djpm9klg1pom5qm97",
//        "Accept-Encoding: gzip, deflate",
//        "Accept: text/html",
//        "Host: kajak.org.pl"
//    )
//    @GET("splywww/tabelaszlakow.php")
//    suspend fun getPathList(): Response<String> //todo in teh future export the html szlakiwww to json data on server side

    @GET()
    suspend fun getPathXML(@Url url: String): Response<PathEnvelope>
}