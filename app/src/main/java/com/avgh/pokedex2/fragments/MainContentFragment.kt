package com.avgh.pokedex2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avgh.pokedex2.R
import com.avgh.pokedex2.models.Pokemon
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_content_fragment_layout.*
import kotlinx.android.synthetic.main.main_content_fragment_layout.view.*

class MainContentFragment: Fragment() {

    var pokemon = Pokemon()

    companion object {
        fun newInstance(pokemon: Pokemon): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.main_content_fragment_layout, container, false)

        bindData(view)

        return view
    }

    fun bindData(view: View){
        view.name_main_content_fragment.text = pokemon.name
        view.type_main_content_fragment.text = pokemon.fsttype
        view.weight_main_content_fragment.text = pokemon.weight
        view.height_main_content_fragment.text = pokemon.height
        Picasso.with(this.context)
                .load(pokemon.sprite)
                .resize((this.resources.displayMetrics.widthPixels/this.resources.displayMetrics.density).toInt(),256)
                .centerCrop()
                .error(R.drawable.ic_pokemon_go)
                .into(view.image_main_content_fragment)
    }

}