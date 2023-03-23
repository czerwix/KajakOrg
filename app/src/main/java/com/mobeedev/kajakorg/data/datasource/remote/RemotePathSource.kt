package com.mobeedev.kajakorg.data.datasource.remote

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobeedev.kajakorg.data.bodyOrException
import kotlinx.coroutines.tasks.await
import org.jsoup.Jsoup

class RemotePathSource(private val kajakOrgService: KajakOrgService) {

    //suspend fun getPathsList() = kajakOrgService.getPathList().bodyOrException()
    //todo when i have more time figure out retrofit approach.
    suspend fun getPathsList() =
        Jsoup.connect(Firebase.storage.reference.child("path/tabelaszlakow.html").downloadUrl.await().toString())
            .get() //todo remove the time machine usage here

    suspend fun getPath(id: Int) = kajakOrgService.getPathXML(Firebase.storage.reference.child("path/szlak$id.xml").downloadUrl.await().toString()).bodyOrException()

//    suspend fun getFirebasePathTable() {
//        val storageRef = Firebase.storage.reference.child("/path/tabelaszlakow.html")
//    }
}
