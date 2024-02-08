package com.abraham.mi.inventario.miinventariopersonal.data.BBDD

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity

@Dao
interface ActiveDao {
    @Insert
    fun insertActive(active: ActiveEntity)

    @Query("SELECT * FROM actives")
    fun getActives(): List<ActiveEntity>

    @Query("SELECT * FROM actives WHERE id = :id")
    fun getActiveById(id: Long): ActiveEntity?

    @Delete
    fun removeActive(active: ActiveEntity)

    @Query("DELETE FROM actives WHERE id = :id")
    fun removeActiveById(id: Long)

    @Query(
        "UPDATE actives " +
                "SET favorite = :favorite, " +
                "price = :price, " +
                "active = :active, " +
                "description = :description, " +
                "image = :image " +
                "WHERE id = :id"
    )
    fun updateActive(
        id: Long,
        favorite: Boolean,
        price: Double,
        active: Boolean,
        description: String,
        image: ByteArray?
    )

}