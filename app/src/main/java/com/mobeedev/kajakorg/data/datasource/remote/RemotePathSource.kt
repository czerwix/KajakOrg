package com.mobeedev.kajakorg.data.datasource.remote

import com.mobeedev.kajakorg.data.bodyOrException
import org.jsoup.Jsoup

class RemotePathSource(private val kajakOrgService: KajakOrgService) {

    //suspend fun getPathsList() = kajakOrgService.getPathList().bodyOrException()
     //todo when i have more time figure out retrofit approach.
    suspend fun getPathsList() =
        Jsoup.connect("http://kajak.org.pl/splywww/tabelaszlakow.php").get()

    suspend fun getPath(id: Int) = kajakOrgService.getPathXML(id.toString()).bodyOrException()
}
