package com.mobeedev.kajakorg.data.datasource.local.db.path.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDescriptionDB

data class EventWithEventDescriptionDB(
    @Embedded val event: EventDB,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val eventDescriptionDB: List<EventDescriptionDB>
)