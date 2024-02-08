package com.abraham.mi.inventario.miinventariopersonal.data.BBDD

interface DatabaseCallback<T> {
    fun onResult(result: T)
}