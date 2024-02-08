package com.abraham.mi.inventario.miinventariopersonal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_types")
data class ActiveTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)