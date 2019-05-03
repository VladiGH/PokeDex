package com.avgh.pokedex2.models

import android.os.Parcel
import android.os.Parcelable

//TODO(1): se hace una data class con todos los campos que queremos de nuestro objeto y se hace parcelable para poder
//manejar mejor los objetos para serializarlo y se le asigna a cada valor un valor por default para que no sea nulo en algun momento
data class Pokemon (val id: Int = 0,
                    val name: String = "N/A",
                    val fsttype: String = "N/A",
                    val sndtype: String = "N/A",
                    val weight: String = "N/A",
                    val height: String = "N/A",
                    val url:String = "N/A",
                    val sprite:String = "N/A"
): Parcelable {
    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            name = parcel.readString(),
            fsttype = parcel.readString(),
            sndtype = parcel.readString(),
            weight = parcel.readString(),
            height = parcel.readString(),
            url = parcel.readString(),
            sprite = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(fsttype)
        parcel.writeString(sndtype)
        parcel.writeString(weight)
        parcel.writeString(height)
        parcel.writeString(url)
        parcel.writeString(sprite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Pokemon> {
            override fun createFromParcel(parcel: Parcel): Pokemon = Pokemon(parcel)
            override fun newArray(size: Int): Array<Pokemon?> = arrayOfNulls(size)
        }
    }

}
