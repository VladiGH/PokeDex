package com.avgh.pokedex2.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avgh.pokedex2.MyPokeAdapter
import com.avgh.pokedex2.R
import com.avgh.pokedex2.models.Pokemon
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_element_pokemon.view.*


class PokemonAdapter(var items: List<Pokemon>, val clickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder> (), MyPokeAdapter {


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

        var posPlus:Int = position + 1
        Picasso.with(holder.itemView.context)
        //Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${items[position].id+1}.png")
            .error(R.drawable.ic_pokemon_go)
            .into(holder.itemView.tv_pokemon_img)

    }

    override fun changeDataSet(newDataSet: ArrayList<Pokemon>) {
        this.items = newDataSet
        notifyDataSetChanged()
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