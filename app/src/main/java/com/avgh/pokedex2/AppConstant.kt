package com.avgh.pokedex2

import com.avgh.pokedex2.models.Pokemon

object AppConstants {
    val saveinstance_key = "CLAVE"
    val MAIN_ArrayList_KEY = "key_arrayList"
}

interface MyPokeAdapter {
    fun changeDataSet(newDataSet : ArrayList<Pokemon>)
}