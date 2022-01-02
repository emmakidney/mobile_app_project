package org.wit.carcrash.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.carcrash.helpers.Converters
import org.wit.carcrash.models.CarCrashModel

@Database(entities = arrayOf(CarCrashModel::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun carcrashDao(): CarCrashDao
}