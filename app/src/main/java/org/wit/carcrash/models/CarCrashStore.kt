package org.wit.carcrash.models

interface CarCrashStore {
    suspend fun findAll(): List<CarCrashModel>
    suspend fun create(carcrash: CarCrashModel)
    suspend fun update(carcrash: CarCrashModel)
    suspend fun delete(carcrash: CarCrashModel)
    suspend fun findById(id:Long) : CarCrashModel?
    suspend fun clear()
}