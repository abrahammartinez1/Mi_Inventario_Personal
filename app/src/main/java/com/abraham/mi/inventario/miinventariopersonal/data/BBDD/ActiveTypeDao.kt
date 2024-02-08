package com.abraham.mi.inventario.miinventariopersonal.data.BBDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity

@Dao
interface ActiveTypeDao {
    @Insert
    fun insertActiveType(activeType: ActiveTypeEntity)

    @Query("SELECT * FROM active_types")
    fun getActiveTypes(): List<ActiveTypeEntity>
}