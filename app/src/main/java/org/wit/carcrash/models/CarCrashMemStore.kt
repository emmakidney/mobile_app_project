package org.wit.carcrash.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CarCrashMemStore : CarCrashStore {

    val carcrashs = ArrayList<CarCrashModel>()

    override fun findAll(): List<CarCrashModel> {
        return carcrashs
    }

    override fun create(carcrash: CarCrashModel) {
        carcrash.id = getId()
        carcrashs.add(carcrash)
        logAll()
    }

    override fun update(carcrash: CarCrashModel) {
        var foundCarCrash: CarCrashModel? = carcrashs.find { p -> p.id == carcrash.id }
        if (foundCarCrash != null) {
            foundCarCrash.title = carcrash.title
            foundCarCrash.description = carcrash.description
            foundCarCrash.image = carcrash.image
            foundCarCrash.lat = carcrash.lat
            foundCarCrash.lng = carcrash.lng
            foundCarCrash.zoom = carcrash.zoom
            logAll()
        }
    }

    override fun delete(carcrash: CarCrashModel) {
        carcrashs.remove(carcrash)
    }

    private fun logAll() {
        carcrashs.forEach { i("$it") }
    }
}