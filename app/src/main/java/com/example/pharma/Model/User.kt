package com.example.pharma.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val telephone: String, val nss: String, val nom: String, val prenom: String, val addresse: String): Parcelable {
    constructor() : this("","","", "", "", "")
}