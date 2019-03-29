package com.avgh.pokedex2

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.avgh.pokedex2.models.Pokemon
import com.avgh.pokedex2.utilities.NetworkUtilities
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    var pokemonList: MutableList<Pokemon> = ArrayList()
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bt_search_pokemon.setOnClickListener {
            val pokemonNumber: String = et_pokemon_number.text.toString().trim()
            if (pokemonNumber.isEmpty()) {
                Snackbar.make(bt_search_pokemon,"Please, enter a ID pokemon",Snackbar.LENGTH_SHORT).show()
            } else {
                FetchPokemonTask().execute(pokemonNumber)
            }
        }
        initRecycler()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String {

        // TODO: Retornar una lista de Pokemons

        override fun doInBackground(vararg pokemonNumber: String?): String{
            if (pokemonNumber.count() == 0) {
                return null
            }

            var IDPoke: String = pokemonNumber[0]!!
            val pokeAPI: URL = NetworkUtilities.buildUrl(IDPoke)!!

            val ListadePokemon = mutableListOf<Pokemon>()
            try {
                val result: String = NetworkUtilities.getResponseFromHttpUrl(pokeAPI)!!
                //val gson = Gson()
                //val pokeObject: Pokemon = gson.fromJson(result, Pokemon::class.java)

                //ListadePokemon.add(pokeObject)
                // TODO : Parsear el String a JSON con gson usando los campos con los campos de POJO y retorno las lista con esos objetos
                return result
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        // TODO: Recibir la lista
        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null || pokemonInfo != "") {
             //   mResultText.setText(pokemonInfo)
            } else {
               // mResultText.setText(getString(R.string.text_not_found_message))
            }


            // TODO: Setear las lista al adaptador del RECYCLER mando a llamar el init recycler recibiendo la lista de objetos
        }
    }

    fun initRecycler() {
        //var pokemon: MutableList<Pokemon> = MutableList(15)
        //{ i -> Pokemon(i, "Name: " + i, "Type: " + i) }

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemonList)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


}
