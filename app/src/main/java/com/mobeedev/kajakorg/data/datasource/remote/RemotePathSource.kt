package com.mobeedev.kajakorg.data.datasource.remote

import com.mobeedev.kajakorg.data.bodyOrException

class RemotePathSource(private val kajakOrgService: KajakOrgService) {

     suspend fun getPathsList() = kajakOrgService.getPathList().bodyOrException()

     suspend fun getPath(id:String) = kajakOrgService.getPathXML(id).bodyOrException()
}
