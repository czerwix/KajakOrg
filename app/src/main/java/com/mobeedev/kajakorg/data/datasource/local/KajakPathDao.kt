package com.mobeedev.kajakorg.data.datasource.local

import androidx.room.*
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.db.PathMapDetailScreenState
import com.mobeedev.kajakorg.data.db.overview.PathOverviewDB
import com.mobeedev.kajakorg.data.db.path.EventDB
import com.mobeedev.kajakorg.data.db.path.EventDescriptionDB
import com.mobeedev.kajakorg.data.db.path.PathDB
import com.mobeedev.kajakorg.data.db.path.SectionDB
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.detail.toDB
import com.mobeedev.kajakorg.data.model.detail.toPathDB
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.detail.Section
import com.mobeedev.kajakorg.domain.model.detail.toDomain
import com.mobeedev.kajakorg.domain.model.overview.toItem
import com.mobeedev.kajakorg.ui.model.EventMapItem
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.model.PathOverviewItem

@Dao
abstract class KajakPathDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(pathDetailScreenState: PathMapDetailScreenState)
    @Query("SELECT * FROM PathMapDetailScreenState WHERE pathId = :id")
    abstract fun getPathDetailScreenState(id: Int): PathMapDetailScreenState

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllPathsOverview(pathOverviews: List<PathOverviewDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllPaths(paths: List<PathDB>)

    //insertPathHelpers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(path: PathDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(section: SectionDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(event: EventDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(eventDescriptionDB: EventDescriptionDB)

    @Transaction
    open suspend fun insert(path: PathDto) {
        insert(path.toDB())
        path.sections?.forEach { sectionDto ->
            insert(sectionDto.toDB(path.id))
            sectionDto.events.forEach { eventDto ->
                insert(eventDto.toDB(sectionDto.id))
                eventDto.eventDescription.forEach { eventDescriptionDto ->
                    insert(eventDescriptionDto.toDB(eventDto.id))
                }
            }
        }
        path.events?.forEach { eventDto ->
            insert(eventDto.toPathDB(path.id))
            eventDto.eventDescription.forEach { eventDescriptionDto ->
                insert(eventDescriptionDto.toDB(eventDto.id))
            }
        }
    }

    //GET
    @Query("SELECT * FROM PathOverviewDB ORDER BY pathOverviewId")
    abstract fun getAllPathsOverview(): List<PathOverviewDB>

    @Query("SELECT * FROM PathOverviewDB WHERE pathOverviewId=:id")
    abstract fun getPathOverview(id: Int): PathOverviewDB

    @Query("SELECT pathId FROM PathDB ORDER BY pathId")
    abstract fun getAllPathsIds(): List<Int>

    //getPathHelpers
    @Query("SELECT * FROM PathDB WHERE pathId=:id")
    abstract fun getPath(id: Int): PathDB

    @Query("SELECT description FROM PathDB WHERE pathId=:id")
    abstract fun getPathDescription(id: Int): String?

    @Query("SELECT * FROM SectionDB WHERE pathId=:pathId ORDER BY sortOrder")
    abstract fun getSectionByPath(pathId: Int): List<SectionDB>

    @Query("SELECT * FROM EventDB WHERE pathId=:pathId ORDER BY sortOrder")
    abstract fun getEventByPath(pathId: Int): List<EventDB>

    @Query("SELECT * FROM EventDB WHERE sectionId=:sectionId ORDER BY sortOrder")
    abstract fun getEventBySection(sectionId: Int): List<EventDB>

    @Query("SELECT * FROM EventDescriptionDB WHERE eventId=:eventId ORDER BY sortOrder")
    abstract fun getEventDescriptionByEvent(eventId: Int): List<EventDescriptionDB>


    //todo use CrossRef after MVP
    @Transaction
    open suspend fun getPathDomain(pathId: Int): Path = getPath(pathId).toDomain(
        getSectionsForPath(pathId),
        getEventsForPath(pathId)
    )

    private fun getSectionsForPath(pathId: Int) = getSectionByPath(pathId).sortedBy { it.sortOrder }.map { section ->
        section.toDomain(getEventBySection(section.sectionId).map { event ->
            event.toDomain(getEventDescriptionByEvent(event.eventId).map { it.toDomain() })
        })
    }

    private fun getEventsForPath(pathId: Int) = getEventByPath(pathId).map { event ->
        event.toDomain(getEventDescriptionByEvent(event.eventId).map { it.toDomain() })
    }

    @Transaction
    open suspend fun getPathsOverviewItem(): List<PathOverviewItem> =
        mutableListOf<PathOverviewItem>().apply {
            getAllPathsOverview().forEach { path ->
                add(path.toItem(getPathDescription(path.pathOverviewId) ?: String.empty))
            }
        }

    @Transaction
    open suspend fun getPathOverviewItem(pathId: Int) =
        getPathOverview(pathId).toItem(getPathDescription(pathId) ?: String.empty)

    @Transaction
    open suspend fun getAllPathMapData(): List<PathMapItem> =
        mutableListOf<PathMapItem>().apply {
            getAllPathsOverview().forEach { path ->
                add(
                    PathMapItem(
                        overview = path.toItem(
                            getPathDescription(path.pathOverviewId) ?: String.empty
                        ),
                        eventList = getEventMapByPathSorted(path.pathOverviewId)
                    )
                )
            }
        }

    @Transaction
    open suspend fun getPathMapData(pathId: Int): PathMapItem = with(getPathOverview(pathId)) {
        PathMapItem(this.toItem(String.empty), getEventMapByPathSorted(pathId))
    }


    private fun getEventMapByPathSorted(pathOverviewId: Int): List<EventMapItem> {
        val pathSectionEvents =
            (getSectionByPath(pathOverviewId).map { it.toDomain(mutableListOf()) } +
                    getEventByPath(pathOverviewId).map { it.toDomain(mutableListOf()) })
                .sortedBy { it.sortOrder }
        val sortedPathItems = mutableListOf<EventMapItem>()

        pathSectionEvents.forEach { pathEvent ->
            when (pathEvent) {
                is Section -> {
                    sortedPathItems.addAll(getEventBySection(pathEvent.id).map {
                        EventMapItem(
                            it.eventId,
                            it.position,
                            it.atKilometer,
                            it.label,
                            it.sortOrder
                        )
                    })
                }
                is Event -> {
                    sortedPathItems.add(
                        EventMapItem(
                            pathEvent.id,
                            pathEvent.position,
                            pathEvent.atKilometer,
                            pathEvent.label,
                            pathEvent.sortOrder
                        )
                    )
                }
            }
        }
        return sortedPathItems
    }
}