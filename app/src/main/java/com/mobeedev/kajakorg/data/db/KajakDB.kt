package com.mobeedev.kajakorg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobeedev.kajakorg.data.datasource.local.KajakPathDao
import com.mobeedev.kajakorg.data.datasource.local.db.overview.PathOverviewDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDescriptionDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB
import com.mobeedev.kajakorg.data.datasource.local.db.path.SectionDB
import com.mobeedev.kajakorg.data.db.KajakDB.Companion.DATABASE_VERSION


@Database(
    entities = [
        PathOverviewDB::class,
        PathDB::class,
        SectionDB::class,
        EventDB::class,
        EventDescriptionDB::class
    ],
    version = DATABASE_VERSION
)
@TypeConverters(DBTypeConverters::class)
abstract class KajakDB() : RoomDatabase() {

    abstract fun kajakPathDao(): KajakPathDao

    companion object {
        const val DATABASE_NAME = "kajak_db"
        const val DATABASE_VERSION = 1

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
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}