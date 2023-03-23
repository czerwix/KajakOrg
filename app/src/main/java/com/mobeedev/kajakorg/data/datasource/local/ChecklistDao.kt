package com.mobeedev.kajakorg.data.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mobeedev.kajakorg.data.db.CheckListDB

@Dao
interface ChecklistDao {

    @Upsert
    fun upsertAllChecklistDB(list: List<CheckListDB>)

    @Upsert
    fun upsertChecklistDB(checklist: CheckListDB)

    @Query("SELECT * FROM CheckListDB")
    fun getALlChecklists(): List<CheckListDB>

}