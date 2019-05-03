package com.avgh.pokedex2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avgh.pokedex2.R
import com.avgh.pokedex2.models.Pokemon
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_content_fragment_layout.view.*

class MainContentFragment: Fragment() {

    var pokemon = Pokemon()


    //TODO(1): Se hace un companion object para que se pueda hacer una nueva instancia por cada pokemon que se pasará
    //al fragmento de la derecha con su info
    companion object {
        fun newInstance(pokemon: Pokemon): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    //TODO(1.1):Acá se infla la vista que se mostrará en el fragmento de la izquierda
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.main_content_fragment_layout, container, false)
        //TODO(1.2): acá se le pasa la vista para poder bindear la info de cada pokemon
        bindData(view)

        return view
    }

    //TODO(1.2): esta funcion sirve para asignarle a cada campo de la view la info del pokemon
    fun bindData(view: View){
        view.name_main_content_fragment.text = pokemon.name
        view.type_main_content_fragment.text = pokemon.fsttype
        view.weight_main_content_fragment.text = pokemon.weight
        view.height_main_content_fragment.text = pokemon.height
        Picasso.with(this.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id+1}.png")
                .resize((this.resources.displayMetrics.widthPixels/this.resources.displayMetrics.density).toInt(),256)
                .centerCrop()
                .error(R.drawable.ic_pokemon_go)
                .into(view.image_main_content_fragment)
    }

}