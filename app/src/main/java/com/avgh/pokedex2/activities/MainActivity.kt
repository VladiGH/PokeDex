package com.avgh.pokedex2.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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

//TODO(1): Se extiene de la interfaz de PokeFragment para poder hacer override de sus metodos
class MainActivity : AppCompatActivity(), PokeFragment.SearchNewPokemonListener {

    //TODO(1.1): se hace lateinit de las variables de esos tipos para poder inicializarlas mas adelante
    private lateinit var mainFragment : PokeFragment
    private lateinit var mainContentFragment: MainContentFragment

    //TODO(1.2): Se hace una variable que contendrá a los pokemon en el Fetch inicial
    private var pokeList = ArrayList<Pokemon>()

    //TODO(1.3): aca se coloca el layout principal que solo contiene un FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO(1.4): al arraylist se le asigna un arraylist si ya había sido creado anteriormente mediante el savedinstancestate
        pokeList = savedInstanceState?.getParcelableArrayList(AppConstants.saveinstance_key) ?: ArrayList()
        initMainFragment()
    }

    //TODO(1.5): se hace override del onSaveInstanceState donde a la variable de tipo bundle le pasamos un arraylist para
    //que se guarde el estado anterior ante un cambio en la actividad
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.saveinstance_key, pokeList)
        super.onSaveInstanceState(outState)
    }

    //TODO(1.6): Esta funcion es para poder hacer la transaccion para reemplazar el fragmento segun la orientacion
    //el primer parametro es el id de la view que contendrá al fragmento, y el segundo parametro es el fragmento
    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }

    fun initMainFragment(){
       FetchPokemonTask().execute("")
        //TODO(1.7): Se hace una nueva instancia del fragmento pasando la lista que se llena en el main
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


    override fun searchPokemon(pokeName: String) {
    }

    //TODO(1.8): esta funcion es para hacer el intent al hacer click al pokemon en el recycler y así mandar el intent
    //a la segunda actividad
    override fun managePortraitItemClick(pokemon: Pokemon) {
        val pokeBundle = Bundle()
        pokeBundle.putParcelable("POKEMON", pokemon)
        startActivity(Intent(this, pokeViewer::class.java).putExtras(pokeBundle))
    }


    //TODO(1.9): esta funcion es para enviar al info del pokemon clickeado en el recycler y enviarlo al fragmento derechp contenedor
    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.fragment_content_right, mainContentFragment)
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
                for(i in 0..15){
                    val pokeVacio = Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                    pokeList.add(pokeVacio)
                }
            }
        }

    }


}
