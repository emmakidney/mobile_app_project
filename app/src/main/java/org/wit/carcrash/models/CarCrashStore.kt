package org.wit.carcrash.models

interface CarCrashStore {
    fun findAll(): List<CarCrashModel>
    fun create(carcrash: CarCrashModel)
    suspend fun update(carcrash: CarCrashModel)
    fun delete(carcrash: CarCrashModel)
    fun findById(id:Long) : CarCrashModel?
}