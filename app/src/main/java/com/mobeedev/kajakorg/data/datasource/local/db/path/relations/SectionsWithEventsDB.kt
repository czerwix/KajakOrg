package com.mobeedev.kajakorg.data.datasource.local.db.path.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.SectionDB


data class SectionsWithEventsDB(
    @Embedded val sectionDB: SectionDB,
    @Relation(
        parentColumn = "sectionId",
        entityColumn = "sectionId"
    )
    val events:List<EventDB>
)