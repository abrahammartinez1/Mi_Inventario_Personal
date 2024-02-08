package com.abraham.mi.inventario.miinventariopersonal.data.BBDD

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity

@Database(entities = [ActiveTypeEntity::class, ActiveEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeTypeDao(): ActiveTypeDao
    abstract fun activeDao(): ActiveDao
}
