package org.wit.carcrash.room

import androidx.room.*
import org.wit.carcrash.models.CarCrashModel

@Dao
interface CarCrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(carcrash: CarCrashModel)

    @Query("SELECT * FROM CarCrashModel")
    suspend fun findAll(): List<CarCrashModel>

    @Query("select * from CarCrashModel where id = :id")
    suspend fun findById(id: Long): CarCrashModel

    @Update
    suspend fun update(carcrash: CarCrashModel)

    @Delete
    suspend fun deleteCarCrash(carcrash: CarCrashModel)
}