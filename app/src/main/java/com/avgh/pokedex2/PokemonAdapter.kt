package com.avgh.pokedex2


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.avgh.pokedex2.models.Pokemon
import kotlinx.android.synthetic.main.list_element_pokemon.view.*


class PokemonAdapter(val items: List<Pokemon>, val clickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.list_element_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with (itemView){
            tv_pokemon_id.text = item.id.toString()
            tv_pokemon_name.text = item.name
            tv_pokemon_type.text = item.url
            this.setOnClickListener { clickListener(item) }
        }
    }



}