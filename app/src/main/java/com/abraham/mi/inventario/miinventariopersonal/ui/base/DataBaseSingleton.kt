package com.abraham.mi.inventario.miinventariopersonal.ui.base

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.AppDatabase
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.DatabaseCallback
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity

object DatabaseSingleton {
    private var instance: AppDatabase? = null

    private fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): AppDatabase {
        val database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        if (database.activeTypeDao().getActiveTypes().isEmpty()) {
            val types = listOf(
                ActiveTypeEntity(name = "Consolas"),
                ActiveTypeEntity(name = "Videojuegos"),
                ActiveTypeEntity(name = "Películas VHS"),
                ActiveTypeEntity(name = "Muñecas"),
                ActiveTypeEntity(name = "Bolsos"),
                ActiveTypeEntity(name = "Zapatillas"),
                ActiveTypeEntity(name = "Libros")
            )

            types.forEach {
                insertTypeAsync(context, it, object : DatabaseCallback<Unit> {
                    override fun onResult(result: Unit) {

                    }
                })
            }
        }

        return database
    }

    fun getTypesAsync(context: Context, callback: DatabaseCallback<List<ActiveTypeEntity>>) {
        AsyncTask.execute {
            val types = getDatabase(context).activeTypeDao().getActiveTypes()
            callback.onResult(types)
        }
    }

    fun getActivesAsync(context: Context, callback: DatabaseCallback<List<ActiveEntity>>) {
        AsyncTask.execute {
            val actives = getDatabase(context).activeDao().getActives()
            callback.onResult(actives)
        }
    }

    fun getActiveByIdAsync(
        context: Context,
        activeId: Long,
        callback: DatabaseCallback<ActiveEntity?>
    ) {
        AsyncTask.execute {
            val active = getDatabase(context).activeDao().getActiveById(activeId)
            callback.onResult(active)
        }
    }

    fun removeActiveByIdAsync(context: Context, activeId: Long, callback: DatabaseCallback<Unit>) {
        AsyncTask.execute {
            getDatabase(context).activeDao().removeActiveById(activeId)
            callback.onResult(Unit)
        }
    }

    private fun insertTypeAsync(
        context: Context,
        type: ActiveTypeEntity,
        callback: DatabaseCallback<Unit>
    ) {
        AsyncTask.execute {
            getDatabase(context).activeTypeDao().insertActiveType(type)
            callback.onResult(Unit)
        }
    }

    fun insertActiveAsync(
        context: Context,
        active: ActiveEntity,
        callback: DatabaseCallback<Unit>
    ) {
        AsyncTask.execute {
            getDatabase(context).activeDao().insertActive(active)
            callback.onResult(Unit)
        }
    }

    fun updateActiveAsync(
        context: Context,
        active: ActiveEntity,
        callback: DatabaseCallback<Unit>,
    ) {
        AsyncTask.execute {
            with(active) {
                getDatabase(context).activeDao().updateActive(
                    id,
                    favorite,
                    price,
                    this.active,
                    description,
                    image,
                )
            }
            callback.onResult(Unit)
        }
    }

}
