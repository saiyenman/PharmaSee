package com.example.pharma.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class Pharmacie(val adresse: String, val ville: Int, val horaires: String, val telephone: String, val caisseConv: ArrayList<String>, val facebook: String, val longitude: Double, val latitude: Double ): Parcelable {
    constructor() : this("", 0,"","", arrayListOf(""), "", 0.0,  0.0)
}