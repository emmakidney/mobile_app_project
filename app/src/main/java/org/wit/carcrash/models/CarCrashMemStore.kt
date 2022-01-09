package org.wit.carcrash.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CarCrashMemStore : CarCrashStore {

    val carcrashs = ArrayList<CarCrashModel>()

    override suspend fun findAll(): List<CarCrashModel> {
        return carcrashs
    }

    override suspend fun create(carcrash: CarCrashModel) {
        carcrash.id = getId()
        carcrashs.add(carcrash)
        logAll()
    }

    override suspend fun update(carcrash: CarCrashModel) {
        val foundCarCrash: CarCrashModel? = carcrashs.find { p -> p.id == carcrash.id }
        if (foundCarCrash != null) {
            foundCarCrash.title = carcrash.title
            foundCarCrash.description = carcrash.description
            foundCarCrash.image = carcrash.image
            foundCarCrash.location = carcrash.location
            logAll();
        }
    }

    override suspend fun delete(carcrash: CarCrashModel) {
        carcrashs.remove(carcrash)
        logAll()
    }

    override suspend fun findById(id: Long): CarCrashModel? {
        val foundCarCrash: CarCrashModel? = carcrashs.find { it.id == id}
        return foundCarCrash
    }

    private fun logAll() {
        carcrashs.forEach { i("$it") }
    }

    override suspend fun clear(){
        carcrashs.clear()
    }
}