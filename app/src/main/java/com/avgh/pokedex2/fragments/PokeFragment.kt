package com.avgh.pokedex2.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.avgh.pokedex2.AppConstants
import com.avgh.pokedex2.MyPokeAdapter
import com.avgh.pokedex2.R
import com.avgh.pokedex2.activities.pokeViewer
import com.avgh.pokedex2.adapters.PokemonAdapter
import com.avgh.pokedex2.models.Pokemon
import com.avgh.pokedex2.utilities.NetworkUtilities
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poke_list_fragment.*
import kotlinx.android.synthetic.main.poke_list_fragment.view.*
import org.json.JSONObject
import java.io.IOException

class PokeFragment: Fragment() {

    private lateinit var  pokemonLisFrag: ArrayList<Pokemon>

    private lateinit var pokeAdapter: MyPokeAdapter

    var listenerTool :  SearchNewPokemonListener? = null

    companion object {
        fun newInstance(dataset : ArrayList<Pokemon>): PokeFragment{
            val newFragment = PokeFragment()
            newFragment.pokemonLisFrag = dataset
            return newFragment
        }
    }

    interface SearchNewPokemonListener{
        fun searchPokemon(pokeName: String)

        fun managePortraitItemClick(pokemon: Pokemon)

        fun manageLandscapeItemClick(pokemon: Pokemon)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =inflater.inflate(R.layout.poke_list_fragment, container, false)

        if(savedInstanceState != null) pokemonLisFrag = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_ArrayList_KEY)!!

        initRecyclerView(pokemonLisFrag,resources.configuration.orientation, view)
        initSearchButton(view)
        initClearButton(view)

        return view
    }
    fun initRecycler(pokemon: ArrayList<Pokemon>){
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            pokeAdapter = PokemonAdapter(pokemon, { pokemon: Pokemon -> pokemonItemClicked(pokemon) })
        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokeAdapter = PokemonAdapter(pokemon, { pokemon: Pokemon -> listenerTool?.manageLandscapeItemClick(pokemon) })
        }
    }

    fun initRecyclerView(pokemon: ArrayList<Pokemon>,orientation:Int, container:View){
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            initRecycler(pokemon)
            container.rv_pokemon_list.adapter = pokeAdapter as PokemonAdapter
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            initRecycler(pokemon)
            container.rv_pokemon_list.adapter = pokeAdapter as PokemonAdapter
        }

        container.rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    fun initSearchButton(container:View) = container.searchbarbutton.setOnClickListener {
        if (!container.searchbar.text.isEmpty()){
            QueryPokemonTask().execute("${searchbar.text}")
        }
    }
    fun initClearButton(container: View) = container.searchbarclearbutton.setOnClickListener{
        searchbar.setText("")
        FetchPokemonTask().execute("")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchNewPokemonListener) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_ArrayList_KEY, pokemonLisFrag)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }


    private fun pokemonItemClicked(item: Pokemon){
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            startActivity(Intent(this.context, pokeViewer::class.java).putExtra("CLAVIER", item.url))
        }

    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg query: String): String{
            if (query.isNullOrEmpty()) return ""

            val ID = query[0]
            val pokeAPI = NetworkUtilities().buildUrl("pokemon",ID)

            return try {
                NetworkUtilities().getResponseFromHttpUrl(pokeAPI)
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }
        override fun onPostExecute(pokemonInfo: String) {
            if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("results")
                for(i in 0..15){
                    val result = JSONObject(results[i].toString())
                    val pokemon = Pokemon(i,
                        result.getString("name").capitalize(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        result.getString("url"),
                        R.string.n_a_value.toString())
                    pokemonLisFrag.add(pokemon)
                }
            } else {
                for(i in 0..15){
                    val pokeVacio = Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                    pokemonLisFrag.add(pokeVacio)
                }
            }
            initRecycler(pokemonLisFrag)
        }

    }

    private inner class QueryPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg query: String): String {

            if (query.isNullOrEmpty()) return ""

            val ID = query[0]
            val pokeAPI = NetworkUtilities().buildUrl("type", ID)

            return try {
                NetworkUtilities().getResponseFromHttpUrl(pokeAPI)
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }
        override fun onPostExecute(pokemonInfo: String) {
            pokemonLisFrag.clear()
            if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("pokemon")
                Log.d("LIsta", results.toString())
                for(i in 0..15) {
                    val resulty = JSONObject(results[i].toString())
                    val result = JSONObject(resulty.getString("pokemon"))

                    val pokemon = Pokemon(i,
                            result.getString("name").capitalize(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            result.getString("url"),
                            R.string.n_a_value.toString())

                    pokemonLisFrag.add(pokemon)
                }
            } else {
                for(i in 0..15){
                    val pokeVacio = Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                    pokemonLisFrag.add(pokeVacio)
                }
            }
            pokeAdapter.changeDataSet(pokemonLisFrag)
        }
    }

}