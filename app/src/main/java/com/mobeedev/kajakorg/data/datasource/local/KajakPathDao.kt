package com.mobeedev.kajakorg.data.datasource.local

import androidx.room.*
import com.mobeedev.kajakorg.data.datasource.local.db.overview.PathOverviewDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.*
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.detail.toDB

@Dao
abstract class KajakPathDao {
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
        path.sections.forEach { sectionDto ->
            insert(sectionDto.toDB(path.id))
            sectionDto.events.forEach { eventDto ->
                insert(eventDto.toDB(sectionDto.id))
                eventDto.eventDescription.forEach { eventDescriptionDto ->
                    insert(eventDescriptionDto.toDB(eventDto.id))
                }
            }
        }
    }

    //GET
    @Query("SELECT * FROM PathOverviewDB")
    abstract fun getAllPathsOverview(): List<PathOverviewDB>

    //getPathHelpers
    @Query("SELECT * FROM PathDB WHERE pathId=:id")
    abstract fun getPath(id: Int): PathDB

    @Query("SELECT * FROM SectionDB WHERE pathId=:pathId")
    abstract fun getSectionByPath(pathId: Int): List<SectionDB>

    @Query("SELECT * FROM EventDB WHERE sectionId=:sectionId")
    abstract fun getEventBySection(sectionId: Int): List<EventDB>

    @Query("SELECT * FROM EventDescriptionDB WHERE eventId=:eventId")
    abstract fun getEventDescriptionByEvent(eventId: Int): List<EventDescriptionDB>

    //get path main transaction todo(Optimise this)
    @Transaction
    open suspend fun getPathDto(pathId: Int): PathDto {
        val path = getPath(pathId).toDto()
        val sectionsForPath = getSectionByPath(pathId).map { it.toDto() }

        sectionsForPath.forEach { section ->
            val eventsForSection = getEventBySection(section.id).map { it.toDto() }
            eventsForSection.forEach { event ->
                val eventDescriptionForEvent =
                    getEventDescriptionByEvent(event.id).map { it.toDto() }
                event.eventDescription.addAll(eventDescriptionForEvent)
            }

            section.events.addAll(eventsForSection.toMutableList())
        }
        return path.apply {
            sections.addAll(sectionsForPath)
        }
    }
}