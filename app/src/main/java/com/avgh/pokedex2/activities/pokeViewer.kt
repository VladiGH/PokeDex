package com.avgh.pokedex2.activities

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.avgh.pokedex2.R
import com.avgh.pokedex2.models.Pokemon
import com.avgh.pokedex2.utilities.NetworkUtilities
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_content_fragment_layout.*
import kotlinx.android.synthetic.main.pokemon_element.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class pokeViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_content_fragment_layout)

        val uri:String = this.intent.extras.getString("CLAVIER")
        setSupportActionBar(toolbarviewer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
     //   collapsingtoolbarviewer.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
     //   collapsingtoolbarviewer.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        FetchPokemonTask().execute(uri)
    }

    fun init(pokemon: Pokemon){
        Picasso.with(this)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png")
            .resize((this.resources.displayMetrics.widthPixels/this.resources.displayMetrics.density).toInt(),256)
            .centerCrop()
            .error(R.drawable.ic_pokemon_go)
            .into(image_main_content_fragment)
        name_main_content_fragment.text = pokemon.name
        weight_main_content_fragment.text = pokemon.weight
        height_main_content_fragment.text = pokemon.height
        type_main_content_fragment.text = pokemon.fsttype
        //sndType.text = pokemon.sndtype
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            android.R.id.home -> {onBackPressed();true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg query: String): String {

            if (query.isNullOrEmpty()) return ""

            val url = query[0]
            val pokeAPI = Uri.parse(url).buildUpon().build()
            val finalurl = try {
                URL(pokeAPI.toString())
            } catch (e: MalformedURLException){
                URL("")
            }

            return try {
                NetworkUtilities().getResponseFromHttpUrl(finalurl)
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }

        }

        override fun onPostExecute(pokemonInfo: String) {
            val pokemon = if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val sprites = root.getString("sprites")
                val types = root.getJSONArray("types")
                val fsttype = JSONObject(types[0].toString()).getString("type")
                val sndtype = try { JSONObject(types[1].toString()).getString("type") }catch (e: JSONException){ "" }

                Pokemon(root.getInt("id"),
                    root.getString("name").capitalize(),
                    JSONObject(fsttype).getString("name").capitalize(),
                    if(sndtype.isEmpty()) " " else JSONObject(sndtype).getString("name").capitalize(),
                    root.getString("weight"), root.getString("height"), root.getString("location_area_encounters"),
                    JSONObject(sprites).getString("front_default"))

            } else {
                Pokemon(0, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
            }
            init(pokemon)
        }
    }
}