package com.mobeedev.kajakorg.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mobeedev.kajakorg.data.db.CheckListDB
import java.util.UUID

@Dao
interface ChecklistDao {

    @Upsert
    fun upsertAllChecklistDB(list: List<CheckListDB>)

    @Upsert
    fun upsertChecklistDB(checklist: CheckListDB)

    @Query("SELECT * FROM CheckListDB")
    fun getALlChecklists(): List<CheckListDB>

    @Query("SELECT * FROM CheckListDB WHERE id= :id")
    fun getChecklists(id: UUID): CheckListDB

    @Delete
    fun deleteChecklist(checkListDB: CheckListDB)

    @Transaction
    fun deleteChecklist(id: UUID) = deleteChecklist(getChecklists(id))

}