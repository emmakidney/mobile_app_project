package org.wit.carcrash.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.carcrash.models.CarCrashModel

@Database(entities = arrayOf(CarCrashModel::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun carcrashDao(): CarCrashDao
}