package com.mobeedev.kajakorg.data.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//todo add http://kajak.org.pl/ as base url to retrofit
interface KajakOrgService {

    @GET("splywww/tabelaszlakow.php")
    suspend fun getPathList():Response<String>

    @GET("splywww/xmlout.php")
    suspend fun getPathXML(@Query("idsz") id:String):Response<String>
}