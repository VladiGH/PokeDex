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

    //TODO(1):en el create onCreateViewHolder vamos a inflar la vista(XML cardview) de los elementos individuales que contienen la info del recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_element_pokemon, parent, false)
        return ViewHolder(view)
    }

    //TODO(1.2): Lo unico que hace es devolver el total de items que estan en el adapter
    override fun getItemCount(): Int {
        return items.size
    }

    //TODO(1.3): Esto lo llama el RecyclerV para mostrar la data en la posicion. Acá se une el viewHolder con su data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)

        var posPlus:Int = position + 1
        Picasso.with(holder.itemView.context)
            //Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${items[position].id+1}.png")
            .error(R.drawable.ic_pokemon_go)
            .into(holder.itemView.tv_pokemon_img)

    }

    //TODO(1.4): changeDataSet es una interfaz
    override fun changeDataSet(newDataSet: ArrayList<Pokemon>) {
        this.items = newDataSet
        //TODO(1.5): este metodo sirve para que LayoutManagers will be forced to fully rebind and relayout all visible views.
        notifyDataSetChanged()
    }


    //TODO(1.6): Esta clase ViewHolder extiende de del view holder del recycler y se hace una funcion que bindea
    //cada Pokemon y su clickListener con la view y aca se setea lo que aparecerá en cada campo de la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with (itemView){
            tv_pokemon_id.text = item.id.toString()
            tv_pokemon_name.text = item.name
            tv_pokemon_type.text = item.url
            this.setOnClickListener { clickListener(item) }
        }
    }



}