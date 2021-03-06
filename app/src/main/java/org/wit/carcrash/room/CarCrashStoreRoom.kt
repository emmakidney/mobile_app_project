package org.wit.carcrash.room

import android.content.Context
import androidx.room.Room
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.models.CarCrashStore

class CarCrashStoreRoom(val context: Context) : CarCrashStore {

    var dao: CarCrashDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.carcrashDao()
    }

    override suspend fun findAll(): List<CarCrashModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): CarCrashModel? {
        return dao.findById(id)
    }

    override suspend fun create(placemark: CarCrashModel) {
        dao.create(placemark)
    }

    override suspend fun update(placemark: CarCrashModel) {
        dao.update(placemark)
    }

    override suspend fun delete(placemark: CarCrashModel) {
        dao.deleteCarCrash(placemark)
    }

    override suspend fun clear() {
    }
}