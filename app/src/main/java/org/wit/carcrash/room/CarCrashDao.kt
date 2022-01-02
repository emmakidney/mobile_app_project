package org.wit.carcrash.room

import androidx.room.*
import org.wit.carcrash.models.CarCrashModel

@Dao
interface CarCrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(carcrash: CarCrashModel)

    @Query("SELECT * FROM CarCrashModel")
    fun findAll(): List<CarCrashModel>

    @Query("select * from CarCrashModel where id = :id")
    fun findById(id: Long): CarCrashModel

    @Update
    fun update(carcrash: CarCrashModel)

    @Delete
    fun deleteCarCrash(carcrash: CarCrashModel)
}