package com.mobeedev.kajakorg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mobeedev.kajakorg.data.datasource.local.ChecklistDao
import com.mobeedev.kajakorg.data.datasource.local.KajakPathDao
import com.mobeedev.kajakorg.data.db.KajakDB.Companion.DATABASE_VERSION
import com.mobeedev.kajakorg.data.db.overview.PathOverviewDB
import com.mobeedev.kajakorg.data.db.path.EventDB
import com.mobeedev.kajakorg.data.db.path.EventDescriptionDB
import com.mobeedev.kajakorg.data.db.path.PathDB
import com.mobeedev.kajakorg.data.db.path.SectionDB


@Database(
    entities = [
        PathOverviewDB::class,
        PathDB::class,
        SectionDB::class,
        EventDB::class,
        EventDescriptionDB::class,
        CheckListDB::class,
        PathMapDetailScreenState::class
    ],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(DBTypeConverters::class)
abstract class KajakDB() : RoomDatabase() {

    abstract fun kajakPathDao(): KajakPathDao
    abstract fun checklistDao(): ChecklistDao

    companion object {
        const val DATABASE_NAME = "kajak_db"
        const val DATABASE_VERSION = 3

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `PathMapDetailScreenState` (`pathId` INTEGER NOT NULL, `cameraPositionLat` REAL, `cameraPositionLng` REAL, `cameraPositionZoom` REAL, `cameraPositionTilt` REAL, `cameraPositionBearing` REAL, `currentPage` INTEGER, `bottomLayoutVisibility` INTEGER, PRIMARY KEY(`pathId`))")
            }
        }


        @Volatile
        private var INSTANCE: KajakDB? = null

        fun getInstance(context: Context): KajakDB {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    //todo change me. only for development purposes
//                    instance = Room.inMemoryDatabaseBuilder(context, KajakDB::class.java)
//                        .fallbackToDestructiveMigration()
//                        .build()

                    instance = Room.databaseBuilder(context, KajakDB::class.java, DATABASE_NAME)
                        .createFromAsset("database/kajak_db.db")
                        .addMigrations(MIGRATION_2_3)
//                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}