package com.mobeedev.kajakorg.data.datasource.local.db.path.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.SectionDB

data class PathWithSectionsDB(
    @Embedded val path: PathDB,
    @Relation(
        parentColumn = "pathId",
        entityColumn = "pathId"
    )
    val sections: List<SectionDB>
)