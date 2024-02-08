package com.abraham.mi.inventario.miinventariopersonal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actives")
data class ActiveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "type_id")
    val typeId: Long,
    val favorite: Boolean,
    val active: Boolean,
    val price: Double,
    val description: String,
    val image: ByteArray?
)
