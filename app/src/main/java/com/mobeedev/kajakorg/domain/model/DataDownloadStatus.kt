package com.mobeedev.kajakorg.domain.model

import java.time.ZonedDateTime

data class DataDownloadStatus(
    val lastUpdateDate: ZonedDateTime? = null,
    val status: DataDownloadState = DataDownloadState.EMPTY
)


