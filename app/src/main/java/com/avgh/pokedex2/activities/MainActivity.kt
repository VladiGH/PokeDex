package com.avgh.pokedex2.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.avgh.pokedex2.AppConstants
import com.avgh.pokedex2.adapters.PokemonAdapter
import com.avgh.pokedex2.R
import com.avgh.pokedex2.fragments.MainContentFragment
import com.avgh.pokedex2.fragments.PokeFragment
import com.avgh.pokedex2.fragments.PokeFragment.Companion.newInstance
import com.avgh.pokedex2.models.Pokemon
import com.avgh.pokedex2.utilities.NetworkUtilities
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poke_list_fragment.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), PokeFragment.SearchNewPokemonListener {
    private lateinit var mainFragment : PokeFragment
    private lateinit var mainContentFragment: MainContentFragment

    private var pokeList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokeList = savedInstanceState?.getParcelableArrayList(AppConstants.saveinstance_key) ?: ArrayList()
        initMainFragment()


    }

    fun initMainFragment(){
        FetchPokemonTask().execute("")
        pokeList.clear()
        mainFragment = PokeFragment.newInstance(pokeList)

        val resource = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            mainContentFragment = MainContentFragment.newInstance(Pokemon())
            changeFragment(R.id.fragment_content_right, mainContentFragment)

            R.id.landscape_left
        }

        changeFragment(resource, mainFragment)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.saveinstance_key, pokeList)
        super.onSaveInstanceState(outState)
    }

    override fun searchPokemon(pokeName: String) {
        QueryPokemonTask().execute(pokeName)
    }

    override fun managePortraitItemClick(pokemon: Pokemon) {
        val pokeBundle = Bundle()
        pokeBundle.putParcelable("POKEMON", pokemon)
        startActivity(Intent(this, pokeViewer::class.java).putExtras(pokeBundle))
    }

    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }

    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.fragment_content_right, mainContentFragment)
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
            if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("pokemon")
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

                    pokeList.add(pokemon)
                }
            } else {
                pokeList.clear()
                for(i in 1..15){
                    val pokeVacio = Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                    pokeList.add(pokeVacio)
                }
            }
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
                    pokeList.add(pokemon)
                }
            } else {
                for(i in 1..15){
                    val pokeVacio = Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                    pokeList.add(pokeVacio)
                }
            }
        }

    }


}
